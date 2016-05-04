package net.prokhyon.modularfuzzy.fuzzySet;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.layout.TilePane;
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
		services.registerView("Fuzzy Set 1", "view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class,
				TilePane.class);
	}

	@Override
	public void registerShellInstantiatedDependencies(List<ModuleDescriptor> dependencies) {

	}

	@Override
	public ModuleMainController getMainController() {
		return null;
	}

}
