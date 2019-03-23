package split;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class ParamsCLITest {

    @Test
    public void createParams() throws CmdLineException {
        String input = "123.txt";
        String output = "-";

        ParamsCLI paramsAuto = new ParamsCLI("-o - -d 123.txt -l 2".split(" "));
        ParamsCLI params = new ParamsCLI(output, true,
                2, 0, 0, input, false);
        assertEquals(params, paramsAuto);

        paramsAuto = new ParamsCLI("123.txt".split(" "));
        params = new ParamsCLI(ParamsSplit.BASE_NAME_OUTPUT_FILE, false,
                0, 0, 0, input, true);
        assertEquals(params, paramsAuto);

        paramsAuto = new ParamsCLI("-o out 123.txt -c 5".split(" "));
        params = new ParamsCLI("out", false,
                0, 5, 0, input, false);
        assertEquals(params, paramsAuto);

    }

    @Test
    public void testExceptions() {
        assertThrows(CmdLineException.class, ()->{
            new ParamsCLI("-o - -d 123.txt -l 2 -n 5".split(" "));
        });

        assertThrows(CmdLineException.class, ()->{
            new ParamsCLI("-o out -c 5".split(" "));
        });
    }

}