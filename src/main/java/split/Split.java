package split;

import org.kohsuke.args4j.CmdLineException;
import split.exceptions.SplitArgumentException;

import java.io.*;

/**
 * @author rinefica
 * @version 1
 *
 * Класс, разделяющий входной файл на выходные по заданным правилам
 *
 *
 */

public class Split {

    private static String endOfLine = System.getProperty("line.separator");
    private ParamsSplit params;

    /**
     * Конструктор
     * @param args
     * @throws CmdLineException при нарушении правил записи команды
     * @throws SplitArgumentException при логическом нарушении команды
     */
    public Split(String[] args) throws CmdLineException, SplitArgumentException {
        params = new ParamsSplit(args);
        params.isCorrectCommand();
    }

    /**
     * Непосредственная запись для правил деления по количеству символов\файлов
     * @param reader обертка для чтения из входного файла
     * @param countFiles количество полных выходных файлов
     * @param curLength длина каждого выходного файла в символах
     * @param lastFileSize размер последнего файла при делении файлов не нацело
     */
    private static void writeFiles(BufferedReader reader, int countFiles, int curLength,
                            int lastFileSize) {
        try {
            String[] nameOutputFile = ParamsSplit.createNamesOutputFiles(countFiles);
            char[] curData = new char[curLength];

            for (int i = 0 ; i < countFiles; i++) {
                File output = new File(nameOutputFile[i]);
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                if (reader.read(curData) > 0) {
                    writer.write(curData);
                }
                writer.close();
            }

            if (lastFileSize != 0){
                File output = new File(
                    ParamsSplit.createNameOutputByNumber(countFiles) + ParamsSplit.BASE_INFO.BASE_FORMAT);
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                curData = new char[lastFileSize];
                reader.read(curData);
                writer.write(curData);
                writer.close();
            }
            reader.close();
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Подготовка к записи по правилу количества файлов
     * @param inputFile наименование входного файла
     * @param reader обертка для чтения из входного файла
     */
    private void writeByCountFiles(File inputFile, BufferedReader reader){
        try {
            int countFiles = params.getCountDivElements();
            int curLength = (int) (inputFile.length() / countFiles);
            int lastFileSize = (int) (inputFile.length() % countFiles);

            if (countFiles > inputFile.length()) {
                curLength = 1;
                lastFileSize = 0;
            }

            if (lastFileSize != 0) {
                countFiles--;
                lastFileSize += curLength;
            }

            writeFiles(reader, countFiles, curLength, lastFileSize);

        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Подготовка к записи по правилу количества символов
     * @param inputFile наименование входного файла
     * @param reader обертка для чтения из входного файла
     */
    private void writeByChars(File inputFile, BufferedReader reader){
        try {
            int curLength = params.getCountDivElements();
            int countFiles = (int) (inputFile.length() / curLength);

            writeFiles(reader, countFiles, curLength, (int)(inputFile.length() % curLength));

        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Запись по количеству строк в каждом выходном файле. Непосредственная запись.
     * @param reader обертка для чтения из входного файла
     */
    private void writeByLines(BufferedReader reader) {
        try {
            int linesPerFile = params.getCountDivElements();
            int curNumberFile = 0;
            String curData = "";
            boolean endWriting = false;
            boolean writeDataInFile;

            while (!endWriting) {
                writeDataInFile = false;

                File output = new File(
                    ParamsSplit.createNameOutputByNumber(curNumberFile) + ParamsSplit.BASE_INFO.BASE_FORMAT);
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));

                for (int i = 0; i < linesPerFile &&
                        ((curData = reader.readLine()) != null); i++) {
                    writer.append(curData).append(endOfLine);
                    writeDataInFile = true;
                }

                if (!writeDataInFile) {
                    output.delete();
                }

                if (curData == null) {
                    endWriting = true;
                }

                writer.close();
                curNumberFile++;
            }
            reader.close();

        } catch (Exception e) {
            System.err.print(e.getMessage());
        }
    }

    /**
     * Запись файлов: чтение входного файла и вызов функции для соответстующего элемента деления
     * @throws SplitArgumentException если правила некорректны (отрицательные параметры,
     *  пустой\несуществующий входной файл)
     */
    public void writeFiles() throws SplitArgumentException {

        params.isCorrectCommand();

        File inputFile = new File(params.getInputFileName());

        try (
            BufferedReader reader = new BufferedReader(new FileReader(inputFile))
        ) {
            if (params.getDivElement() == ParamsSplit.DivFilesBy.files) {
                writeByCountFiles(inputFile, reader);
            }

            if (params.getDivElement() == ParamsSplit.DivFilesBy.chars) {
                writeByChars(inputFile, reader);
            }

            if (params.getDivElement() == ParamsSplit.DivFilesBy.lines) {
                writeByLines(reader);
            }
        } catch (IOException e) {
            System.err.print(e.getMessage());
        }

    }

    /**
     * Точка входа в программу
     * @param args - параметры для деления файла
     */
    public static void main(String[] args) {
        try {
            Split split = new Split(args);
            split.writeFiles();
        } catch (CmdLineException | SplitArgumentException e) {
            System.err.print(e.getMessage());
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
