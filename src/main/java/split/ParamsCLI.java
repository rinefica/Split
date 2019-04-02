package split;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

/**
 * @author rinefica
 * @version 1
 *
 * Класс, получающий значения из терминального вызова. Проверки на корректность записи вызова.
 *
 * Поля:
 *      <b>String</b> inputFile
 *              имя файла, который нужно считать;
 *
 *      <b>String</b> outputFile
 *              базовое имя для выходного файла;
 *
 *      <b>boolean</b> namingOutputByDigits
 *              true - именование файлов при помощи цифр, false - именование при помощи буквенной последовательности
 *
 *      <b>int</b> divByLines
 *              число строк для одного файла (если задействован аргумент -l)
 *
 *      <b>int</b> divByChars
 *              число символов для одного файла (если задействован аргумент -c)
 *
 *      <b>int</b> countFiles
 *              выходное число файлов (если задействован аргумент -n)
 *
 *      <b>boolean</b> isBasedDiv
 *              true - файл делится по умолчанию (100 строк), false - правило деления файла задано
 *
 */

public class ParamsCLI {

    @Option(name = "-o", usage = "Set output file name")
    private String outputFile = ParamsSplit.BASE_INFO.BASE_NAME_OUTPUT_FILE;

    @Option(name = "-d")
    private boolean namingOutputByDigits;

    @Option(name = "-l", forbids = {"-c", "-n"})
    private int divByLines;

    @Option(name = "-c", forbids = {"-l", "-n"})
    private int divByChars;

    @Option(name = "-n", forbids = {"-c", "-l"})
    private int countFiles;

    @Argument(required = true, metaVar = "name input file")
    private String inputFile;

    private boolean isBasedDiv;

    public boolean isNamingOutputByDigits() {
        return namingOutputByDigits;
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public int getDivByLines() {
        return divByLines;
    }

    public int getDivByChars() {
        return divByChars;
    }

    public int getCountFiles() {
        return countFiles;
    }

    public boolean isBasedDiv() {
        return isBasedDiv;
    }

    public ParamsCLI(String outputFile, boolean namingOutputByDigits, int divByLines,
                     int divByChars, int countFiles, String inputFile, boolean isBasedDiv) {
        this.outputFile = outputFile;
        this.namingOutputByDigits = namingOutputByDigits;
        this.divByLines = divByLines;
        this.divByChars = divByChars;
        this.countFiles = countFiles;
        this.inputFile = inputFile;
        this.isBasedDiv = isBasedDiv;
    }



    public ParamsCLI(String[] args) throws CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);
        parser.parseArgument(args);

        isBasedDiv = true;
        for (String option : args) {
            if (option.equals("-l") || option.equals("-c") || option.equals("-n")) {
                isBasedDiv = false;
                break;
            }
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ParamsCLI params = (ParamsCLI) obj;
        return (outputFile.equals(params.outputFile)) && (inputFile.equals(params.inputFile)) &&
                (namingOutputByDigits == params.namingOutputByDigits) &&
                (divByLines == params.divByLines) && (divByChars == params.divByChars) &&
                (countFiles == params.countFiles);
    }

    @Override
    public String toString() {
        return "ParamsCLI{" +
                "outputFile='" + outputFile + '\'' +
                ", namingOutputByDigits=" + namingOutputByDigits +
                ", divByLines=" + divByLines +
                ", divByChars=" + divByChars +
                ", countFiles=" + countFiles +
                ", isBasedDiv=" + isBasedDiv +
                ", inputFile='" + inputFile + '\'' +
                '}';
    }
}
