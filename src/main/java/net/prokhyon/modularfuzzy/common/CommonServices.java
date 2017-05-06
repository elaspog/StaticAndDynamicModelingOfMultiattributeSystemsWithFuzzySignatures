package net.prokhyon.modularfuzzy.common;

import java.io.File;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.common.errors.NotConvertibleDescriptorException;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.modules.DefaultModelLoaderInfo;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;

public interface CommonServices {

	Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules();

	void registerView(FxModulesViewInfo moduleInfo);

	void registerModelTypeInStore(WorkspaceInfo storeInfo);

	void registerDefaultModelLoader(DefaultModelLoaderInfo defaultModelLoaderInfo);

	Map<WorkspaceInfo, ObservableList<? extends WorkspaceFxRootElementBase>> getRegisteredStores();

	// TODO this should not be a common service, only ShellLayoutController should use this method
	List<DefaultModelLoaderInfo> gerRegitsteredDefaultModelLoaders();

	void saveModelByModule(ObservableList<? extends WorkspaceFxRootElementBase> modelList, WorkspaceInfo modelInformation);

	<T extends WorkspaceFxRootElementBase> void addModelToRegisteredStore(T model);

	<T extends WorkspaceFxRootElementBase> void updateModelInRegisteredStore(T original, T model);

	void loadFiles(List<File> filesToLoad)
			throws ModuleImplementationException, NotParsableDescriptorException, NotConvertibleDescriptorException;

	List<DescriptorBase> loadFilesIntoDescriptorsAndFilterByPersistableModel(List<File> filesToLoad, PersistableModelInfo persistableModelInfo)
			throws ModuleImplementationException, NotParsableDescriptorException;

	void loadDescriptorsIntoWorkspaceElementsByPersistableModel(List<DescriptorBase> descriptorsToLoad, PersistableModelInfo persistableModelInfo)
			throws ModuleImplementationException, NotConvertibleDescriptorException;

	<T extends WorkspaceFxRootElementBase> T resolveModelByUUID(String uuid);
}
