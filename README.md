# File Pattern Scanner

A simple tool for scanning files for predefined patterns and generating messages depending on its matches.

## Defining rules

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

## Calling the script

Define rules in a YAML config file and pass your config/files you want scanned as script arguments.
   
`groovy scanner rules.yml myFile.log`