package net.prokhyon.modularfuzzy.shell.view;

import java.io.IOException;
import java.util.Map;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.shell.util.ContentLoaderHandler;
import net.prokhyon.modularfuzzy.shell.util.PaneAndControllerPair;

public class SharedWorkspaceControlAndController<T extends WorkspaceFxRootElementBase> extends AnchorPane {

	@FXML
	private ListView<T> sharedWorkspace;

	@FXML
	private Button loadButton;

	@FXML
	private Button removeButton;

	private AnchorPane contentArea;

	private ObservableList<T> sharedModels;

	private WorkspaceInfo workspaceInfo;

	ShellLayoutController shellLayoutController;

	public SharedWorkspaceControlAndController(AnchorPane contentArea, WorkspaceInfo workspaceInfo,
											   ObservableList<T> value, ShellLayoutController shellLayoutController) {

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
		this.shellLayoutController = shellLayoutController;
	}

	@FXML
	private void initialize() {

		ReadOnlyObjectProperty<T> selectedItemProperty = sharedWorkspace.getSelectionModel().selectedItemProperty();
		BooleanBinding isNoItemSelected = Bindings.isNull(selectedItemProperty);
		loadButton.disableProperty().bind(isNoItemSelected);
		removeButton.disableProperty().bind(isNoItemSelected);

		sharedWorkspace.setCellFactory(new Callback<ListView<T>,ListCell<T>>(){

			@Override
			public ListCell<T> call(ListView<T> p) {

				final ListCell<T> cell = new ListCell<T>(){

					@Override
					protected void updateItem(T t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null)
							setText(t.getListElementIdentifier());
						else
							setText(null);
					}
				};
				return cell;
			}
		});

	}

	@FXML
	protected void loadSelected() {

		T selectedItem = sharedWorkspace.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {

			final Map<FxModulesViewInfo, PaneAndControllerPair> contents = shellLayoutController.getContents();
			final FxModulesViewInfo loaderInformation = workspaceInfo.getLoaderInformation();
			final PaneAndControllerPair paneAndControllerPair = contents.get(loaderInformation);

			ContentLoaderHandler.loadContentPane(contentArea, selectedItem, paneAndControllerPair);
		}
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
