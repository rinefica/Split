package split;

import java.io.File;

public class ParamsSplit {

    enum DivFilesBy {
        lines, chars, files
    }

    static final String BASE_NAME_OUTPUT_FILE = "x";

    private String inputFileName;
    private String baseOutputFileName;


    private DivFilesBy divElement = DivFilesBy.lines;
    private int countDivElements = 100;

    private boolean namingOutputByDigits;

    public ParamsSplit(String[] args){
        ParamsCLI params = new ParamsCLI(args);

        inputFileName = params.getInputFile();
        if (inputFileName.endsWith(".txt")) {
            inputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".txt"));
        }

        if (params.getOutputFile() == null) {
            baseOutputFileName = BASE_NAME_OUTPUT_FILE;
        } else if (params.getOutputFile().equals("-")) {
            baseOutputFileName = inputFileName;
        } else {
            baseOutputFileName = params.getOutputFile();
        }

        namingOutputByDigits = params.isNamingOutputByDigits();

        if (params.isBasedDiv()) {
            return;
        }

        int lines = params.getDivByLines();
        int chars = params.getDivByChars();
        int files = params.getCountFiles();

        if (lines == 0 && chars == 0 && files == 0) {
            throw new IllegalArgumentException("Must be used -l, -n or -c with number > 0");
        }

        if (lines < 0 || chars < 0 || files < 0) {
            throw new IllegalArgumentException("Count div elements must be > 0");
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

    private static String toLettersName(int i) {
        char first = (char)('a' + i / 27);
        char second = (char)('a' + i % 27);

        return "" + first + second;
    }

    public String createNameOutputByNumber(int number) {
        if (namingOutputByDigits) {
            return baseOutputFileName + number;
        } else {
            return baseOutputFileName + toLettersName(number);
        }
    }

    public String[] createNamesOutputFiles(int countOutputFiles) {

        String[] names = new String[countOutputFiles];

        StringBuffer baseName = new StringBuffer(baseOutputFileName);

        if (namingOutputByDigits) {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + i;
            }
        } else {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + toLettersName(i);
            }
        }

        return names;
    }

    public void isCorrectCommand() {
        if (divElement.equals(DivFilesBy.files) && countDivElements > 27*27)
            throw new IllegalArgumentException("Count of files must be < 729");

        if (!(new File(inputFileName).exists()))
            throw new IllegalArgumentException("Input file does not exist");
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

    @Override
    public String toString() {

        StringBuffer answer = new StringBuffer();

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
}
