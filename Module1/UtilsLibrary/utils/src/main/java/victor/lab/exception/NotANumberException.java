package victor.lab.exception;

public class NotANumberException extends Exception {

    private static final long serialVersionUID = 4378759460578675290L;

    public NotANumberException() {
        super();
    }

    public NotANumberException(String message) {
        super(message);
    }

    public NotANumberException(Exception e) {
        super(e);
    }

    public NotANumberException(String message, Exception e) {
        super(message, e);
    }
}
