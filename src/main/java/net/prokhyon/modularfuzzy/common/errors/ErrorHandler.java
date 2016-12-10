package net.prokhyon.modularfuzzy.common.errors;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ErrorHandler {

    public static String getErrorStacktrace(Exception exception){

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        exception.printStackTrace(printWriter);
        String s = writer.toString();
        return s;
    }

}
