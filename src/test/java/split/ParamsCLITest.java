package split;

import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import static org.junit.jupiter.api.Assertions.*;

class ParamsCLITest {
    String input = "123.txt";
    String output = "-";
    String paramO = "-o";
    String paramD = "-d";
    String paramN = "-n";
    String paramL = "-l";
    String paramC = "-c";

    String first = "-o - -d 123.txt -n 2";
    String second = "-o - -d 123.txt -n 2 -l 5";


    @Test
    public void createParams(){
        String[] args = first.split(" ");
        ParamsCLI paramsAuto = new ParamsCLI(args);

        ParamsCLI params = new ParamsCLI(output, true,
                0, 0, 2, input);

        assertEquals(params, paramsAuto);

        args = second.split(" ");
        System.out.println(new ParamsCLI(args));

    }

}