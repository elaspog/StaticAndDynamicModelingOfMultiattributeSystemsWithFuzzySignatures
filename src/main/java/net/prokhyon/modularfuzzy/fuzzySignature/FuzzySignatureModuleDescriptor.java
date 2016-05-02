package net.prokhyon.modularfuzzy.fuzzySignature;

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

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		dependencies.add(FuzzyAutomatonModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Signature";
	}

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Signature 1", "view/Signature1Layout.fxml", FuzzySignatureModuleDescriptor.class,
				StackPane.class);
		services.registerView("Fuzzy Signature 2", "view/Signature2Layout.fxml", FuzzySignatureModuleDescriptor.class,
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
