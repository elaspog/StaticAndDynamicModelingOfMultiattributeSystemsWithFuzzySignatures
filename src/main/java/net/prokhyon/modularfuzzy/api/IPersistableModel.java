package net.prokhyon.modularfuzzy.api;

import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

import java.io.File;
import java.util.List;

public interface IPersistableModel {

    abstract public <T extends FuzzyDescriptorRootBase>
    T importModel(File file,
                  Class<? extends FuzzyDescriptorRootBase> descriptorRootModel,
                  List<Class<? extends FuzzyDescriptorBase>> descriptorModels)
            throws NotParsableDescriptorException;

    abstract public <T extends WorkspaceElement> void exportModel(List<T> models);
}
