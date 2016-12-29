package net.prokhyon.modularfuzzy.fuzzySignature;

import javafx.scene.layout.AnchorPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;
import net.prokhyon.modularfuzzy.fuzzySignature.util.ModelDomainIOManager;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	private final String VIEW_NAME = "Signatures";
	private FxModulesViewInfo fxModulesViewInfo;
	private PersistableModelInfo persistableModelInfo;
	private WorkspaceInfo workspaceInfo;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		this.fxModulesViewInfo = new FxModulesViewInfo("Fuzzy Signature Editor",
				"view/FuzzySignatureLayout.fxml", FuzzySignatureModuleDescriptor.class, AnchorPane.class);
		services.registerView(fxModulesViewInfo);

		this.persistableModelInfo = new PersistableModelInfo(new ModelDomainIOManager(),
				net.prokhyon.modularfuzzy.fuzzySignature.model.ModelConverter.class,
				null,
				null,
				net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature.class,
				net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature.class,
				net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature.class);

		this.workspaceInfo = new WorkspaceInfo(VIEW_NAME, fxModulesViewInfo, persistableModelInfo);

		services.<FuzzySignature> registerModelTypeInStore(workspaceInfo);
	}

	public String getViewName() {
		return VIEW_NAME;
	}

	public FxModulesViewInfo getFxModulesViewInfo() {
		return fxModulesViewInfo;
	}

	public PersistableModelInfo getPersistableModelInfo() {
		return persistableModelInfo;
	}

	public WorkspaceInfo getWorkspaceInfo() {
		return workspaceInfo;
	}

}
