package net.prokhyon.modularfuzzy.shell.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;

public class ContentLoaderHandler {

	public static void loadContent(FxModulesViewInfo viewToLoad, AnchorPane whereToLoad) {

		loadContent(viewToLoad, whereToLoad, null);
	}

	public static void loadContent(FxModulesViewInfo viewToLoad, AnchorPane whereToLoad,
								   WorkspaceElement selectedItem) {

		String viewRelativePath = viewToLoad.getViewRelativePath();
		Class<?> relativeResourceClass = viewToLoad.getRelativeResourceClass();
		Class<? extends Pane> paneType = viewToLoad.getPaneType();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(relativeResourceClass.getResource(viewRelativePath));
			// Pane p = loader.load();
			Pane p = paneType.cast(loader.load());
			whereToLoad.getChildren().setAll(p);
			loadDataToViewIfPossible(selectedItem, loader);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static void loadDataToViewIfPossible(WorkspaceElement selectedItem, FXMLLoader loader) {

		if (selectedItem != null) {
			try {
				LoadableDataController controller = (LoadableDataController) loader.getController();
				controller.loadWithData(selectedItem);
			} catch (Exception e) {
			}
		}
	}
}
