package split;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ParamsCLITest {
    private static PrintStream console;
    private static ByteArrayOutputStream newOut;

    String input = "123.txt";
    String output = "-";
    String paramO = "-o";
    String paramD = "-d";
    String paramN = "-n";
    String paramL = "-l";
    String paramC = "-c";

    String i1 = "-o - -d 123.txt -l 2";
    String i2 = "-o - -d 123.txt -l 2 -n 5";
    String i3 = "123.txt";
    String i4 = "-o out 123.txt -c 5";


    @Test
    public void createParams(){

        ParamsCLI paramsAuto = new ParamsCLI(i1.split(" "));
        ParamsCLI params = new ParamsCLI(output, true,
                2, 0, 0, input, false);
        assertEquals(params, paramsAuto);

        paramsAuto = new ParamsCLI(i3.split(" "));
        params = new ParamsCLI(ParamsSplit.BASE_NAME_OUTPUT_FILE, false,
                0, 0, 0, input, true);
        assertEquals(params, paramsAuto);

        paramsAuto = new ParamsCLI(i4.split(" "));
        params = new ParamsCLI("out", false,
                0, 5, 0, input, false);
        assertEquals(params, paramsAuto);

    }

    @Test
    public void testExceptions() {
        new ParamsCLI(i2.split(" "));

        System.out.flush();
        assertEquals("option \"-n\" cannot be used with the option(s) [-c, -l]\n", newOut.toString());

        newOut.reset();

        new ParamsCLI("-o out -c 5".split(" "));

        System.out.flush();
        assertEquals("Argument \"name input file\" is required\n", newOut.toString());

    }

    @BeforeAll
    public static void start() {
        console = System.out;
        newOut = new ByteArrayOutputStream();
        PrintStream myStream = new PrintStream(newOut);
        System.setOut(myStream);
    }

    @AfterAll
    public static void end() {
        System.setOut(console);
    }

}