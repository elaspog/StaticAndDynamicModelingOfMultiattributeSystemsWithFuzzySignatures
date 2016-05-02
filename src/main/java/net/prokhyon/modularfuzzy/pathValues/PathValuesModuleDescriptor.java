package net.prokhyon.modularfuzzy.pathValues;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.api.ModuleMainController;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;

public class PathValuesModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		dependencies.add(FuzzyAutomatonModuleDescriptor.class);
		dependencies.add(FuzzySignatureModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Path Values";
	}

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Path Values 1", "view/PathValues1Layout.fxml", PathValuesModuleDescriptor.class,
				StackPane.class);
		services.registerView("Path Values 2", "view/PathValues2Layout.fxml", PathValuesModuleDescriptor.class,
				AnchorPane.class);
	}

	@Override
	public void registerShellInstantiatedDependencies(List<ModuleDescriptor> dependencies) {

	}

	@Override
	public ModuleMainController getMainController() {
		return null;
	}

}
