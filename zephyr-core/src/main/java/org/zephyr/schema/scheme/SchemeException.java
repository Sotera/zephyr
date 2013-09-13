package org.zephyr.schema.scheme;

public class SchemeException extends Exception {

    private static final long serialVersionUID = -8925484364094186846L;

    public SchemeException() {
        super();
    }

    public SchemeException(String message) {
        super(message);
    }

    public SchemeException(Throwable cause) {
        super(cause);
    }

    public SchemeException(String message, Throwable cause) {
        super(message, cause);
    }

}
