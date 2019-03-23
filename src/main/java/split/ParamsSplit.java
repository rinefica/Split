package split;

import org.kohsuke.args4j.CmdLineException;
import split.exceptions.SplitArgumentException;

import java.io.File;
import java.util.Objects;

public class ParamsSplit {

    public enum DivFilesBy {
        lines, chars, files
    }

    static final String BASE_NAME_OUTPUT_FILE = "x";
    static final String BASE_FORMAT = ".txt";

    private String inputFileName;
    private static String baseOutputFileName;

    private DivFilesBy divElement = DivFilesBy.lines;
    private int countDivElements = 100;

    private static boolean namingOutputByDigits;

    public ParamsSplit(String inputFileName, String baseOutputFileName,
                       DivFilesBy divElement, int countDivElements, boolean namingOutputByDigits) {
        this.inputFileName = inputFileName;
        this.baseOutputFileName = baseOutputFileName;
        this.divElement = divElement;
        this.countDivElements = countDivElements;
        this.namingOutputByDigits = namingOutputByDigits;
    }

    public ParamsSplit(String[] args) throws CmdLineException, SplitArgumentException {
        ParamsCLI params = new ParamsCLI(args);

        inputFileName = params.getInputFile();
        if (inputFileName != null && inputFileName.endsWith(".txt")) {
            inputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".txt"));
        }

        if (params.getOutputFile() == null) {
            baseOutputFileName = BASE_NAME_OUTPUT_FILE;
        } else if (params.getOutputFile().equals("-")) {
            baseOutputFileName = inputFileName;
        } else {
            baseOutputFileName = params.getOutputFile();
        }

        inputFileName = params.getInputFile();

        namingOutputByDigits = params.isNamingOutputByDigits();

        if (params.isBasedDiv()) {
            return;
        }

        int lines = params.getDivByLines();
        int chars = params.getDivByChars();
        int files = params.getCountFiles();

        if (lines == 0 && chars == 0 && files == 0) {
            throw new SplitArgumentException(SplitArgumentException.USE_POS_OPT);
        }

        if (lines < 0 || chars < 0 || files < 0) {
            throw new SplitArgumentException(SplitArgumentException.USE_POS_COUNT);
        }

        if (lines != 0) {
            countDivElements = lines;
        } else if (chars != 0) {
            divElement = DivFilesBy.chars;
            countDivElements = chars;
        } else if (files != 0) {
            divElement = DivFilesBy.files;
            countDivElements = files;
        }

    }

    static String toLettersName(int i) {
        char first = (char)('a' + i / 27);
        char second = (char)('a' + i % 27);

        return "" + first + second;
    }

    static String createNameOutputByNumber(int number) {
        if (namingOutputByDigits) {
            return baseOutputFileName + number;
        } else {
            return baseOutputFileName + toLettersName(number);
        }
    }

    public static String[] createNamesOutputFiles(int countOutputFiles) {
        String[] names = new String[countOutputFiles];

        if (namingOutputByDigits) {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + i + BASE_FORMAT;
            }
        } else {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + toLettersName(i) + BASE_FORMAT;
            }
        }

        return names;
    }

    public void isCorrectCommand() throws SplitArgumentException {
        if (divElement.equals(DivFilesBy.files) && countDivElements > 26*27 - 1)
            throw new SplitArgumentException(SplitArgumentException.FILE_COUNT_LESS_701);

        if (!(new File(inputFileName).exists()))
            throw new SplitArgumentException(SplitArgumentException.FILE_NOT_EXIST);
    }


    public String getInputFileName() {
        return inputFileName;
    }

    public DivFilesBy getDivElement() {
        return divElement;
    }

    public int getCountDivElements() {
        return countDivElements;
    }

    public static void setNamingOutputByDigits(boolean namingOutputByDigits) {
        ParamsSplit.namingOutputByDigits = namingOutputByDigits;
    }

    public static void setBaseOutputFileName(String baseOutputFileName) {
        ParamsSplit.baseOutputFileName = baseOutputFileName;
    }
    @Override
    public String toString() {

        StringBuilder answer = new StringBuilder();

        answer  .append("Params:").append("\n")
                .append("input file: ").append(inputFileName).append("\n")
                .append("output file: ").append(baseOutputFileName)
                .append(" with naming by ");

        if (namingOutputByDigits) {
            answer.append("digits");
        } else {
            answer.append("letters");
        }

        answer  .append("\n").append("divide by: ").append(countDivElements)
                .append(" ").append(divElement);
        return answer.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParamsSplit that = (ParamsSplit) o;
        return countDivElements == that.countDivElements &&
            Objects.equals(inputFileName, that.inputFileName) &&
            divElement == that.divElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputFileName, baseOutputFileName, divElement, countDivElements, namingOutputByDigits);
    }
}
