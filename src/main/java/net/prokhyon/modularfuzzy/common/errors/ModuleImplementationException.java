package net.prokhyon.modularfuzzy.common.errors;

public class ModuleImplementationException extends RuntimeException {

    public ModuleImplementationException() {
    }

    public ModuleImplementationException(String message) {
        super(message);
    }

    public ModuleImplementationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleImplementationException(Throwable cause) {
        super(cause);
    }

    public ModuleImplementationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
