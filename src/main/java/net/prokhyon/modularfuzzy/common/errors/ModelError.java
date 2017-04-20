package net.prokhyon.modularfuzzy.common.errors;

public class ModelError extends Exception {

    public ModelError() {
        super();
    }

    public ModelError(String message) {
        super(message);
    }

    public ModelError(String message, Throwable cause) {
        super(message, cause);
    }

    public ModelError(Throwable cause) {
        super(cause);
    }

    public ModelError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
