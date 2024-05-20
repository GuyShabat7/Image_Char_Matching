package ascii_art;

/**
 * Represents an exception for illegal commands in the ASCII art program.
 */
public class IllegalCommandException extends IllegalArgumentException{
    /**
     * Constructs an IllegalCommandException with the specified detail message.
     *
     * @param message The detail message.
     */
    public IllegalCommandException(String message) {
        super(message);
    }
}
