package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Automaton Editor", "view/FuzzyAutomatonEditorLayout.fxml",
				FuzzyAutomatonModuleDescriptor.class, StackPane.class);
	}

}
