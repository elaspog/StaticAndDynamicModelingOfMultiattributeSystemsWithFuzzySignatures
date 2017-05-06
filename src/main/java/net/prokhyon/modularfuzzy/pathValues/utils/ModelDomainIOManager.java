package net.prokhyon.modularfuzzy.pathValues.utils;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;

import java.io.File;
import java.util.List;

public class ModelDomainIOManager implements IPersistableModel {

    @Override
    public <T extends DescriptorRootBase>
    T importModel(File file,
                  Class<? extends DescriptorRootBase> descriptorRootModel,
                  List<Class<? extends DescriptorBase>> descriptorModels)
            throws NotParsableDescriptorException {

        return null;
    }

    @Override
    public <T extends WorkspaceFxRootElementBase> void exportModel(List<T> models) {

    }

}
