package net.prokhyon.modularfuzzy.fuzzySignature;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;
import net.prokhyon.modularfuzzy.fuzzySignature.model.FuzzySignature;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInformationGroup viewOfModuleInfo = new FxModulesViewInformationGroup("Fuzzy Signature Editor",
				"view/FuzzySignatureLayout.fxml", FuzzySignatureModuleDescriptor.class, StackPane.class);
		services.registerView(viewOfModuleInfo);

		WorkspaceInformationGroup storeInfo = new WorkspaceInformationGroup("Signatures", FuzzySignature.class,
				viewOfModuleInfo);
		services.<FuzzySignature> registerModelTypeInStore(storeInfo);

	}

}
