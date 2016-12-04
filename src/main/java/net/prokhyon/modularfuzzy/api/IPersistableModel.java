package net.prokhyon.modularfuzzy.api;

import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorRootBase;

import java.util.List;

public interface IPersistableModel {

    abstract public <T extends FuzzyDescriptorRootBase> List<T>  importModel();

    abstract public <T extends WorkspaceElement> void exportModel(List<T> models);
}
