package net.prokhyon.modularfuzzy.shell.services;

import java.io.File;

public interface ShellDialogServices {

    void informWarningDialog(String title, String header, String content);

    void informErrorWithStacktraceDialog(Exception ex, String title, String header, String content);

    File saveFilesIntoDirectoryDialog();

    File saveFileDialog(String initialFileName, String ... extensions);

    int selectFromOptions(String title, String headed, String content, String ... choices);

}
