package net.prokhyon.modularfuzzy.common;

import java.util.Map;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;

public interface CommonServices {

	Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules();

	void registerView(FxModulesViewInfo moduleInfo);

	void registerModelTypeInStore(WorkspaceInfo storeInfo);

	<T extends WorkspaceElement> void addModelToRegisteredStore(T model);

	<T extends WorkspaceElement> void updateModelInRegisteredStore(T original, T model);

	void registerPersistenceMethod(PersistableModelInfo information);

}
