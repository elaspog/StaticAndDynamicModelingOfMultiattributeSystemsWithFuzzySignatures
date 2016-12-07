package net.prokhyon.modularfuzzy.shell.util;

import javafx.stage.FileChooser;
import java.util.ArrayList;
import java.util.List;

public enum ExtensionHelper {

    TXT("*.txt", "TXT files (*.txt)"),
    XML("*.xml", "XML files (*.xml)"),
    ZIP("*.zip", "ZIP files (*.zip)"),
    BIN("*.bin", "BIN files (*.bin)"),
    JSON("*.json", "JSON files (*.json)");

    private String extension;
    private String description;

    ExtensionHelper(String extension, String description){
        this.extension = extension;
        this.description = description;
    }

    public static List<ExtensionHelper> createExtensions(String ... extensions){

        List<ExtensionHelper> extensionsToAdd = new ArrayList<>();
        for(String extension : extensions){

            if (extension.toLowerCase().equals("xml"))
                extensionsToAdd.add(ExtensionHelper.XML);
            else if (extension.toLowerCase().equals("bin"))
                extensionsToAdd.add(ExtensionHelper.BIN);
            else if (extension.toLowerCase().equals("zip"))
                extensionsToAdd.add(ExtensionHelper.ZIP);
            else if (extension.toLowerCase().equals("txt"))
                extensionsToAdd.add(ExtensionHelper.TXT);
            else if (extension.toLowerCase().equals("json"))
                extensionsToAdd.add(ExtensionHelper.JSON);
        }
        return extensionsToAdd;
    }

    public static FileChooser applyExtensionsOnFileChooser(FileChooser fileChooser, String ... extensions){

        List<ExtensionHelper> refinedExtensions = createExtensions(extensions);
        return applyExtensionsOnFileChooser(fileChooser, refinedExtensions);
    }

    public static FileChooser applyExtensionsOnFileChooser(FileChooser fileChooser, List<ExtensionHelper> extensions){

        if (fileChooser != null){
            for (ExtensionHelper ext : extensions) {
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(ext.getDescription(), ext.getExtension());
                fileChooser.getExtensionFilters().add(extFilter);
            }
        }
        return fileChooser;
    }

    public String getExtension() {
        return extension;
    }

    public String getDescription() {
        return description;
    }

}