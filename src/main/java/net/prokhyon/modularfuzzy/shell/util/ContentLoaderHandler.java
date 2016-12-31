package net.prokhyon.modularfuzzy.shell.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

public class ContentLoaderHandler {


	public static PaneAndControllerPair initializeContentPane(FxModulesViewInfo viewToLoad) {

		String viewRelativePath = viewToLoad.getViewRelativePath();
		Class<?> relativeResourceClass = viewToLoad.getRelativeResourceClass();
		Class<? extends Pane> paneType = viewToLoad.getPaneType();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(relativeResourceClass.getResource(viewRelativePath));
			// Pane p = loader.load();
			Pane p = paneType.cast(loader.load());
			LoadableDataController c = (LoadableDataController) loader.getController();
			return new PaneAndControllerPair(p, c);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static void loadContentPane(AnchorPane whereToLoad, WorkspaceElement selectedItem, PaneAndControllerPair p) {

		whereToLoad.getChildren().setAll(p.pane);
		if (selectedItem != null) {
			try {
				p.controller.loadWithData(selectedItem);
			} catch (Exception e) {
			}
		}
		AnchorPane.setTopAnchor(p.pane, 0.0);
		AnchorPane.setLeftAnchor(p.pane, 0.0);
		AnchorPane.setRightAnchor(p.pane, 0.0);
		AnchorPane.setBottomAnchor(p.pane, 0.0);
	}

}
