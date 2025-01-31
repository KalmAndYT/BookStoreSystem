package exceptions;

public class InvalidISBNException extends Exception {
    public InvalidISBNException(String message) {
        super(message);
    }
}