@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.17')
import org.yaml.snakeyaml.Yaml

class Scanner {
    static void main(String[] args) {
        // Check arguments
        if (!args || args.size() < 2) {
            println "ERROR: Missing file arguments"
            println "Usage:"
            println "scanner [config] [targets]"
            System.exit(1)
        }
        def rulesFile = new File(args[0])
        def targetFiles = args[1..-1].collect{ new File(it) }

        def filesMissing = false
        if (!rulesFile.exists()) {
            println "ERROR: Rule file '$rulesFile' not found"
            filesMissing = true
        }

        targetFiles.each {
            if (it.exists()) { return }
            println "ERROR: Target file '$it' not found"
            filesMissing = true
        }

        if (filesMissing) { 
            System.exit(1) 
        }

        // Parse rules
        def rules = new Yaml().load(rulesFile.text).collect {[
                    title  : it.key,
                    match  : ~it.value.match,
                    type   : it.value.type,
                    fail   : it.value.fail,
                    message: it.value.message
        ]}

        // Apply rules to target files
        def consideredFailed = false
        targetFiles.each { file ->
            println "[FILE: $file (exists: ${file.exists()})]"
            rules.each { rule ->
                println "[RULE: $rule.title]"

                // RegEx match
                def matcher = file.text =~ rule.match
                println "[$matcher.count " + (matcher.count == 1 ? "match" : "matches") + "]"

                // Hits on Find or Missing
                def onFind = matcher.count > 0 && rule.type.equalsIgnoreCase("onFind")
                def onMissing = matcher.count == 0 && rule.type.equalsIgnoreCase("onMissing")

                // If the rule found a hit
                if (onFind || onMissing) {
                    println rule.message
                    if (rule.fail) {
                        consideredFailed = true
                    }
                }
            }
        }

        System.exit(consideredFailed ? 1 : 0)
    }
}
