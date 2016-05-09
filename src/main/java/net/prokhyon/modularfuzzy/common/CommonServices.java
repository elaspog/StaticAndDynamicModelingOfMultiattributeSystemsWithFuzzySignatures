package net.prokhyon.modularfuzzy.common;

import javafx.scene.layout.Pane;

public interface CommonServices {

	void registerView(String viewName, String viewRelativePath, Class<?> relativeResourceClass,
			Class<? extends Pane> paneType);

}
