package net.prokhyon.modularfuzzy.common;

import java.io.File;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.DefaultModelLoaderInfo;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;

public interface CommonServices {

	Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules();

	void registerView(FxModulesViewInfo moduleInfo);

	void registerModelTypeInStore(WorkspaceInfo storeInfo);

	void registerDefaultModelLoader(DefaultModelLoaderInfo defaultModelLoaderInfo);

	Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> getRegisteredStores();

	// TODO this should not be a common service, only ShellLayoutController should use this method
	List<DefaultModelLoaderInfo> gerRegitsteredDefaultModelLoaders();

	void saveModelByModule(ObservableList<? extends WorkspaceElement> modelList, WorkspaceInfo modelInformation);

	<T extends WorkspaceElement> void addModelToRegisteredStore(T model);

	<T extends WorkspaceElement> void updateModelInRegisteredStore(T original, T model);

	void loadFiles(List<File> filesToLoad);

	List<FuzzyDescriptorBase> loadFilesIntoDescriptorsAndFilterByPersistableModel(List<File> filesToLoad, PersistableModelInfo persistableModelInfo);

	void loadDescriptorsIntoWorkspaceElementsByPersistableModel(List<FuzzyDescriptorBase> descriptorsToLoad, PersistableModelInfo persistableModelInfo);
}
