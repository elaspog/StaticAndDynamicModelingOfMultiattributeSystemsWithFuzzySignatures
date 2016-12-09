package net.prokhyon.modularfuzzy.fuzzySet.util;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    private ShellDialogServices shellDialogServices = new ServiceFactory().getShellDialogServices();

    @Override
    public <T extends FuzzyDescriptorRootBase> List<T> importModel() {

        return null;
    }

    @Override
    public <T extends WorkspaceElement> void exportModel(List<T> models) {

        if (models.size() < 1) {
            shellDialogServices.informWarningDialog("Export", "", "There was no file selected");
        } else if (models.size() > 1) {
            saveMultipleFiles(models);
        } else if (models.size() == 1) {
            saveOneFile(models.get(0));
        }
    }

    private <T extends WorkspaceElement> void saveMultipleFiles(List<T> models) {

        File selectedDirectory = shellDialogServices.saveFilesIntoDirectoryDialog();
        if (selectedDirectory == null)
            return;
        String dirPath = getCanonicalPathFromFile(selectedDirectory);

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        for (T m : models) {
            try {

                final net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem fxFuzzySetSystem = (net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem) m;
                final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem descriptorFuzzySetSystem = fxFuzzySetSystem.convert2DescriptorModel();
                String newFileName = fxFuzzySetSystem.getFuzzySystemName() + "_" + fxFuzzySetSystem.getUuid() + ".xml";

                Path path = Paths.get(dirPath, newFileName);
                descriptorHandler.saveToXML(path.toString(), descriptorHandler.getXml(descriptorFuzzySetSystem));

            } catch (Exception e){
                shellDialogServices.informErrorWithStacktraceDialog(e,
                        "Model export error",
                        "Error occured while exporting",
                        "Something went wrong while exporting");
            }
        }
    }

    private <T extends WorkspaceElement> void saveOneFile(T model) {

        try {

            final net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem fxFuzzySetSystem = (net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem) model;
            final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem descriptorFuzzySetSystem = fxFuzzySetSystem.convert2DescriptorModel();

            String initialName = fxFuzzySetSystem.getFuzzySystemName() + "_" + fxFuzzySetSystem.getUuid();
            File selectedFile = shellDialogServices.saveFileDialog(initialName, "xml", "json");
            if (selectedFile == null)
                return;
            String filePath  = getCanonicalPathFromFile(selectedFile);
            DescriptorHandler descriptorHandler = new DescriptorHandler();

            if (filePath.endsWith("xml")) {
                descriptorHandler.saveToXML(filePath, descriptorHandler.getXml(descriptorFuzzySetSystem));
            } else {
                descriptorHandler.saveToText(filePath, descriptorHandler.getJson(descriptorFuzzySetSystem));
            }

        } catch (Exception e){
            shellDialogServices.informErrorWithStacktraceDialog(e,
                    "Model export error",
                    "Error occured while exporting",
                    "Something went wrong while exporting");
        }
    }

    private String getCanonicalPathFromFile(File file){
        String filePath;
        try {
            filePath = file.getCanonicalPath();
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        filePath = file.getAbsolutePath();
        return filePath;
    }

}
