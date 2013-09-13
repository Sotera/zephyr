package org.zephyr.parser;

public class ParseException extends Exception {

    private static final long serialVersionUID = -3929307108070356088L;

    public ParseException() {
        super();
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
