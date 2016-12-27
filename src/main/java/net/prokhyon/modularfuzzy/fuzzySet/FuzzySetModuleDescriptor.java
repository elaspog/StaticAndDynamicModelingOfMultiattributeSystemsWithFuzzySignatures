package net.prokhyon.modularfuzzy.fuzzySet;

import javafx.scene.layout.TilePane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.DefaultModelLoaderInfo;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzySet.util.ModelDomainIOManager;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	private final String VIEW_NAME = "Sets";
	private FxModulesViewInfo fxModulesViewInfo;
	private PersistableModelInfo persistableModelInfo;
	private WorkspaceInfo workspaceInfo;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		this.fxModulesViewInfo = new FxModulesViewInfo("Fuzzy Set Editor",
				"view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class, TilePane.class);
		services.registerView(fxModulesViewInfo);

		this.persistableModelInfo = new PersistableModelInfo(new ModelDomainIOManager(),
				net.prokhyon.modularfuzzy.fuzzySet.model.ModelConverter.class,
				null,
				null,
				net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetBase.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTrapezoidal.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTriangular.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetPolygonal.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBase.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointAbove.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBelow.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom.class);

		this.workspaceInfo = new WorkspaceInfo(VIEW_NAME, fxModulesViewInfo, persistableModelInfo);

		services.<net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem> registerModelTypeInStore(workspaceInfo);

		services.registerDefaultModelLoader(new DefaultModelLoaderInfo("Load Fuzzy Systems", persistableModelInfo));
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
