@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.17')
import org.yaml.snakeyaml.Yaml
@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.17')
import org.yaml.snakeyaml.Yaml

doArgsCheck()
def rules = loadRules()

def resultIsFailed = false
def targetFiles = args[1..-1].collect { new File(it) }
targetFiles.each { file ->
    println "[FILE: $file]"
    rules.each { rule ->
        println "[RULE: $rule.title]"
        def matches = file.text.findAll(rule.match)
        if (triggers(rule, matches)) {
            if (rule.fail) resultIsFailed = true
            println expandMessage(rule, matches)
        }
    }
}

if (resultIsFailed)
    System.exit(1)

// Helper methods
// --------------

private void doArgsCheck() {
    if (!args || args.size() < 2) {
        println "ERROR: Missing file arguments"
        println "Usage: scanner [config] [targets]"
        System.exit(1)
    }
}

private ArrayList loadRules() {
    def rulesFile = new File(args[0])
    def rules = new Yaml().load(rulesFile.text).collect {
        [
                title  : it.key,
                match  : ~it.value.match,
                type   : it.value.type,
                fail   : it.value.fail,
                message: it.value.message
        ]
    }
    return rules
}

boolean triggers(def rule, def matches) {
    println "[$matches.size " + (matches.size == 1 ? "match" : "matches") + "]"
    def foundOnFind = matches.size > 0 && rule.type.equalsIgnoreCase("onFind")
    def missingOnMissing = matches.size == 0 && rule.type.equalsIgnoreCase("onMissing")
    return foundOnFind || missingOnMissing
}

String expandMessage(def rule, def matches) {
    def message = rule.message
    def groups = message =~ /(?<![$])(\$[1-9]\d*)/
    if (!groups.count) return message;
    for (int m = 1; m <= groups.count && m <= matches.size; m++) {
        message = rule.message.replaceAll(/(?<![$])(\$${m}(?!\d))/, matches[m - 1])
    }
    return message
}