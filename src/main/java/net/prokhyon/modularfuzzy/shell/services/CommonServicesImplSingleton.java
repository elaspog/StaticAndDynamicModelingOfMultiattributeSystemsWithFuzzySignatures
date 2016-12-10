package net.prokhyon.modularfuzzy.shell.services;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.*;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.errors.ErrorHandler;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.common.errors.NotConvertibleException;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.model.ModelConverter;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;
import net.prokhyon.modularfuzzy.shell.util.FxDialogHelper;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class CommonServicesImplSingleton implements CommonServices, ShellServices, ShellDialogServices {

	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<>();
	private List<FxModulesViewInfo> registeredViews = new ArrayList<>();
	private Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = new HashMap<>();
	private List<PersistableModelInfo> registeredPersistenceMethods = new ArrayList<>();
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

	public Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> getRegisteredStores() {
		return Collections.unmodifiableMap(registeredStores);
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

	@Override
	public void registerPersistenceMethod(PersistableModelInfo information) {

		registeredPersistenceMethods.add(information);
	}

	@Override
	public void loadFiles(List<File> filesToLoad) {

		for (File file : filesToLoad) {
			for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores
					.entrySet()) {
				try {
					final PersistableModelInfo persistableModelInfo = entry.getKey().getPersistableModelInfo();
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

							ConvertibleDescriptor2FxModel.External convertibleDescriptor2FxModelExternal = null;
							ConvertibleDescriptor2FxModel.Internal convertibleDescriptor2FxModelInternal = null;

							try {
								convertibleDescriptor2FxModelExternal = descriptor2FxModelConverterExternal.newInstance();
							} catch (NullPointerException e){}
							try {
								convertibleDescriptor2FxModelInternal = descriptor2FxModelConverterInternal.newInstance();
							} catch (NullPointerException e){}

							FuzzyFxBase fuzzyFxBase;
							if (convertibleDescriptor2FxModelExternal != null){
								fuzzyFxBase = convertibleDescriptor2FxModelExternal.convert2FxModel(fuzzyDescriptorRootBase);
							} else if (convertibleDescriptor2FxModelInternal != null){
								// TODO implement Internal conversion
								throw new NotImplementedException();
							} else
								throw new NotConvertibleException();

							if (fuzzyFxBase != null){
								addModelToRegisteredStore(fxModel.cast(fuzzyFxBase));
							} else {
								throw new ModuleImplementationException();
							}
						}

					}

				} catch (ModuleImplementationException e){
					this.informErrorWithStacktraceDialog(e, "Module error",
							"Error has occurred while importing file.",
							"Error in a module implementation.");
				} catch (NotConvertibleException e){
					this.informErrorWithStacktraceDialog(e, "Conversion error",
							"Error has occurred while importing file.",
							"The selected file is not convertible to internal representation of the model.");
				} catch (IllegalAccessException | InstantiationException e) {
					this.informErrorWithStacktraceDialog(e, "Unknown exception",
							"Error has occurred while importing file.",
							"The reason of this exception is unknown.");
				}
			}
		}
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
