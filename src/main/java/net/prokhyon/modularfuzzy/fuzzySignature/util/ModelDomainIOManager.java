package net.prokhyon.modularfuzzy.fuzzySignature.util;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.utils.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    private ShellDialogServices shellDialogServices = new ServiceFactory().getShellDialogServices();


    @Override
    public <T extends DescriptorRootBase>
    T importModel(File file,
                  Class<? extends DescriptorRootBase> descriptorRootModel,
                  List<Class<? extends DescriptorBase>> descriptorModels)
            throws NotParsableDescriptorException {

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        try {
            return descriptorHandler.readFromXmlFile(file, descriptorRootModel, descriptorModels);
        } catch (NotParsableDescriptorException nce){}
        try {
            return descriptorHandler.readFromJsonFile(file, descriptorRootModel, descriptorModels);
        } catch (NotParsableDescriptorException nce){}
        throw new NotParsableDescriptorException("Error has occurred while importing a FuzzySignature file: " + file.getAbsolutePath());
    }

    @Override
    public <T extends WorkspaceFxRootElementBase> void exportModel(List<T> models) {

        if (models.size() < 1) {
            shellDialogServices.informWarningDialog("Export", "", "There was no file selected");
        } else if (models.size() > 1) {
            saveMultipleFiles(models);
        } else if (models.size() == 1) {
            saveOneFile(models.get(0));
        }
    }

    private <T extends WorkspaceFxRootElementBase> void saveMultipleFiles(List<T> models) {

        File selectedDirectory = shellDialogServices.saveFilesIntoDirectoryDialog();
        if (selectedDirectory == null)
            return;
        String dirPath = CommonUtils.getCanonicalPathFromFile(selectedDirectory);

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        for (T m : models) {
            try {

                final net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature fxFuzzySignature = (net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature) m;
                final net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature descriptorFuzzySignature = fxFuzzySignature.convert2DescriptorModel();
                String newFileName = fxFuzzySignature.getFuzzySignatureName() + "_" + fxFuzzySignature.getUuid() + ".xml";

                Path path = Paths.get(dirPath, newFileName);
                descriptorHandler.saveToXmlFile(path.toString(), descriptorHandler.generateXmlStringFromModel(descriptorFuzzySignature));

            } catch (Exception e){
                shellDialogServices.informErrorWithStacktraceDialog(e,
                        "Model export error",
                        "Error occurred while exporting",
                        "Something went wrong while exporting");
            }
        }
    }

    private <T extends WorkspaceFxRootElementBase> void saveOneFile(T model) {

        try {

            final net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature fxFuzzySignature = (net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature) model;
            final net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature descriptorFuzzySignature = fxFuzzySignature.convert2DescriptorModel();

            String initialName = fxFuzzySignature.getFuzzySignatureName() + "_" + fxFuzzySignature.getUuid();
            File selectedFile = shellDialogServices.saveFileDialog(initialName, "xml", "json");
            if (selectedFile == null)
                return;
            String filePath  = CommonUtils.getCanonicalPathFromFile(selectedFile);
            DescriptorHandler descriptorHandler = new DescriptorHandler();

            if (filePath.endsWith("xml")) {
                descriptorHandler.saveToXmlFile(filePath, descriptorHandler.generateXmlStringFromModel(descriptorFuzzySignature));
            } else {
                descriptorHandler.saveToTextFile(filePath, descriptorHandler.generateJsonStringFromModel(descriptorFuzzySignature));
            }

        } catch (Exception e){
            shellDialogServices.informErrorWithStacktraceDialog(e,
                    "Model export error",
                    "Error occurred while exporting",
                    "Something went wrong while exporting");
        }
    }

}
