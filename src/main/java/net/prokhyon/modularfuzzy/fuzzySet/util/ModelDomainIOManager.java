package net.prokhyon.modularfuzzy.fuzzySet.util;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.descriptor.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.fuzzySet.model.ModelConverter;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellServices;

import java.io.File;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    private ShellServices shellServices = new ServiceFactory().getShellServices();

    @Override
    public <T extends FuzzyDescriptorRootBase> List<T> importModel() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Model File(s)");
        //FileChooser.ExtensionFilter extFilterXml = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        //FileChooser.ExtensionFilter extFilterJson = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        //fileChooser.getExtensionFilters().add(extFilterXml);
        //fileChooser.getExtensionFilters().add(extFilterJson);
        File file = fileChooser.showSaveDialog(shellServices.getShellStage());

        return null;
    }

    @Override
    public <T extends WorkspaceElement> void exportModel(List<T> models) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(shellServices.getShellStage());

        DescriptorHandler descriptorHandler = new DescriptorHandler();
        for (T m : models) {
            try {
                final net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem model = (net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem) m;
                final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem fuzzySetSystem = ModelConverter.fxmodelToDescriptor(model);

                String xml = descriptorHandler.getXstream().toXML(fuzzySetSystem);
                System.out.println(descriptorHandler.formatXml(xml));

            } catch (Exception e){}
        }
    }

}
