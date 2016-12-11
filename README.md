# File Pattern Scanner

A simple tool for scanning files for predefined patterns and generating messages depending on its matches.

## Usage

### Defining rules

In a rule you define: 
 * The name of the rule
 * The pattern to look for
    *   RegEx
 * When the rule is triggered 
    * When a match **is** found
    * When a match **isn't** found
 * If the script fails when the rule is triggered
 * The message to print when the rule is triggered

Rules follow a simple format:

```YAML
{NAME}:
  match: {Matcher RegEx}
  type: {onMissing/onFind}
  fail: {true/false}
  message: {Message string}
```

Example:

```YAML
failOnNonAscii:
  match: ([^\x00-\x7F]+\ *(?:[^\x00-\x7F]| )*)
  type: onFind
  fail: true
  message: "ERROR: File contains non-ASCII characters"
warnOnComments:
  match: \/\*[\s\S]*?\*\/|\/\/.*
  type: onFind
  fail: false
  message: "WARNING: File still contains JavaScript comments"
```

### Calling the script

After defining the rules, pass them into the script alongside the files you want scanned as arguments.
   
Example:
`groovy scanner rules.yml myFile.log`

Output for our example:
```
[FILE: myFile.log]
[RULE: failOnNonAscii]
[4 matches]
ERROR: File contains non-ASCII characters
[RULE: warnOnComments]
[1 match]
WARNING: File still contains JavaScript comments

Process finished with exit code 1
```

## Support, contributions and maintenance

* Contributions are welcome, see [contributing file](/CONTRIBUTING.md).
* PAC is maintained in the scope of [JOSRA](http://www.josra.org/).
* Issue and work tracking is done using [Github issues](https://github.com/Praqma/file-pattern-scanner/issues)
* Support requests and questions can be created as Github issue or send us an mail on support@praqma.net
* Our roadmap is availbe in [roadmap](/roadmap.md)

## Changes

See [Releases](https://github.com/buep/file-pattern-scanner/release).
