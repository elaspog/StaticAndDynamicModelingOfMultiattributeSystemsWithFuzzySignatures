package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();

		FxModulesViewInformationGroup moduleInfo = new FxModulesViewInformationGroup("Fuzzy Automaton Editor",
				"view/FuzzyAutomatonEditorLayout.fxml", FuzzyAutomatonModuleDescriptor.class, StackPane.class);
		services.registerView(moduleInfo);
	}

}
