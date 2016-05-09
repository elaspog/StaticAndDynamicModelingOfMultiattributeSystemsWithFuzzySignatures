package net.prokhyon.modularfuzzy.fuzzySignature;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Fuzzy Signature Editor", "view/FuzzySignatureLayout.fxml",
				FuzzySignatureModuleDescriptor.class, StackPane.class);
	}

}
