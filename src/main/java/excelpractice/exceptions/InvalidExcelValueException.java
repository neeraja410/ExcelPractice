package excelpractice.exceptions;

/**
 * New exception to handle Invalid Excel Files
 */
public class InvalidExcelValueException extends InvalidInputException{
    public InvalidExcelValueException(String errorMessage) {
        super(errorMessage);
    }
}
