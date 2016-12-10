package net.prokhyon.modularfuzzy.common.modules;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

import java.util.ArrayList;
import java.util.List;

public class PersistableModelInfo {

    IPersistableModel persistableModel;
    Class<? extends ConvertibleDescriptor2FxModel.External> descriptor2FxModelConverterExternal;
    Class<? extends ConvertibleDescriptor2FxModel.Internal> descriptor2FxModelConverterInternal;
    Class<? extends ConvertibleFxModel2Descriptor.External> fxModel2DescriptorConverterExternal;
    Class<? extends ConvertibleFxModel2Descriptor.Internal> fxModel2DescriptorConverterInternal;
    Class<? extends WorkspaceElement> fxModel;
    Class<? extends FuzzyDescriptorRootBase> descriptorRootModel;
    List<Class<? extends FuzzyDescriptorBase>> descriptorModels;

    public PersistableModelInfo(IPersistableModel persistableModel,
                                Class<? extends ConvertibleDescriptor2FxModel.External> descriptor2FxModelConverterExternal,
                                Class<? extends ConvertibleDescriptor2FxModel.Internal> descriptor2FxModelConverterInternal,
                                Class<? extends ConvertibleFxModel2Descriptor.External> fxModel2DescriptorConverterExternal,
                                Class<? extends ConvertibleFxModel2Descriptor.Internal> fxModel2DescriptorConverterInternal,
                                Class<? extends WorkspaceElement> fxModel,
                                Class<? extends FuzzyDescriptorRootBase> descriptorRootModel,
                                Class<? extends FuzzyDescriptorBase> ... descriptorModels) {
        this.persistableModel = persistableModel;
        this.descriptor2FxModelConverterExternal = descriptor2FxModelConverterExternal;
        this.descriptor2FxModelConverterInternal = descriptor2FxModelConverterInternal;
        this.fxModel2DescriptorConverterExternal = fxModel2DescriptorConverterExternal;
        this.fxModel2DescriptorConverterInternal = fxModel2DescriptorConverterInternal;
        this.fxModel = fxModel;
        this.descriptorRootModel = descriptorRootModel;
        this.descriptorModels = new ArrayList<>();
        for (Class<? extends FuzzyDescriptorBase> dm : descriptorModels){
            this.descriptorModels.add(dm);
        }
        if (! this.descriptorModels.contains(descriptorRootModel)){
            this.descriptorModels.add(descriptorRootModel);
        }
    }

    public IPersistableModel getPersistableModel() {
        return persistableModel;
    }

    public void setPersistableModel(IPersistableModel persistableModel) {
        this.persistableModel = persistableModel;
    }

    public Class<? extends ConvertibleDescriptor2FxModel.External> getDescriptor2FxModelConverterExternal() {
        return descriptor2FxModelConverterExternal;
    }

    public void setDescriptor2FxModelConverterExternal(Class<? extends ConvertibleDescriptor2FxModel.External> descriptor2FxModelConverterExternal) {
        this.descriptor2FxModelConverterExternal = descriptor2FxModelConverterExternal;
    }

    public Class<? extends ConvertibleDescriptor2FxModel.Internal> getDescriptor2FxModelConverterInternal() {
        return descriptor2FxModelConverterInternal;
    }

    public void setDescriptor2FxModelConverterInternal(Class<? extends ConvertibleDescriptor2FxModel.Internal> descriptor2FxModelConverterInternal) {
        this.descriptor2FxModelConverterInternal = descriptor2FxModelConverterInternal;
    }

    public Class<? extends ConvertibleFxModel2Descriptor.External> getFxModel2DescriptorConverterExternal() {
        return fxModel2DescriptorConverterExternal;
    }

    public void setFxModel2DescriptorConverterExternal(Class<? extends ConvertibleFxModel2Descriptor.External> fxModel2DescriptorConverterExternal) {
        this.fxModel2DescriptorConverterExternal = fxModel2DescriptorConverterExternal;
    }

    public Class<? extends ConvertibleFxModel2Descriptor.Internal> getFxModel2DescriptorConverterInternal() {
        return fxModel2DescriptorConverterInternal;
    }

    public void setFxModel2DescriptorConverterInternal(Class<? extends ConvertibleFxModel2Descriptor.Internal> fxModel2DescriptorConverterInternal) {
        this.fxModel2DescriptorConverterInternal = fxModel2DescriptorConverterInternal;
    }

    public Class<? extends WorkspaceElement> getFxModel() {
        return fxModel;
    }

    public void setFxModel(Class<? extends WorkspaceElement> fxModel) {
        this.fxModel = fxModel;
    }

    public Class<? extends FuzzyDescriptorRootBase> getDescriptorRootModel() {
        return descriptorRootModel;
    }

    public void setDescriptorRootModel(Class<? extends FuzzyDescriptorRootBase> descriptorRootModel) {
        this.descriptorRootModel = descriptorRootModel;
    }

    public List<Class<? extends FuzzyDescriptorBase>> getDescriptorModels() {
        return descriptorModels;
    }

    public void setDescriptorModels(List<Class<? extends FuzzyDescriptorBase>> descriptorModels) {
        this.descriptorModels = descriptorModels;
    }
}
