package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.api.ModuleMainController;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Automaton";
	}

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Automaton 1", "view/Automaton1Layout.fxml", FuzzyAutomatonModuleDescriptor.class,
				StackPane.class);
		services.registerView("Fuzzy Automaton 2", "view/Automaton2Layout.fxml", FuzzyAutomatonModuleDescriptor.class,
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
