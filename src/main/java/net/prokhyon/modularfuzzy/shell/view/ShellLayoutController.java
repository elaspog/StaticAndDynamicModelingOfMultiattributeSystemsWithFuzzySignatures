package net.prokhyon.modularfuzzy.shell.view;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modules.DefaultModelLoaderInfo;
import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.PersistableModelInfo;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;
import net.prokhyon.modularfuzzy.shell.util.ContentLoaderHandler;
import net.prokhyon.modularfuzzy.shell.util.PaneAndControllerPair;

public class ShellLayoutController {

	@FXML
	private AnchorPane contentArea;

	@FXML
	private VBox moduleSelectorButtons;

	@FXML
	private TabPane workspaceTabPane;

	@FXML
	private Menu defaultModelLoaderMenu;


	private CommonServices commonServices;
	private ShellDialogServices shellDialogServices;

	public Map<FxModulesViewInfo, PaneAndControllerPair> getContents() {
		return contents;
	}

	private Map<FxModulesViewInfo, PaneAndControllerPair> contents;

	public ShellLayoutController() {

		commonServices = new ServiceFactory().getCommonServices();
		shellDialogServices = new ServiceFactory().getShellDialogServices();
	}

	@FXML
	private void initialize() {

	}

	public void loadModules() throws IOException {

		initializeContentsForContentArea();
		initializeTabTablesWorkspaceArea();
		initializeSideButtonSelectorArea();
		initializeDefaultModelLoaderArea();
	}

	private void initializeContentsForContentArea() {

		contents = new HashMap<>();
		List<FxModulesViewInfo> registeredViews = new ServiceFactory().getShellServices().getRegisteredViews();
		for (FxModulesViewInfo viewToLoad : registeredViews) {
			PaneAndControllerPair p = ContentLoaderHandler.initializeContentPane(viewToLoad);
			contents.put(viewToLoad, p);
		}
	}

	private void initializeTabTablesWorkspaceArea() {

		Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores
				= commonServices.getRegisteredStores();

		for (Map.Entry<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> entry : registeredStores
				.entrySet()) {

			SharedWorkspaceControlAndController swcac = new SharedWorkspaceControlAndController(contentArea,
					entry.getKey(), entry.getValue(), this);
			Tab t = new Tab();
			t.setContent(swcac);
			t.setText(entry.getKey().getViewName());
			workspaceTabPane.getTabs().add(t);
		}
	}

	private void initializeSideButtonSelectorArea() {

		moduleSelectorButtons.setSpacing(5);
		moduleSelectorButtons.setPadding(new Insets(5, 0, 0, 0));

		List<FxModulesViewInfo> registeredViews = new ServiceFactory().getShellServices()
				.getRegisteredViews();

		for (FxModulesViewInfo viewToLoad : registeredViews) {

			Button buttonCurrent = new Button(viewToLoad.getViewName());
			buttonCurrent.setMaxWidth(Double.MAX_VALUE);
			buttonCurrent.setMinHeight(30);
			moduleSelectorButtons.getChildren().addAll(buttonCurrent);

			buttonCurrent.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ae) {

					PaneAndControllerPair p = contents.get(viewToLoad);
					ContentLoaderHandler.loadContentPane(contentArea, null, p);
					AnchorPane.setTopAnchor(p.pane, 0.0);
					AnchorPane.setLeftAnchor(p.pane, 0.0);
					AnchorPane.setRightAnchor(p.pane, 0.0);
					AnchorPane.setBottomAnchor(p.pane, 0.0);
				}
			});

		}
	}

	private void initializeDefaultModelLoaderArea() {

		final List<DefaultModelLoaderInfo> defaultModelLoaderInfos = commonServices.gerRegitsteredDefaultModelLoaders();
		for (DefaultModelLoaderInfo defaultModelLoaderInfo : defaultModelLoaderInfos){
			final PersistableModelInfo persistableModelInfo = defaultModelLoaderInfo.getPersistableModelInfo();

			final MenuItem menuItem = new MenuItem(defaultModelLoaderInfo.getViewName());
			menuItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {

					List<File> defaultModelFiles = listDefaultModelFiles();
					final List<FuzzyDescriptorBase> fuzzyDescriptorBases
							= commonServices.loadFilesIntoDescriptorsAndFilterByPersistableModel(defaultModelFiles, persistableModelInfo);
					commonServices.loadDescriptorsIntoWorkspaceElementsByPersistableModel(fuzzyDescriptorBases, persistableModelInfo);
				}
			});
			defaultModelLoaderMenu.getItems().add(menuItem);
		}
	}

	@FXML
	private void loadAllDefaultModels() {

		List<File> defaultModelFiles = listDefaultModelFiles();
		commonServices.loadFiles(defaultModelFiles);
	}

	@FXML
	private void loadModels() {

		List<File> filesToLoad = shellDialogServices.openFilesDialog();
		if (filesToLoad != null)
			commonServices.loadFiles(filesToLoad);
		// TODO commonServices.validateLoadedFiles();
	}

	@FXML
	private void unloadModels() {

	}

	@FXML
	private void saveSelectedModelsFromWorkspace() {

		final Node content = workspaceTabPane.getSelectionModel().getSelectedItem().getContent();
		final SharedWorkspaceControlAndController selectedController = (SharedWorkspaceControlAndController) content;
		final ObservableList<WorkspaceElement> sharedModels = selectedController.getSelectedSharedModels();
		final WorkspaceInfo workspaceInfo = selectedController.getWorkspaceInfo();
		try {
			commonServices.saveModelByModule(sharedModels, workspaceInfo);
		} catch (ModuleImplementationException mie){
			shellDialogServices.informErrorWithStacktraceDialog(mie,
					"Model export error",
					"Error occured while exporting",
					"A module has implemented incorrectly the IPersistableModel interface.");
		}
	}

	public List<File> listDefaultModelFiles() {

		List<File> filesToLoad = new ArrayList<>();
		try(Stream<Path> paths = Files.walk(Paths.get("./coremodels/"))) {
			paths.forEach(filePath -> {
				if (Files.isRegularFile(filePath)) {
					filesToLoad.add(filePath.toFile());
				}
			});
		} catch (Exception e){ }
		return filesToLoad;
	}

}
