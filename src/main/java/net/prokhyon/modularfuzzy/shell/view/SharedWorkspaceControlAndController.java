package net.prokhyon.modularfuzzy.shell.view;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.WorkspaceInfo;
import net.prokhyon.modularfuzzy.shell.util.ContentLoaderHandler;

public class SharedWorkspaceControlAndController<T extends WorkspaceElement> extends AnchorPane {

	@FXML
	private ListView<T> sharedWorkspace;

	@FXML
	private Button loadButton;

	@FXML
	private Button removeButton;

	private AnchorPane contentArea;

	private ObservableList<T> sharedModels;

	private WorkspaceInfo workspaceInfo;

	public SharedWorkspaceControlAndController(AnchorPane contentArea,
											   WorkspaceInfo workspaceInfo, ObservableList<T> value) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SharedWorkspaceControl.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		this.sharedModels = value;
		this.sharedWorkspace.itemsProperty().set(sharedModels);
		this.sharedWorkspace.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		this.contentArea = contentArea;
		this.workspaceInfo = workspaceInfo;

	}

	@FXML
	private void initialize() {

		ReadOnlyObjectProperty<T> selectedItemProperty = sharedWorkspace.getSelectionModel().selectedItemProperty();
		BooleanBinding isNoItemSelected = Bindings.isNull(selectedItemProperty);
		loadButton.disableProperty().bind(isNoItemSelected);
		removeButton.disableProperty().bind(isNoItemSelected);
	}

	@FXML
	protected void loadSelected() {

		T selectedItem = sharedWorkspace.getSelectionModel().getSelectedItem();
		if (selectedItem != null)
			ContentLoaderHandler.loadContent(workspaceInfo.getLoaderInformation(), contentArea,
					selectedItem);
	}

	@FXML
	protected void removeSelected() {

		T selectedItem = sharedWorkspace.getSelectionModel().getSelectedItem();
		sharedModels.remove(selectedItem);
	}


	public ObservableList<T> getAllSharedModels() {
		return sharedModels;
	}

	public ObservableList<T> getSelectedSharedModels() {
		return sharedWorkspace.getSelectionModel().getSelectedItems();
	}

	public WorkspaceInfo getWorkspaceInfo() {
		return workspaceInfo;
	}

}
