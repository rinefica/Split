package split;

import org.kohsuke.args4j.CmdLineException;
import split.exceptions.SplitArgumentException;

import java.io.*;

/**
 * @author rinefica
 * @version 1
 *
 * Поля:    <b>String</b> inputFileName
 *                  имя файла, который нужно считать
 *
 *          <b>String</b> outputFileName
 *                  базовое имя для выходных файлов, по умолчанию <i>"x"</i>
 *
 *          <b>boolean</b> useConstNumberOfFiles
 *                  если <b>true</b>, должно быть задано количество выходных файлов
 *                  (размер каждого выходного файла высчитывается автоматически),
 *                  по умолчанию <b>false</b>
 *
 *          <b>int</b> countConstNumberOfFiles
 *                  высчитывается автоматически, если useConstNumberOfFiles <b>true</b>,
 *                  иначе не задействуется
 *
 *          <b>boolean</b> useDivByLines
 *                  если <b>true</b>, деление на выходные файлы происходит по количеству строк,
 *                  если <b>false</b>, деление на выходные файлы происходит по количеству символов
 *
 *          <b>int</b> numberDivElements
 *                  количество элементов, по которому происходит деление на файлы,
 *                  <i>100</i> по умолчанию для строк
 *
 *          <b></b>
 *
 * Методы:
 */

public class Split {

    private ParamsSplit params;

    public Split(String[] args) throws CmdLineException, SplitArgumentException {
        params = new ParamsSplit(args);
        params.isCorrectCommand();
    }

    private void writeFiles(BufferedReader reader, int countFiles, int curLength,
                            int lastFileSize) {
        try {
            String[] nameOutputFile = ParamsSplit.createNamesOutputFiles(countFiles);
            char[] curData = new char[curLength];

            for (int i = 0 ; i < countFiles; i++) {
                File output = new File(nameOutputFile[i]);
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                reader.read(curData);
                writer.write(curData);
                writer.close();
            }

            if (lastFileSize != 0){
                File output = new File(ParamsSplit.createNameOutputByNumber(countFiles));
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));
                curData = new char[lastFileSize];
                reader.read(curData);
                writer.write(curData);
                writer.close();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeByCountFiles(File inputFile, BufferedReader reader){
        try {
            int countFiles = params.getCountDivElements();
            int curLength = (int) (inputFile.length() / countFiles);
            System.out.println((int) (inputFile.length() / countFiles));

            writeFiles(reader, countFiles, curLength, (int)(inputFile.length() % countFiles));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeByChars(File inputFile, BufferedReader reader){
        try {
            int curLength = params.getCountDivElements();
            int countFiles = (int) (inputFile.length() / curLength);

            writeFiles(reader, countFiles, curLength, (int)(inputFile.length() % curLength));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeByLines(BufferedReader reader) {
        try {
            int linesPerFile = params.getCountDivElements();
            int curNumberFile = 0;
            String curData = "";
            boolean endWriting = false;
            boolean writeDataInFile;

            while (!endWriting) {
                writeDataInFile = false;

                File output = new File(ParamsSplit.createNameOutputByNumber(curNumberFile) + ParamsSplit.BASE_FORMAT);
                BufferedWriter writer = new BufferedWriter(new FileWriter(output));

                for (int i = 0; i < linesPerFile &&
                        ((curData = reader.readLine()) != null); i++) {
                    writer.append(curData).append('\n');
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
            e.printStackTrace();
        }
    }

    public void writeFiles() throws SplitArgumentException {

        params.isCorrectCommand();

        File inputFile = new File(params.getInputFileName());
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(inputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (params.getDivElement() == ParamsSplit.DivFilesBy.files) {
            writeByCountFiles(inputFile, reader);
        }

        if (params.getDivElement() == ParamsSplit.DivFilesBy.chars) {
            writeByChars(inputFile, reader);
        }

        if (params.getDivElement() == ParamsSplit.DivFilesBy.lines) {
            writeByLines(reader);
        }
    }

    public static void main(String[] args) {
        try {
            Split split = new Split(args);
            split.writeFiles();
        } catch (CmdLineException | SplitArgumentException e) {
            System.out.println(e.getMessage());
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
