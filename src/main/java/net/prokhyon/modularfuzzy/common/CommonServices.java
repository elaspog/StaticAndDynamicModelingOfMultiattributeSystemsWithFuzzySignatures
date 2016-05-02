package net.prokhyon.modularfuzzy.common;

import java.util.List;

import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;

public interface CommonServices {

	List<ModuleDescriptor> getModuleDependencyInstances(Class<? extends ModuleDescriptor> moduleTypes);

	void registerView(String viewName, String viewRelativePath, Class<?> relativeResourceClass,
			Class<? extends Pane> paneType);

	void loadViewToContentArea(String view);

	void loadViewToModalWindow(String view);

	CommonServices getCommonServices();

}
