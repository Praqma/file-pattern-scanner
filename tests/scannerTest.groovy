import org.junit.Test

// TODO
public class scannerTest {

    final String logScanner = "src/scanner.groovy"

    @Test
    public void testLogScanner() throws Exception {
        def rules = "tests/rules.yml"
        def targets = "tests/textFile.txt"

        def process = "groovy $logScanner $rules $targets".execute()
        def out = process.text
        def exitValue = process.exitValue()

        println "Exit value: $exitValue"
        println "Output: \n$out"

        assert out.contains("[ERROR] Contains non-ascii characters")
        assert out.contains("[WARNING] Contains JScript comments")
        assert !out.contains("[ERROR] File is empty")
        assert exitValue == 1
    }
}