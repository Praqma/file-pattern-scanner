@Grab(group = 'org.yaml', module = 'snakeyaml', version = '1.17')
import org.yaml.snakeyaml.Yaml

final Yaml YAML = new Yaml()

if (!args || args.size() < 2) {
    println "ERROR: Missing file arguments"
    println "Usage:"
    println "scanner [config] [targets]"
    System.exit(1)
}

def rulesFile = new File(args[0])
def rules = []
YAML.load(rulesFile.text).each {
    rules.add([
            title: it.key,
            match: ~it.value.match,
            type: it.value.type,
            fail: it.value.fail,
            message: it.value.message
    ])
}

def resultIsFailure = false
def targetFiles = args[1..-1].collect{new File(it)}
targetFiles.each { file ->
    println "[FILE: $file]"
    def text = file.text
    rules.each { rule ->
        println "[RULE: $rule.title]"
        def matcher = text =~ rule.match
        println "[$matcher.count " + (matcher.count == 1 ? "match" : "matches") + "]"

        def foundOnFind = matcher.count > 0 && rule.type.equalsIgnoreCase("onFind")
        def missingOnMissing = matcher.count == 0 && rule.type.equalsIgnoreCase("onMissing")
        if(foundOnFind || missingOnMissing){
            if(rule.fail) resultIsFailure = true
            println rule.message
        }
    }
}

if(resultIsFailure)
    System.exit(1)