package net.prokhyon.modularfuzzy.shell.view;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonServicesImplSingleton;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;

public class ShellLayoutController {

	CommonServices services;

	@FXML
	private AnchorPane contentArea;

	@FXML
	private VBox moduleSelectorButtons;

	public ShellLayoutController() {

		services = CommonServicesImplSingleton.getInstance();
	}

	@FXML
	private void initialize() {

	}

	public void loadModules() throws IOException {

		moduleSelectorButtons.setSpacing(5);
		moduleSelectorButtons.setPadding(new Insets(5, 0, 0, 0));

		List<FxModulesViewInformationGroup> registeredViews = ((CommonServicesImplSingleton) services)
				.getRegisteredViews();

		for (FxModulesViewInformationGroup e : registeredViews) {

			String viewName = e.getViewName();
			String viewRelativePath = e.getViewRelativePath();
			Class<?> relativeResourceClass = e.getRelativeResourceClass();
			Class<? extends Pane> paneType = e.getPaneType();

			Button buttonCurrent = new Button(viewName);
			buttonCurrent.setMaxWidth(Double.MAX_VALUE);
			buttonCurrent.setMinHeight(30);
			moduleSelectorButtons.getChildren().addAll(buttonCurrent);

			buttonCurrent.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(relativeResourceClass.getResource(viewRelativePath));
						// Pane p = loader.load();
						Pane p = paneType.cast(loader.load());
						// ControllerBase controller = loader.getController();
						// controller.initializeController();
						contentArea.getChildren().setAll(p);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});

		}

	}

}
