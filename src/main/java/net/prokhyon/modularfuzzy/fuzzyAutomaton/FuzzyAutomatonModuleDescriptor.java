package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.GridPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
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

		PersistableModelInfo pmt = new PersistableModelInfo(null,
				null,
				null,
				null,
				null,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.FuzzyAutomaton.class,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton.class);

		WorkspaceInfo storeInfo = new WorkspaceInfo("Automatons", viewOfModuleInfo, pmt);

		services.<FuzzyAutomaton> registerModelTypeInStore(storeInfo);
		services.registerPersistenceMethod(pmt);

	}

}
