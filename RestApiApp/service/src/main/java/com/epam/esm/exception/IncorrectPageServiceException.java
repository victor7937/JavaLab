package com.epam.esm.exception;

public class IncorrectPageServiceException extends ServiceException {

    private static final long serialVersionUID = 3945542224861911471L;

    public IncorrectPageServiceException() {
        super();
    }

    public IncorrectPageServiceException(String message) {
        super(message);
    }

    public IncorrectPageServiceException(Exception e) {
        super(e);
    }

    public IncorrectPageServiceException(String message, Exception e) {
        super(message, e);
    }
}
