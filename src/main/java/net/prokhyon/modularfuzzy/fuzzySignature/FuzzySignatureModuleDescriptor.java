package net.prokhyon.modularfuzzy.fuzzySignature;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInformationGroup moduleInfo = new FxModulesViewInformationGroup("Fuzzy Signature Editor",
				"view/FuzzySignatureLayout.fxml", FuzzySignatureModuleDescriptor.class, StackPane.class);
		services.registerView(moduleInfo);
	}

}
