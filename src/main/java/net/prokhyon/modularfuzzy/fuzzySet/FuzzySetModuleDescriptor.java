package net.prokhyon.modularfuzzy.fuzzySet;

import javafx.scene.layout.TilePane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;
import net.prokhyon.modularfuzzy.fuzzySet.model.FuzzySetSystem;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();

		FxModulesViewInformationGroup viewOfModuleInfo = new FxModulesViewInformationGroup("Fuzzy Set Editor",
				"view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class, TilePane.class);
		services.registerView(viewOfModuleInfo);

		WorkspaceInformationGroup storeInfo = new WorkspaceInformationGroup("Fuzzy Sets", FuzzySetSystem.class,
				viewOfModuleInfo);
		services.<FuzzySetSystem> registerModelTypeInStore(storeInfo);

	}

}
