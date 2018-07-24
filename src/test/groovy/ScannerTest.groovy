import org.junit.Test

public class ScannerTest {

    final String scannerScript = "src/main/groovy/Scanner.groovy"

    @Test
    public void testLogScanner() throws Exception {
        def rules = "src/test/groovy/rules.yml"
        def targets = "src/test/groovy/textFile.txt"

        def out = new StringBuffer()
        def err = new StringBuffer()
        def process = "groovy $scannerScript $rules $targets".execute()
        process.consumeProcessOutput(out, err)
        def exitValue = process.waitFor()

        println "Exit value: $exitValue"
        println "Output: \n$out"
        println "Error: \n${err}"

        assert !err
        assert out.contains("[ERROR] Contains non-ascii characters")
        assert out.contains("[WARNING] Contains JScript comments")
        assert !out.contains("[ERROR] File is empty")
        assert exitValue == 1
    }
}