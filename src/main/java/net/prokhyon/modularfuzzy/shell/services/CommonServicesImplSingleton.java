package net.prokhyon.modularfuzzy.shell.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.common.errors.MultipleExceptionWrapper;
import net.prokhyon.modularfuzzy.common.errors.NotConvertibleDescriptorException;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.DefaultModelLoaderInfo;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;
import net.prokhyon.modularfuzzy.shell.ShellApp;
import net.prokhyon.modularfuzzy.shell.util.FxDialogHelper;
import net.prokhyon.modularfuzzy.shell.view.SharedWorkspaceControlAndController;
import net.prokhyon.modularfuzzy.shell.view.ShellLayoutController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.util.*;

public class CommonServicesImplSingleton implements CommonServices, ShellServices, ShellDialogServices {

	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<>();
	private Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = new HashMap<>();
	private List<FxModulesViewInfo> registeredViews = new ArrayList<>();
	private List<DefaultModelLoaderInfo> registeredDefaultModelLoaders = new ArrayList<>();
	private Stage stage;


	private static class SingletonHolder {
		private static final CommonServicesImplSingleton INSTANCE = new CommonServicesImplSingleton();
	}

	public static CommonServicesImplSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private CommonServicesImplSingleton() {
		registerModules();
	}

	/*
		Methods
	 */

	private void registerModules() {

		pseudoModules.put(FuzzySetModuleDescriptor.class, new FuzzySetModuleDescriptor());
		pseudoModules.put(FuzzyAutomatonModuleDescriptor.class, new FuzzyAutomatonModuleDescriptor());
		pseudoModules.put(FuzzySignatureModuleDescriptor.class, new FuzzySignatureModuleDescriptor());
		pseudoModules.put(PathValuesModuleDescriptor.class, new PathValuesModuleDescriptor());
	}

	/*
		Implementing interface: ShellServices
	 */

