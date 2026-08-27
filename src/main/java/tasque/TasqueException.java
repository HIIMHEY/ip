package tasque;

/**
 * Represents an error that Tasque can report to the user.
 */
public class TasqueException extends Exception {
    public TasqueException(String errorMessage) {
        super(errorMessage);
    }
}
