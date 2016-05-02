package net.prokhyon.modularfuzzy.fuzzySet;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.api.ModuleMainController;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Set";
	}

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Set 1", "view/Set1Layout.fxml", FuzzySetModuleDescriptor.class, StackPane.class);
		services.registerView("Fuzzy Set 2", "view/Set2Layout.fxml", FuzzySetModuleDescriptor.class, AnchorPane.class);
	}

	@Override
	public void registerShellInstantiatedDependencies(List<ModuleDescriptor> dependencies) {

	}

	@Override
	public ModuleMainController getMainController() {
		return null;
	}

}
