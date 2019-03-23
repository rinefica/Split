package split.exceptions;

public class SplitArgumentException extends Exception {
    public static final String USE_POS_OPT = "Must be used -l, -n or -c with number > 0";
    public static final String USE_POS_COUNT = "Count div elements must be > 0";
    public static final String FILE_COUNT_LESS_701 = "Count of files must be < 701";
    public static final String FILE_NOT_EXIST = "Input file does not exist";
    public static final String FILE_IS_EMPTY = "Input file is empty";

    private String message;

    public SplitArgumentException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}