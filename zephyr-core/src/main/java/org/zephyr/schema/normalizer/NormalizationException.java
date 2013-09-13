package org.zephyr.schema.normalizer;

public class NormalizationException extends Exception {

    private static final long serialVersionUID = -8925484364094186846L;

    public NormalizationException() {
        super();
    }

    public NormalizationException(String message) {
        super(message);
    }

    public NormalizationException(Throwable cause) {
        super(cause);
    }

    public NormalizationException(String message, Throwable cause) {
        super(message, cause);
    }

}
