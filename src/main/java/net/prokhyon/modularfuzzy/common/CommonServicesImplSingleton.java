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
import net.prokhyon.modularfuzzy.shell.ShellApp;
import net.prokhyon.modularfuzzy.shell.view.ShellLayoutController;

public class CommonServicesImplSingleton implements CommonServices {

	private ShellApp shellApp;
	private ShellLayoutController shellLayoutController;
	private Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = new HashMap<Class<? extends ModuleDescriptor>, ModuleDescriptor>();;
	private List<FxModulesViewInformationGroup> viewNameAndPaneTypePairs = new ArrayList<FxModulesViewInformationGroup>();

	private static class SingletonHolder {
		private static final CommonServicesImplSingleton INSTANCE = new CommonServicesImplSingleton();
	}

	public static CommonServicesImplSingleton getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private CommonServicesImplSingleton() {
		registerModules();
	}

	public void initMainApp(ShellApp shellApp) {
		this.shellApp = shellApp;
	}

	public void initShellLayoutController(ShellLayoutController shellLayoutController) {
		this.shellLayoutController = shellLayoutController;
	}

	public Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> getPseudoModules() {
		return pseudoModules;
	}

	public List<FxModulesViewInformationGroup> getViewNameAndPaneTypePairs() {
		return viewNameAndPaneTypePairs;
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

		viewNameAndPaneTypePairs
				.add(new FxModulesViewInformationGroup(viewName, viewRelativePath, relativeResourceClass, paneType));
	}

	@Override
	public List<ModuleDescriptor> getModuleDependencyInstances(Class<? extends ModuleDescriptor> moduleTypes) {

		return null;
	}

	@Override
	public void loadViewToContentArea(String view) {

	}

	@Override
	public void loadViewToModalWindow(String view) {

	}

	@Override
	public CommonServices getCommonServices() {

		return null;
	}

}
