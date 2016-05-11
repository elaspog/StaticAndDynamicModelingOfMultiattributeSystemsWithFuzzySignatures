package net.prokhyon.modularfuzzy.pathValues;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

public class PathValuesModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = new ServiceFactory().getCommonServices();

		FxModulesViewInformationGroup moduleInfo = new FxModulesViewInformationGroup("Path Values Editor",
				"view/PathValuesLayout.fxml", PathValuesModuleDescriptor.class, StackPane.class);
		services.registerView(moduleInfo);
	}

}
