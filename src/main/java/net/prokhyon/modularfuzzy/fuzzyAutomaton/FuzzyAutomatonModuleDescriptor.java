package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import javafx.scene.layout.FlowPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.util.ModelDomainIOManager;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInfo viewOfModuleInfo = new FxModulesViewInfo("Fuzzy Automaton Editor",
				"view/FuzzyAutomatonEditorLayout.fxml", FuzzyAutomatonModuleDescriptor.class, FlowPane.class);
		services.registerView(viewOfModuleInfo);

		PersistableModelInfo pmt = new PersistableModelInfo(new ModelDomainIOManager(),
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.ModelConverter.class,
				null,
				null,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton.class,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton.class,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton.class,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState.class,
				net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition.class);

		WorkspaceInfo storeInfo = new WorkspaceInfo("Automatons", viewOfModuleInfo, pmt);

		services.<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> registerModelTypeInStore(storeInfo);
	}

}
