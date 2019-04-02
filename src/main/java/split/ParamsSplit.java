package split;

import org.kohsuke.args4j.CmdLineException;
import split.exceptions.SplitArgumentException;

import java.io.File;
import java.util.Objects;

/**
 * @author rinefica
 * @version 1
 *
 * Класс, преобразующий значения из {@link ParamsCLI} в логически необходимые для {@link Split}.
 * Проверки {@link SplitArgumentException} на корректность введенной задачи (существующий непустой входной файл,
 * неотрицательные значения деления файла).
 *
 * Поля:
 *      <b>String</b> BASE_NAME_OUTPUT_FILE
 *                      базовое имя выходного файла по умолчанию ("x")
 *
 *      <b>String</b> BASE_FORMAT
 *                      формат файлов (.txt по умолчанию)
 *
 *      <b>String</b> inputFileName
 *                      имя входного файла
 *
 *      <b>String</b> baseOutputFileName
 *                      базовое имя выходного файла
 *
 *      <b>DivFilesBy</b> divElement
 *                      по какому базису происходит деление: lines, chars, files - строки, символы, файлы,
 *                      по умолчанию - строки
 *
 *      <b>int</b> countDivElements
 *                      количество элементов (символов или строк) на один файл или общее количество файлов,
 *                      по умолчанию - 100
 *
 *      <b>boolean</b> namingOutputByDigits
 *                      правило наименования выходных файлов,
 *                      при true - с помощью цифр, false - букв (от 'aa' до 'zz')
 *
 *
 */

public class ParamsSplit {

    public enum DivFilesBy {
        lines, chars, files
    }

    static class BASE_INFO {
        static final String BASE_NAME_OUTPUT_FILE = "x";
        static final String BASE_FORMAT = ".txt";
    }

    private static String inputFileName;
    private static String baseOutputFileName = BASE_INFO.BASE_NAME_OUTPUT_FILE;

    private static DivFilesBy divElement = DivFilesBy.lines;
    private static int countDivElements = 100;

    private static boolean namingOutputByDigits;

    /**
     * Конструктор для ручного создания (необходим для тестирования)
     *
     * @param inputFileName входной файл
     * @param baseOutputFileName выходной файл
     * @param divElement элемент деления
     * @param countDivElements количество элементов деления
     * @param namingOutputByDigits наименование выходных файлов с числами
     */
    public ParamsSplit(String inputFileName, String baseOutputFileName,
                       DivFilesBy divElement, int countDivElements, boolean namingOutputByDigits) {
        this.inputFileName = inputFileName;
        this.baseOutputFileName = baseOutputFileName;
        this.divElement = divElement;
        this.countDivElements = countDivElements;
        this.namingOutputByDigits = namingOutputByDigits;
    }


    /**
     * Конструктор
     * @param args - аргументы командной строки
     * @throws CmdLineException - ошибки ввода команды
     * @throws SplitArgumentException - логические ошибки команды
     */
    public ParamsSplit(String[] args) throws CmdLineException, SplitArgumentException {
        ParamsCLI params = new ParamsCLI(args);

        inputFileName = params.getInputFile();
        if (inputFileName != null && inputFileName.endsWith(".txt")) {
            inputFileName = inputFileName.substring(0, inputFileName.lastIndexOf(".txt"));
        }

        if (params.getOutputFile().equals("-")) {
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

    /**
     * Перевести из числа в буквенное обозначение (0 - 'aa', 1 - 'ab' etc)
     * @param i - число для преобразования в буквенную комбинацию
     * @return строка из соответствующих букв латинского алфавита
     */
    static String toLettersName(int i) {
        char first = (char)('a' + i / 26);
        char second = (char)('a' + i % 26);

        return "" + first + second;
    }

    /**
     * Создать имя выходного файла
     * @param number номер файла
     * @return имя
     */
    static String createNameOutputByNumber(int number) {
        if (namingOutputByDigits) {
            return baseOutputFileName + number;
        } else {
            return baseOutputFileName + toLettersName(number);
        }
    }

    /**
     * Создать массив из имен выходных файлов
     * @param countOutputFiles количество файлов
     * @return массив имен
     */
    public static String[] createNamesOutputFiles(int countOutputFiles) {
        String[] names = new String[countOutputFiles];

        if (namingOutputByDigits) {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + i + BASE_INFO.BASE_FORMAT;
            }
        } else {
            for (int i = 0; i < countOutputFiles; i++) {
                names[i] = baseOutputFileName + toLettersName(i) + BASE_INFO.BASE_FORMAT;
            }
        }

        return names;
    }

    /**
     * Проверка на корректность: входной файл не существует, пуст или
     *  количество выходных файлов больше возможного для поддержки при буквенном именовании
     * @throws SplitArgumentException
     */
    public static void isCorrectCommand() throws SplitArgumentException {
        if (countDivElements > 26*26 - 1)
            throw new SplitArgumentException(SplitArgumentException.FILE_COUNT_LESS_701);

        File input = new File(inputFileName);
        if (!(input.exists()))
            throw new SplitArgumentException(SplitArgumentException.FILE_NOT_EXIST);

        if (input.length() == 0)
            throw new SplitArgumentException(SplitArgumentException.FILE_IS_EMPTY);
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputFileName, baseOutputFileName, divElement, countDivElements, namingOutputByDigits);
    }
}
