package net.prokhyon.modularfuzzy.fuzzySignature;

import javafx.scene.layout.AnchorPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzySignature.model.FuzzySignature;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInfo viewOfModuleInfo = new FxModulesViewInfo("Fuzzy Signature Editor",
				"view/FuzzySignatureLayout.fxml", FuzzySignatureModuleDescriptor.class, AnchorPane.class);
		services.registerView(viewOfModuleInfo);

		PersistableModelInfo pmt = new PersistableModelInfo(null,
				null,
				null,
				null,
				null,
				net.prokhyon.modularfuzzy.fuzzySignature.model.FuzzySignature.class,
				net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature.class);

		WorkspaceInfo storeInfo = new WorkspaceInfo("Signatures", viewOfModuleInfo, pmt);

		services.<FuzzySignature> registerModelTypeInStore(storeInfo);
	}

}
