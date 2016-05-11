package net.prokhyon.modularfuzzy.common;

import java.util.Map;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;

public interface CommonServices {

	Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules();

	void registerView(FxModulesViewInformationGroup moduleInfo);

	<T extends WorkspaceElement> void registerModelTypeInStore(WorkspaceInformationGroup storeInfo);

	<T extends WorkspaceElement> void addModelStore(T model);

}
