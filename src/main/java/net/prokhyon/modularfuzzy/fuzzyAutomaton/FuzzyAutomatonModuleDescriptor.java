package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.GridPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInformationGroup moduleInfo = new FxModulesViewInformationGroup("Fuzzy Automaton Editor",
				"view/FuzzyAutomatonEditorLayout.fxml", FuzzyAutomatonModuleDescriptor.class, GridPane.class);
		services.registerView(moduleInfo);
	}

}
