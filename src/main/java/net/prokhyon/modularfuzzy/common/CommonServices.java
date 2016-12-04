package net.prokhyon.modularfuzzy.common;

import java.util.Map;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;

public interface CommonServices {

	Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules();

	void registerView(FxModulesViewInfo moduleInfo);

	void registerModelTypeInStore(WorkspaceInfo storeInfo);

	<T extends WorkspaceElement> void addModelStore(T model);

	void registerPersistenceMethod(PersistableModelInfo information);

}
