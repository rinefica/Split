package split;

import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;
import split.exceptions.SplitArgumentException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ParamsSplitTest {

    @Test
    public void testCreate() throws CmdLineException, SplitArgumentException {
        ParamsSplit params = new ParamsSplit("-o - -d 123.txt -l 2".split(" "));
        ParamsSplit paramsAuto = new ParamsSplit("123.txt", "123",
                    ParamsSplit.DivFilesBy.lines, 2, true);

        assertEquals(paramsAuto, params);

        params = new ParamsSplit("-o out 123.txt -c 5".split(" "));
        paramsAuto = new ParamsSplit("123.txt", "out",
            ParamsSplit.DivFilesBy.chars, 5, false);
        assertEquals(paramsAuto, params);

        params = new ParamsSplit("-n 4 123.txt".split(" "));
        paramsAuto = new ParamsSplit("123.txt", ParamsSplit.BASE_INFO.BASE_NAME_OUTPUT_FILE,
            ParamsSplit.DivFilesBy.files, 4, false);
        assertEquals(paramsAuto, params);
    }

    @Test
    public void testException() {
        assertThrows(SplitArgumentException.class, () -> {
                new ParamsSplit("-o - -d 123.txt -l -2".split(" "));
            });

        assertThrows(SplitArgumentException.class, () -> {
                new ParamsSplit("-o - -d 123.txt -l 0".split(" "));
            });

        assertThrows(SplitArgumentException.class, () -> {
                new ParamsSplit("-o - notExistFile.txt -d".split(" ")).isCorrectCommand();
            });

        assertThrows(SplitArgumentException.class, () -> {
                new ParamsSplit("-o - -d 123.txt -n 1000".split(" ")).isCorrectCommand();
            });
    }

    @Test
    void toLettersName() {
        assertEquals("aa", ParamsSplit.toLettersName(0));
        assertEquals("ab", ParamsSplit.toLettersName(1));
        assertEquals("ba", ParamsSplit.toLettersName(26));
        assertEquals("zz", ParamsSplit.toLettersName(675));
    }

    @Test
    void createNameOutputByNumber() {
        String outputBaseName = "123";
        int[] numbers = {0, 1, 27, 28, 700};

        ParamsSplit.setBaseOutputFileName(outputBaseName);
        ParamsSplit.setNamingOutputByDigits(true);
        for (int i : numbers) {
            assertEquals(outputBaseName + i, ParamsSplit.createNameOutputByNumber(i));
        }

        ParamsSplit.setNamingOutputByDigits(false);
        for (int i : numbers) {
            assertEquals(outputBaseName + ParamsSplit.toLettersName(i),
                ParamsSplit.createNameOutputByNumber(i));
        }
    }

    @Test
    void createNamesOutputFiles() {
        String outputBaseName = "123";
        ParamsSplit.setBaseOutputFileName(outputBaseName);

        String[] answer = new String[4];

        for (int i = 0; i < 4; i++) {
            answer[i] = outputBaseName + i + ParamsSplit.BASE_INFO.BASE_FORMAT;
        }
        ParamsSplit.setNamingOutputByDigits(true);
        assertLinesMatch(new ArrayList<>(Arrays.asList(answer)),
            new ArrayList<>(Arrays.asList(ParamsSplit.createNamesOutputFiles(4))));

        for (int i = 0; i < 4; i++) {
            answer[i] = outputBaseName + ParamsSplit.toLettersName(i) + ParamsSplit.BASE_INFO.BASE_FORMAT;
        }
        ParamsSplit.setNamingOutputByDigits(false);
        assertLinesMatch(new ArrayList<>(Arrays.asList(answer)),
            new ArrayList<>(Arrays.asList(ParamsSplit.createNamesOutputFiles(4))));

    }
}