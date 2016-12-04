package net.prokhyon.modularfuzzy.shell.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.*;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;

public class CommonServicesImplSingleton implements CommonServices, ShellServices {

	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<Class<? extends ModuleDescriptor>, ModuleDescriptor>();;
	private List<FxModulesViewInfo> registeredViews = new ArrayList<FxModulesViewInfo>();
	private Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = new HashMap<WorkspaceInfo, ObservableList<? extends WorkspaceElement>>();
	private List<PersistableModelInfo> registeredPersistenceMethods = new ArrayList<PersistableModelInfo>();
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

	public List<FxModulesViewInfo> getRegisteredViews() {
		return Collections.unmodifiableList(registeredViews);
	}

	public Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> getRegisteredStores() {
		return Collections.unmodifiableMap(registeredStores);
	}

	private void registerModules() {

		pseudoModules.put(FuzzySetModuleDescriptor.class, new FuzzySetModuleDescriptor());
		pseudoModules.put(FuzzyAutomatonModuleDescriptor.class, new FuzzyAutomatonModuleDescriptor());
		pseudoModules.put(FuzzySignatureModuleDescriptor.class, new FuzzySignatureModuleDescriptor());
		pseudoModules.put(PathValuesModuleDescriptor.class, new PathValuesModuleDescriptor());
	}

	public void initializeModules() {

		for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> md : pseudoModules.entrySet()) {
			md.getValue().initializeModule();
		}
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
	public Stage getShellStage() {
		return this.stage;
	}

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
	public <T extends WorkspaceElement> void addModelStore(T model) {

		Class<? extends WorkspaceElement> modelClass = model.getClass();
		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			WorkspaceInfo key = entry.getKey();
			if (key.getModelType().equals(modelClass)) {

				ObservableList value = entry.getValue();
				if (!value.contains(model))
					value.add(model);
			}
		}
	}

	@Override
	public void registerPersistenceMethod(PersistableModelInfo information) {

		registeredPersistenceMethods.add(information);
	}

	public void setShellStage(Stage stage) {
		this.stage = stage;
	}

}
