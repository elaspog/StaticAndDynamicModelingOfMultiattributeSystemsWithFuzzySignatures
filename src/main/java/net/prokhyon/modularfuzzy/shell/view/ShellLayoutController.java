package net.prokhyon.modularfuzzy.shell.view;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellServices;
import net.prokhyon.modularfuzzy.shell.util.ContentLoaderHandler;
import net.prokhyon.modularfuzzy.shell.util.FxDialogHelper;

public class ShellLayoutController {

	@FXML
	private AnchorPane contentArea;

	@FXML
	private VBox moduleSelectorButtons;

	@FXML
	private TabPane workspaceTabPane;

	private ShellServices services;

	private Stage stage;

	public ShellLayoutController() {

		services = new ServiceFactory().getShellServices();
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

	@FXML
	private void loadDefaultModels() {

	}

	@FXML
	private void loadModels() {

	}

	@FXML
	private void unloadModels() {

	}

	@FXML
	private void saveSelectedModelsFromWorkspace() {

		final Node content = workspaceTabPane.getSelectionModel().getSelectedItem().getContent();
		final SharedWorkspaceControlAndController selectedController = (SharedWorkspaceControlAndController) content;
		final ObservableList<WorkspaceElement> sharedModels = selectedController.getSelectedSharedModels();
		final WorkspaceInformationGroup workspaceInformationGroup = selectedController.getWorkspaceInformationGroup();
		try {
			services.saveModelByModule(sharedModels, workspaceInformationGroup);
		} catch (ModuleImplementationException mie){
			FxDialogHelper.ErrorDialogHelper(mie, "Model export error","Error occured while exporting", "A module has implemented incorrectly the IPersistableModel interface.");
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
