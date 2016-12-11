@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.17')
import org.yaml.snakeyaml.Yaml

// automated version number, added by build automation
String versionNumber = "0.0.0-dev"
println "file-pattern-scanner version $versionNumber"

doArgsCheck()


def rules = loadRules()

def resultIsFailed = false
def targetFiles = args[1..-1].collect{new File(it)}
targetFiles.each { file ->
    println "[FILE: $file]"
    def text = file.text
    rules.each { rule ->
        println "[RULE: $rule.title]"
        if(triggers(rule, text)){
            if(rule.fail) resultIsFailed = true
            println rule.message
        }
    }
}

if(resultIsFailed)
    System.exit(1)

// Helper methods
// --------------

private void doArgsCheck() {
    if (!args || args.size() < 2) {
        println "ERROR: Missing file arguments"
        println "Usage:"
        println "scanner [config] [targets]"
        System.exit(1)
    }
}

private ArrayList loadRules() {
    def rulesFile = new File(args[0])
    def rules = new Yaml().load(rulesFile.text).collect {[
                title  : it.key,
                match  : ~it.value.match,
                type   : it.value.type,
                fail   : it.value.fail,
                message: it.value.message
    ]}
    return rules
}

boolean triggers(def rule, String text) {
    def matcher = text =~ rule.match
    println "[$matcher.count " + (matcher.count == 1 ? "match" : "matches") + "]"
    def foundOnFind = matcher.count > 0 && rule.type.equalsIgnoreCase("onFind")
    def missingOnMissing = matcher.count == 0 && rule.type.equalsIgnoreCase("onMissing")
    return foundOnFind || missingOnMissing
}
