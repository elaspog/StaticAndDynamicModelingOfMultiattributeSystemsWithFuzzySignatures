package net.prokhyon.modularfuzzy.shell.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.api.IPersistableModel;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.*;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

class CommonServicesImplSingleton implements CommonServices, ShellServices {

	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<Class<? extends ModuleDescriptor>, ModuleDescriptor>();;
	private List<FxModulesViewInformationGroup> registeredViews = new ArrayList<FxModulesViewInformationGroup>();
	private Map<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> registeredStores = new HashMap<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>>();
	private List<PersistableModelTuple> registeredPersistenceMethods = new ArrayList<PersistableModelTuple>();

	private static class SingletonHolder {
		private static final CommonServicesImplSingleton INSTANCE = new CommonServicesImplSingleton();
	}

	public static CommonServicesImplSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private CommonServicesImplSingleton() {
		registerModules();
	}

	public List<FxModulesViewInformationGroup> getRegisteredViews() {
		return Collections.unmodifiableList(registeredViews);
	}

	public Map<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> getRegisteredStores() {
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
	public void saveModelByModule(ObservableList<? extends WorkspaceElement> modelList, WorkspaceInformationGroup modelInformation) {

		// TODO Export model according to the module what registered this type (should be extended later)
		final PersistableModelTuple persistableModelTuple = modelInformation.getPersistableModelTuple();
		try {
			IPersistableModel persistableModel = persistableModelTuple.getPersistableModel();
			persistableModel.exportModel(modelList);
		} catch (RuntimeException e) {
			throw new ModuleImplementationException("Not correctly implemented IPersistableModel interface in module.", e);
		}
	}

	@Override
	public Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules() {

		return Collections.unmodifiableMap(pseudoModules);
	}

	@Override
	public void registerView(FxModulesViewInformationGroup moduleInfo) {

		registeredViews.add(moduleInfo);
	}

	@Override
	public void registerModelTypeInStore(WorkspaceInformationGroup storeInfo) {

		registeredStores.put(storeInfo, FXCollections.observableArrayList());
	}

	@Override
	public <T extends WorkspaceElement> void addModelStore(T model) {

		Class<? extends WorkspaceElement> modelClass = model.getClass();
		for (Map.Entry<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			WorkspaceInformationGroup key = entry.getKey();
			if (key.getModelType().equals(modelClass)) {

				ObservableList value = entry.getValue();
				if (!value.contains(model))
					value.add(model);
			}
		}
	}

	@Override
	public void registerPersistenceMethod(PersistableModelTuple information) {

		registeredPersistenceMethods.add(information);
	}

}
