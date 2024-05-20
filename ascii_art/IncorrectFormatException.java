package ascii_art;


/**
 * Custom exception class for representing an incorrect format in a command.
 * Extends RuntimeException for unchecked exception behavior.
 */
public class IncorrectFormatException extends RuntimeException {
    /**
     * Constructs a new IncorrectFormatException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public IncorrectFormatException(String message) {
        super(message);
    }
}
