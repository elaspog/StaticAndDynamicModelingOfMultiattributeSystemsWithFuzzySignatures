package net.prokhyon.modularfuzzy.common;

public interface CommonServices {

	void registerView(FxModulesViewInformationGroup moduleInfo);

	<T extends WorkspaceElement> void registerModelTypeInStore(WorkspaceInformationGroup storeInfo);

	<T extends WorkspaceElement> void addModelStore(T model);

}