	public void initializeModules() {

		for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> md : pseudoModules.entrySet()) {
			md.getValue().initializeModule();
		}
	}

	public List<FxModulesViewInfo> getRegisteredViews() {
		return Collections.unmodifiableList(registeredViews);
	}

	/*
		Implementing interface: CommonServices
	 */

	@Override
	public Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules() {

		return Collections.unmodifiableMap(pseudoModules);
	}

	@Override
	public void registerView(FxModulesViewInfo moduleInfo) {

		registeredViews.add(moduleInfo);
	}

	@Override
	public void registerModelTypeInStore(WorkspaceInfo storeInfo) {

		registeredStores.put(storeInfo, FXCollections.observableArrayList());
	}

	@Override
	public void registerDefaultModelLoader(DefaultModelLoaderInfo defaultModelLoaderInfo) {

		registeredDefaultModelLoaders.add(defaultModelLoaderInfo);
	}

	public Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> getRegisteredStores() {
		return Collections.unmodifiableMap(registeredStores);
	}

	@Override
	public List<DefaultModelLoaderInfo> gerRegitsteredDefaultModelLoaders() {

		return registeredDefaultModelLoaders;
	}

	@Override
	public void saveModelByModule(ObservableList<? extends WorkspaceElement> modelList, WorkspaceInfo modelInformation) {

		// TODO Export model according to the module what registered this type (should be extended later)
		final PersistableModelInfo persistableModelInfo = modelInformation.getPersistableModelInfo();
		try {
			IPersistableModel persistableModel = persistableModelInfo.getPersistableModel();
			persistableModel.exportModel(modelList);
		} catch (RuntimeException e) {
			throw new ModuleImplementationException("Not correctly implemented IPersistableModel interface in module.", e);
		}
	}

	@Override
	public <T extends WorkspaceElement> void addModelToRegisteredStore(T model) {

		Class<? extends WorkspaceElement> modelClass = model.getClass();
		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			WorkspaceInfo key = entry.getKey();
			if (key.getPersistableModelInfo().getFxModel().equals(modelClass)) {

				ObservableList list = entry.getValue();
				if (!list.contains(model))
					list.add(model);
			}
		}
	}

	@Override
	public <T extends WorkspaceElement> void updateModelInRegisteredStore(T original, T model) {

		Class<? extends WorkspaceElement> modelClass = model.getClass();
		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			WorkspaceInfo key = entry.getKey();
			if (key.getPersistableModelInfo().getFxModel().equals(modelClass)) {

				ObservableList list = entry.getValue();
				if (list.contains(original)) {
					int index = list.indexOf(original);
					list.set(index, model);
				}
			}
		}
	}

	private void loadFile(PersistableModelInfo persistableModelInfo, File file)
			throws ModuleImplementationException, NotParsableDescriptorException, NotConvertibleDescriptorException, IllegalAccessException, InstantiationException {

			final IPersistableModel persistableModel = persistableModelInfo.getPersistableModel();
			final Class<? extends ConvertibleDescriptor2FxModel.External> descriptor2FxModelConverterExternal = persistableModelInfo.getDescriptor2FxModelConverterExternal();
			final Class<? extends ConvertibleDescriptor2FxModel.Internal> descriptor2FxModelConverterInternal = persistableModelInfo.getDescriptor2FxModelConverterInternal();
			final Class<? extends FuzzyDescriptorRootBase> descriptorRootModel = persistableModelInfo.getDescriptorRootModel();
			final Class<? extends WorkspaceElement> fxModel = persistableModelInfo.getFxModel();
			final List<Class<? extends FuzzyDescriptorBase>> descriptorModels = persistableModelInfo.getDescriptorModels();

			if (FuzzyDescriptorRootBase.class.isAssignableFrom(descriptorRootModel)) {

				FuzzyDescriptorRootBase fuzzyDescriptorRootBase = null;
				if (persistableModel != null) {
					fuzzyDescriptorRootBase = persistableModel.importModel(file, descriptorRootModel, descriptorModels);

					if (fuzzyDescriptorRootBase == null)
						throw new NotParsableDescriptorException("Error has occurred while importing file with: " + persistableModel.getClass().toString());

					ConvertibleDescriptor2FxModel.External convertibleDescriptor2FxModelExternal = null;
					ConvertibleDescriptor2FxModel.Internal convertibleDescriptor2FxModelInternal = null;

					try {
						convertibleDescriptor2FxModelExternal = descriptor2FxModelConverterExternal.newInstance();
					} catch (NullPointerException e){}
					try {
						convertibleDescriptor2FxModelInternal = descriptor2FxModelConverterInternal.newInstance();
					} catch (NullPointerException e){}

					if (convertibleDescriptor2FxModelExternal == null && convertibleDescriptor2FxModelInternal == null)
						throw new ModuleImplementationException("Error has occurred while searching for converter in module: " + persistableModel.getClass().toString());

					FuzzyFxBase fuzzyFxBase = null;
					if (convertibleDescriptor2FxModelExternal != null){
						fuzzyFxBase = convertibleDescriptor2FxModelExternal.convert2FxModel(fuzzyDescriptorRootBase);
					} else if (convertibleDescriptor2FxModelInternal != null){
						// TODO implement Internal conversion
						// throw new NotImplementedException();
					}

					if (fuzzyFxBase != null){
						addModelToRegisteredStore(fxModel.cast(fuzzyFxBase));
					} else {
						throw new NotConvertibleDescriptorException("Error has occurred while converting descriptor to model in module: " + persistableModel.getClass().toString());
					}
				}

			}
	}

	private boolean loadFileByStoreConfigurationOfTheActiveTab(PersistableModelInfo persistableModelInfo, File file){

		try {
			loadFile(persistableModelInfo, file);
			return true;
		} catch (Exception e){
			return false;
		}
	}

	private boolean loadFileByStoreConfigurationsOfAllRegisteredStores(File file)
			throws MultipleExceptionWrapper {

		StringBuilder exceptioMessages = new StringBuilder();
		List<Exception> exceptions = new ArrayList<>();

		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores.entrySet()) {
			final PersistableModelInfo persistableModelInfo = entry.getKey().getPersistableModelInfo();
			try {
				loadFile(persistableModelInfo, file);
				return true;
			} catch (Exception e){
				exceptions.add(e);
				exceptioMessages.append("\n" + e.getMessage());
			}
		}
		throw new MultipleExceptionWrapper(exceptions, exceptioMessages.toString());
	}

	@Override
	public void loadFiles(List<File> filesToLoad)
			throws ModuleImplementationException, NotParsableDescriptorException, NotConvertibleDescriptorException {

		// Getting the PersistableModelInfo from the active tab
		final ShellLayoutController shellLayoutController = ShellApp.getShellLayoutController();
		final SharedWorkspaceControlAndController sharedWorkspaceControlAndControllerOfActiveTab = shellLayoutController.getSharedWorkspaceControlAndControllerOfActiveTab();
		final WorkspaceInfo workspaceInfo = sharedWorkspaceControlAndControllerOfActiveTab.getWorkspaceInfo();
		final PersistableModelInfo persistableModelInfo = workspaceInfo.getPersistableModelInfo();

		// Trying to load input files with opened tab's model
		// if that fails, tries to open with models of all registered models
		// if that fails, shows the warning windows
		for (File file : filesToLoad) {

			if (loadFileByStoreConfigurationOfTheActiveTab(persistableModelInfo, file) == true)
				continue;

			try {
				loadFileByStoreConfigurationsOfAllRegisteredStores(file);
			} catch (MultipleExceptionWrapper mew){
				this.informErrorWithStacktraceDialog(mew, "Errors, during the file import",
						"Error has occurred while importing file.",
						"Something went wrong.");
			}
		}
	}

	@Override
	public List<FuzzyDescriptorBase> loadFilesIntoDescriptorsAndFilterByPersistableModel(List<File> filesToLoad, PersistableModelInfo persistableModelInfoFilter)
			throws ModuleImplementationException, NotParsableDescriptorException {

		List<FuzzyDescriptorBase> fuzzyDescriptors = new ArrayList<>();
		for (File file : filesToLoad) {
			for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores.entrySet()) {
				try {
					final PersistableModelInfo persistableModelInfo = entry.getKey().getPersistableModelInfo();

					if (! persistableModelInfoFilter.equals(persistableModelInfo))
						continue;

					final IPersistableModel persistableModel = persistableModelInfo.getPersistableModel();
					final Class<? extends FuzzyDescriptorRootBase> descriptorRootModel = persistableModelInfo.getDescriptorRootModel();
					final List<Class<? extends FuzzyDescriptorBase>> descriptorModels = persistableModelInfo.getDescriptorModels();

					if (FuzzyDescriptorRootBase.class.isAssignableFrom(descriptorRootModel)) {
						FuzzyDescriptorRootBase fuzzyDescriptorRootBase = null;
						if (persistableModel != null) {
							fuzzyDescriptorRootBase = persistableModel.importModel(file, descriptorRootModel, descriptorModels);
							fuzzyDescriptors.add(fuzzyDescriptorRootBase);
						}
					}
				} catch (NotParsableDescriptorException e){}
			}
		}
		return fuzzyDescriptors;
	}

	@Override
	public void loadDescriptorsIntoWorkspaceElementsByPersistableModel(List<FuzzyDescriptorBase> descriptorsToLoad, PersistableModelInfo persistableModelInfo)
			throws ModuleImplementationException, NotConvertibleDescriptorException{

		if (persistableModelInfo == null)
			return;

		final Class<? extends ConvertibleDescriptor2FxModel.External> descriptor2FxModelConverterExternal = persistableModelInfo.getDescriptor2FxModelConverterExternal();
		final Class<? extends ConvertibleDescriptor2FxModel.Internal> descriptor2FxModelConverterInternal = persistableModelInfo.getDescriptor2FxModelConverterInternal();
		final Class<? extends WorkspaceElement> fxModel = persistableModelInfo.getFxModel();

		ConvertibleDescriptor2FxModel.External convertibleDescriptor2FxModelExternal = null;
		ConvertibleDescriptor2FxModel.Internal convertibleDescriptor2FxModelInternal = null;

		for (FuzzyDescriptorBase fuzzyDescriptorBase : descriptorsToLoad) {

			try {
				convertibleDescriptor2FxModelExternal = descriptor2FxModelConverterExternal.newInstance();
			} catch (NullPointerException | IllegalAccessException | InstantiationException e){}
			try {
				convertibleDescriptor2FxModelInternal = descriptor2FxModelConverterInternal.newInstance();
			} catch (NullPointerException | IllegalAccessException | InstantiationException e){}

			FuzzyFxBase fuzzyFxBase;
			if (convertibleDescriptor2FxModelExternal != null){
				fuzzyFxBase = convertibleDescriptor2FxModelExternal.convert2FxModel(fuzzyDescriptorBase);
			} else if (convertibleDescriptor2FxModelInternal != null){
				fuzzyFxBase = ((ConvertibleDescriptor2FxModel.Internal) fuzzyDescriptorBase).convert2FxModel();
			} else
				throw new NotConvertibleDescriptorException("Error has occurred while searching for converter in module: " + persistableModelInfo.getClass().toString());

			if (fuzzyFxBase != null){
				addModelToRegisteredStore(fxModel.cast(fuzzyFxBase));
			} else {
				throw new ModuleImplementationException();
			}
		}
	}

	@Override
	public <T extends WorkspaceElement> T resolveModelByUUID(String uuid) {

		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> workspaceInfoObservableListEntry : registeredStores.entrySet()) {
			final ObservableList<? extends WorkspaceElement> value = workspaceInfoObservableListEntry.getValue();
			for (WorkspaceElement workspaceElement : value) {
				final String workspaceElementUUID = workspaceElement.getUuid();
				if (workspaceElementUUID.equals(uuid))
					return (T) workspaceElement;
			}
		}
		return null;
	}

	/*
		Implementing interface: ShellDialogServices
	 */

	@Override
	public void informWarningDialog(String title, String header, String content) {

		FxDialogHelper.informWarningDialog(title, header, content);
	}

	@Override
	public void informErrorWithStacktraceDialog(Exception ex, String title, String header, String content) {

		FxDialogHelper.informErrorWithStacktraceDialog(ex, title, header, content);
	}

	@Override
	public File saveFilesIntoDirectoryDialog() {

		return FxDialogHelper.saveFilesIntoDirectoryDialog(stage);
	}

	@Override
	public File saveFileDialog(String initialFileName, String ... extensions) {

		return FxDialogHelper.saveFileDialog(stage, initialFileName, extensions);
	}

	@Override
	public int selectFromOptions(String title, String headed, String content, String... choices) {

		return FxDialogHelper.selectFromActions(title, headed, content, choices);
	}

	@Override
	public List<File> openFilesDialog() {

		return FxDialogHelper.selectFilesDialog(stage);
	}

	/*
		Getters and Setters
	 */

	public void setShellStage(Stage stage) {

		this.stage = stage;
	}

}
