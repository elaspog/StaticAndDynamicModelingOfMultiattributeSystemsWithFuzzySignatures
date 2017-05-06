package net.prokhyon.modularfuzzy.api;

import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorRootBase;

import java.io.File;
import java.util.List;

public interface IPersistableModel {

    abstract public <T extends DescriptorRootBase>
    T importModel(File file,
                  Class<? extends DescriptorRootBase> descriptorRootModel,
                  List<Class<? extends DescriptorBase>> descriptorModels)
            throws NotParsableDescriptorException;

    abstract public <T extends WorkspaceFxRootElementBase> void exportModel(List<T> models);
}
