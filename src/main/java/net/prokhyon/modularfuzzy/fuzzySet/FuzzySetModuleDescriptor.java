package net.prokhyon.modularfuzzy.fuzzySet;

import javafx.scene.layout.TilePane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzySet.util.ModelDomainIOManager;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInfo viewOfModuleInfo = new FxModulesViewInfo("Fuzzy Set Editor",
				"view/FuzzySetEditorLayout.fxml", FuzzySetModuleDescriptor.class, TilePane.class);
		services.registerView(viewOfModuleInfo);

		PersistableModelInfo pmt = new PersistableModelInfo(new ModelDomainIOManager(),
				net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem.class,
				net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem.class);

		WorkspaceInfo storeInfo = new WorkspaceInfo("Fuzzy Sets", viewOfModuleInfo, pmt);

		services.<net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem> registerModelTypeInStore(storeInfo);
		services.registerPersistenceMethod(pmt);

	}

}
