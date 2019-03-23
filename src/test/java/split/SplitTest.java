package split;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import com.google.common.io.Files;
import split.exceptions.SplitArgumentException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SplitTest {
    private File input;
    private BufferedWriter writer;

    private void startWithLines() {
        try {
            new File("src/test/files").mkdir();
            input = new File("src/test/files/123.txt");
            writer = new BufferedWriter(new FileWriter(input));
            for (String s : lines) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void startWithText() {
        try {
            new File("src/test/files").mkdir();
            input = new File("src/test/files/123.txt");
            writer = new BufferedWriter(new FileWriter(input));
            writer.write(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String[] lines = {"meow", "Hello!", "A vot"};
    private String text = "meow\nHello!\nA vot tak\nSmth important";

    @Test
    void testExceptions() {
        ByteArrayOutputStream newOut = new ByteArrayOutputStream();

        PrintStream console = System.out;
        PrintStream ps = new PrintStream(newOut);
        System.setOut(ps);

        Split.main("-o out -c 5".split(" "));
        System.out.flush();
        assertEquals("Argument \"name input file\" is required", newOut.toString());
        newOut.reset();

        Split.main("-o - -d 123.txt -l 2 -n 5".split(" "));
        System.out.flush();
        assertEquals("option \"-n\" cannot be used with the option(s) [-c, -l]", newOut.toString());
        newOut.reset();

        Split.main("345.txt".split(" "));
        System.out.flush();
        assertEquals(SplitArgumentException.FILE_NOT_EXIST, newOut.toString());
        newOut.reset();

        Split.main("-o - -d 123.txt -l 0".split(" "));
        System.out.flush();
        assertEquals(SplitArgumentException.USE_POS_OPT, newOut.toString());
        newOut.reset();

        Split.main("-o - -d 123.txt -n 703".split(" "));
        System.out.flush();
        assertEquals(SplitArgumentException.FILE_COUNT_LESS_701, newOut.toString());
        newOut.reset();

        Split.main("-o - -d 123.txt -l -5".split(" "));
        System.out.flush();
        assertEquals(SplitArgumentException.USE_POS_COUNT, newOut.toString());
        newOut.reset();

        System.setOut(console);
    }

    @Test
    void testL() {
        startWithLines();
        testByLines(1);
        testByLines(2);
        testByLines(3);
        testByLines(4);
        testByLines(100);
    }

    @Test
    void testN() {
        startWithText();
        testByFiles(1);
        testByFiles(2);
        testByFiles(4);
        testByFiles(36);
        testByFiles(37);
        testByFiles(100);
    }


    @Test
    void testC() {
        startWithText();
        testByChars(1);
        testByFiles(2);
        testByFiles(4);
        testByFiles(36);
        testByFiles(37);
        testByFiles(100);
    }

    private void equalsFiles(int countFiles, int countChars) throws IOException {
        String[] names = ParamsSplit.createNamesOutputFiles(countFiles);

        for (int i = 0; i < countFiles; i++) {

            input = new File("src/test/files/" + i + ".txt");
            writer = new BufferedWriter(new FileWriter(input));

            for(int j = 0; (j < countChars) && (j + i * countChars < text.length()) ; j++) {
                writer.write(text.charAt(j + i * countChars));
            }

            writer.close();

            assertTrue(Files.equal(input, new File(names[i])));
        }
    }

    private void testByChars(int countChars) {
        try {

            Split.main(("-d -c " + countChars + " -o - src/test/files/123.txt").split(" "));

            int countFiles = (int) Math.ceil((double) text.length() / countChars);

            if (countChars > text.length()) {
                countFiles = 1;
            }

            equalsFiles(countFiles, countChars);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testByFiles(int countFiles) {
        try {

            Split.main(("-d -n " + countFiles + " -o - src/test/files/123.txt").split(" "));

            int charsPerFile = (int) Math.ceil((double) text.length() / countFiles);

            if (countFiles > text.length()) {
                charsPerFile = 1;
            }

            equalsFiles(countFiles, charsPerFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void testByLines(int countLines) {
        try {
            Split.main(("-d -l " + countLines + " -o - src/test/files/123.txt").split(" "));

            int countFiles = (int) Math.ceil((double)3 / countLines);

            if (countLines > lines.length) {
                countFiles = 1;
            }

            int linesPerFile = (int) Math.ceil((double) lines.length / countFiles);

            String[] names = ParamsSplit.createNamesOutputFiles(countFiles);


            for (int i = 0; i < countFiles; i++) {

                input = new File("src/test/files/" + i + ".txt");
                writer = new BufferedWriter(new FileWriter(input));

                for(int j = 0; (j < linesPerFile) && (j + i * linesPerFile < lines.length) ; j++) {
                    writer.write(lines[j + i * linesPerFile]);
                    writer.newLine();
                }

                writer.close();

                assertTrue(Files.equal(input, new File(names[i])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @AfterEach
    void SplitTest() {
        try {
            FileUtils.deleteDirectory(new File("src/test/files/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}