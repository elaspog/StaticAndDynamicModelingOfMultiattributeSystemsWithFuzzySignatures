package net.prokhyon.modularfuzzy.common.errors;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MultipleExceptionWrapper extends Exception {

    private List<Exception> exceptions = new ArrayList<>();

    public MultipleExceptionWrapper(List<Exception> exceptions, String exeptionMessages){
        super(exeptionMessages);
        this.exceptions = exceptions;
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        super.printStackTrace(s);
        for(Exception exception : exceptions) {
            s.println("\n>");
            exception.printStackTrace(s);
        }
    }

}
