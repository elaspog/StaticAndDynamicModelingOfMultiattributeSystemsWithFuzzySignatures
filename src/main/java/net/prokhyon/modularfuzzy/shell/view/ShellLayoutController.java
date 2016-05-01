package net.prokhyon.modularfuzzy.shell.view;

import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;

public class ShellLayoutController {

	@FXML
	private AnchorPane contentArea;

	@FXML
	private VBox moduleSelectorButtons;

	public ShellLayoutController() {
	}

	@FXML
	private void initialize() {

		contentArea = new AnchorPane();
	}

	public void loadModules(List<ModuleDescriptor> pseudoModules) {

		moduleSelectorButtons.setSpacing(10);
		moduleSelectorButtons.setPadding(new Insets(10, 0, 0, 0));

		for (ModuleDescriptor md : pseudoModules) {

			Button buttonCurrent = new Button(md.getPublicName());
			buttonCurrent.setMaxWidth(Double.MAX_VALUE);
			buttonCurrent.setMinHeight(50);
			moduleSelectorButtons.getChildren().addAll(buttonCurrent);
		}

	}

}
