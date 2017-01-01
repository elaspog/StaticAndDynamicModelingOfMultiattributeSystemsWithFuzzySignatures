package net.prokhyon.modularfuzzy.pathValues;

import javafx.scene.layout.AnchorPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.pathValues.model.fx.FuzzyModelValues;
import net.prokhyon.modularfuzzy.pathValues.utils.ModelDomainIOManager;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class PathValuesModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	private final String VIEW_NAME = "Models";
	private FxModulesViewInfo fxModulesViewInfo;
	private PersistableModelInfo persistableModelInfo;
	private WorkspaceInfo workspaceInfo;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInfo moduleInfo = new FxModulesViewInfo("Model Editor",
				"view/PathValuesLayout.fxml", PathValuesModuleDescriptor.class, AnchorPane.class);
		services.registerView(moduleInfo);

		this.persistableModelInfo = new PersistableModelInfo(new ModelDomainIOManager(),
				null,
				null,
				null,
				null,
				net.prokhyon.modularfuzzy.pathValues.model.fx.FuzzyModelValues.class,
				net.prokhyon.modularfuzzy.pathValues.model.descriptor.FuzzyModelValues.class);

		this.workspaceInfo = new WorkspaceInfo(VIEW_NAME, fxModulesViewInfo, persistableModelInfo);

		services.<FuzzyModelValues> registerModelTypeInStore(workspaceInfo);

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
