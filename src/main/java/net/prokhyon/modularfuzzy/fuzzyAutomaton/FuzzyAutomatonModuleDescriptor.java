package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.GridPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInfo viewOfModuleInfo = new FxModulesViewInfo("Fuzzy Automaton Editor",
				"view/FuzzyAutomatonEditorLayout.fxml", FuzzyAutomatonModuleDescriptor.class, GridPane.class);
		services.registerView(viewOfModuleInfo);

		WorkspaceInfo storeInfo = new WorkspaceInfo("Automatons", FuzzyAutomaton.class,
				viewOfModuleInfo);
		services.<FuzzyAutomaton> registerModelTypeInStore(storeInfo);

	}

}
