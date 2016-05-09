package net.prokhyon.modularfuzzy.pathValues;

import javafx.scene.layout.StackPane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;

public class PathValuesModuleDescriptor implements ModuleDescriptor {

	private CommonServices services;

	@Override
	public void initializeModule() {
		services = CommonServicesImplSingleton.getInstance();
		services.registerView("Path Values Editor", "view/PathValuesLayout.fxml", PathValuesModuleDescriptor.class,
				StackPane.class);
	}

}
