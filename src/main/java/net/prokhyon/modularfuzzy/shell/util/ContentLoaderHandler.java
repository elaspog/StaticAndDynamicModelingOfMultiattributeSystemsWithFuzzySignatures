package net.prokhyon.modularfuzzy.shell.util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;

public class ContentLoaderHandler {

	public static void loadContent(FxModulesViewInformationGroup viewToLoad, AnchorPane whereToLoad) {

		loadContent(viewToLoad, whereToLoad, null);
	}

	public static void loadContent(FxModulesViewInformationGroup viewToLoad, AnchorPane whereToLoad,
			WorkspaceElement selectedItem) {

		String viewName = viewToLoad.getViewName();
		String viewRelativePath = viewToLoad.getViewRelativePath();
		Class<?> relativeResourceClass = viewToLoad.getRelativeResourceClass();
		Class<? extends Pane> paneType = viewToLoad.getPaneType();

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(relativeResourceClass.getResource(viewRelativePath));
			// Pane p = loader.load();
			Pane p = paneType.cast(loader.load());
			loadDataToViewIfPossible(selectedItem, loader);
			whereToLoad.getChildren().setAll(p);
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
