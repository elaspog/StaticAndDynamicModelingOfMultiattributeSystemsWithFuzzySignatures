package net.prokhyon.modularfuzzy.shell.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.util.ContentLoaderHandler;

public class ShellLayoutController {

	@FXML
	private AnchorPane contentArea;

	@FXML
	private VBox moduleSelectorButtons;

	@FXML
	private TabPane workspaceTabPane;

	public ShellLayoutController() {

	}

	@FXML
	private void initialize() {

	}

	public void loadModules() throws IOException {

		loadWorkspace();
		loadSideButtons();
	}

	private void loadWorkspace() {

		Map<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> registeredStores = new ServiceFactory()
				.getShellServices().getRegisteredStores();

		for (Map.Entry<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			SharedWorkspaceControlAndController swcac = new SharedWorkspaceControlAndController(contentArea,
					entry.getKey(), entry.getValue());
			Tab t = new Tab();
			t.setContent(swcac);
			t.setText(entry.getKey().getViewName());
			workspaceTabPane.getTabs().add(t);
		}
	}

	private void loadSideButtons() {

		moduleSelectorButtons.setSpacing(5);
		moduleSelectorButtons.setPadding(new Insets(5, 0, 0, 0));

		List<FxModulesViewInformationGroup> registeredViews = new ServiceFactory().getShellServices()
				.getRegisteredViews();

		for (FxModulesViewInformationGroup viewToLoad : registeredViews) {

			Button buttonCurrent = new Button(viewToLoad.getViewName());
			buttonCurrent.setMaxWidth(Double.MAX_VALUE);
			buttonCurrent.setMinHeight(30);
			moduleSelectorButtons.getChildren().addAll(buttonCurrent);

			buttonCurrent.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {

					ContentLoaderHandler.loadContent(viewToLoad, contentArea);
				}
			});

		}
	}

}
