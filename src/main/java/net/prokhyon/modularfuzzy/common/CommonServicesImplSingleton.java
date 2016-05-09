package net.prokhyon.modularfuzzy.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.pathValues.PathValuesModuleDescriptor;

public class CommonServicesImplSingleton implements CommonServices {

	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<Class<? extends ModuleDescriptor>, ModuleDescriptor>();;
	private List<FxModulesViewInformationGroup> registeredViews = new ArrayList<FxModulesViewInformationGroup>();

	private static class SingletonHolder {
		private static final CommonServicesImplSingleton INSTANCE = new CommonServicesImplSingleton();
	}

	public static CommonServicesImplSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private CommonServicesImplSingleton() {
		registerModules();
	}

	public Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules() {
		return pseudoModules;
	}

	public List<FxModulesViewInformationGroup> getRegisteredViews() {
		return registeredViews;
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
	public void registerView(String viewName, String viewRelativePath, Class<?> relativeResourceClass,
			Class<? extends Pane> paneType) {

		registeredViews
				.add(new FxModulesViewInformationGroup(viewName, viewRelativePath, relativeResourceClass, paneType));
	}

}
