package net.prokhyon.modularfuzzy.common;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorRootBase;

public class PersistableModelTuple {

    IPersistableModel persistableModel;
    Class<? extends WorkspaceElement> fxModel;
    Class<? extends FuzzyDescriptorRootBase> descriptorModel;

    public PersistableModelTuple(IPersistableModel persistableModel, Class<? extends WorkspaceElement> fxModel, Class<? extends FuzzyDescriptorRootBase> descriptorModel) {
        this.persistableModel = persistableModel;
        this.fxModel = fxModel;
        this.descriptorModel = descriptorModel;
    }

    public IPersistableModel getPersistableModel() {
        return persistableModel;
    }

    public Class<? extends WorkspaceElement> getFxModel() {
        return fxModel;
    }

    public Class<? extends FuzzyDescriptorRootBase> getDescriptorModel() {
        return descriptorModel;
    }

    public void setPersistableModel(IPersistableModel persistableModel) {
        this.persistableModel = persistableModel;
    }

    public void setFxModel(Class<? extends WorkspaceElement> fxModel) {
        this.fxModel = fxModel;
    }

    public void setDescriptorModel(Class<? extends FuzzyDescriptorRootBase> descriptorModel) {
        this.descriptorModel = descriptorModel;
    }

}
