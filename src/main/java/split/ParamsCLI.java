package split;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

public class ParamsCLI {

    @Option(name = "-o", usage = "Set output file name")
    private String outputFile;

    @Option(name = "-d")
    private boolean namingOutputByDigits;

    @Option(name = "-l", forbids = {"-c", "-n"})
    private int divByLines;

    @Option(name = "-c", forbids = {"-l", "-n"})
    private int divByChars;

    @Option(name = "-n", forbids = {"-c", "-l"})
    private int countFiles;

    @Argument(required = true)
    private String inputFile;

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

    public ParamsCLI(String outputFile, boolean namingOutputByDigits,
                     int divByLines, int divByChars, int countFiles, String inputFile) {
        this.outputFile = outputFile;
        this.namingOutputByDigits = namingOutputByDigits;
        this.divByLines = divByLines;
        this.divByChars = divByChars;
        this.countFiles = countFiles;
        this.inputFile = inputFile;
    }

    public ParamsCLI(String[] args){
        /*for (String arg: args)
            System.out.println(arg.toString());*/
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj.getClass() != this.getClass())
            return false;

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
                ", inputFile='" + inputFile + '\'' +
                '}';
    }
}
