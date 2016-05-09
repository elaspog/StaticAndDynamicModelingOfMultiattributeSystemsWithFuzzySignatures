package net.prokhyon.modularfuzzy.fuzzySet;

import javafx.scene.layout.TilePane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Set Editor", "view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class,
				TilePane.class);
	}

}
