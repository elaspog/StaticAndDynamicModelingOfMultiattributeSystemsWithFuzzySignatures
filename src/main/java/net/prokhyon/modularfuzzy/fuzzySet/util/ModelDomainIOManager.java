package net.prokhyon.modularfuzzy.fuzzySet.util;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    private ShellDialogServices shellDialogServices = new ServiceFactory().getShellDialogServices();


    @Override
    public <T extends FuzzyDescriptorRootBase>
    T importModel(File file,
                  Class<? extends FuzzyDescriptorRootBase> descriptorRootModel,
                  List<Class<? extends FuzzyDescriptorBase>> descriptorModels)
            throws NotParsableDescriptorException {

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        try {
            return descriptorHandler.readFromXmlFile(file, descriptorRootModel, descriptorModels);
        } catch (NotParsableDescriptorException nce){}
        try {
            return descriptorHandler.readFromJsonFile(file, descriptorRootModel, descriptorModels);
        } catch (NotParsableDescriptorException nce){}
        throw new NotParsableDescriptorException("Error has occurred while importing a FuzzySet file: " + file.getAbsolutePath());
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
        String dirPath = CommonUtils.getCanonicalPathFromFile(selectedDirectory);

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        for (T m : models) {
            try {

                final net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem fxFuzzySetSystem = (net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem) m;
                final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem descriptorFuzzySetSystem = fxFuzzySetSystem.convert2DescriptorModel();
                String newFileName = fxFuzzySetSystem.getFuzzySystemName() + "_" + fxFuzzySetSystem.getUuid() + ".xml";

                Path path = Paths.get(dirPath, newFileName);
                descriptorHandler.saveToXmlFile(path.toString(), descriptorHandler.generateXmlStringFromModel(descriptorFuzzySetSystem));

            } catch (Exception e){
                shellDialogServices.informErrorWithStacktraceDialog(e,
                        "Model export error",
                        "Error occurred while exporting",
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
            String filePath  = CommonUtils.getCanonicalPathFromFile(selectedFile);
            DescriptorHandler descriptorHandler = new DescriptorHandler();

            if (filePath.endsWith("xml")) {
                descriptorHandler.saveToXmlFile(filePath, descriptorHandler.generateXmlStringFromModel(descriptorFuzzySetSystem));
            } else {
                descriptorHandler.saveToTextFile(filePath, descriptorHandler.generateJsonStringFromModel(descriptorFuzzySetSystem));
            }

        } catch (Exception e){
            shellDialogServices.informErrorWithStacktraceDialog(e,
                    "Model export error",
                    "Error occurred while exporting",
                    "Something went wrong while exporting");
        }
    }

}
