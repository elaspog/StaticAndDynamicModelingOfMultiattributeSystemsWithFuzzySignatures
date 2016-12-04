package net.prokhyon.modularfuzzy.fuzzySet;

import javafx.scene.layout.TilePane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.PersistableModelTuple;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInformationGroup viewOfModuleInfo = new FxModulesViewInformationGroup("Fuzzy Set Editor",
				"view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class, TilePane.class);
		services.registerView(viewOfModuleInfo);

		PersistableModelTuple pmt = new PersistableModelTuple(null,
				net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem.class);

		WorkspaceInformationGroup storeInfo = new WorkspaceInformationGroup("Fuzzy Sets", net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem.class,
				viewOfModuleInfo, pmt);

		services.<net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem> registerModelTypeInStore(storeInfo);
		services.registerPersistenceMethod(pmt);

	}

}
