package net.prokhyon.modularfuzzy.pathValues.utils;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

import java.io.File;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    @Override
    public <T extends FuzzyDescriptorRootBase> T importModel(File file, Class<? extends FuzzyDescriptorRootBase> descriptorRootModel, List<Class<? extends FuzzyDescriptorBase>> descriptorModels) throws NotParsableDescriptorException {
        return null;
    }

    @Override
    public <T extends WorkspaceElement> void exportModel(List<T> models) {

    }

}
