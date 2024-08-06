package excelpractice.exceptions;

public class InvalidExcelFilePathException extends InvalidInputException{
    public InvalidExcelFilePathException(String errorMessage) {
        super(errorMessage);
    }
}