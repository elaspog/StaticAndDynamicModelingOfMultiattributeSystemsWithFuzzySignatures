package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.AggregationType;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

import java.util.Map;

public class FuzzySignatureEditorController implements LoadableDataController {

    /*
     * Properties
     */

	private ObjectProperty<FuzzySignature> fuzzySignature;

	private ObjectProperty<FuzzyNode> fuzzyNodeToEdit;

    /*
     * UI elements
     */

	@FXML
	private TreeView<FuzzyNode> signatureTreeView;

	@FXML
	private TextField signatureNameTextField;

	@FXML
	private TextField nodeNameTextField;

	@FXML
	private TextArea signatureDescriptionTextArea;

	@FXML
	private TextArea nodeDescriptionTextArea;

	@FXML
	private ComboBox<AggregationType> aggregationOperatorComboBox;

	@FXML
	private ComboBox<FuzzyAutomaton> automatonTypeComboBox;

	@FXML
	private Button createSignatureButton;

	@FXML
	private Button clearSignatureButton;

	@FXML
	private Button saveSignatureButton;

    /*
     * Variables
     */

	private int createdSignatureCounter;
	private int latestCreatedSignatureCounterValue;
	private int createdNodeCounter;

	/*
     * Services
     */

	private CommonServices commonServices;

	private FuzzyAutomatonModuleDescriptor fuzzyAutomatonModuleDescriptor;

    /*
     * Methods
     */

	@FXML
	private void initialize() {

		this.commonServices = new ServiceFactory().getCommonServices();
		final Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = this.commonServices.getPseudoModules();
		for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> classModuleDescriptorEntry : pseudoModules.entrySet()) {
			final Class<? extends ModuleDescriptor> key = classModuleDescriptorEntry.getKey();
			final ModuleDescriptor value = classModuleDescriptorEntry.getValue();
			if (key == FuzzyAutomatonModuleDescriptor.class ) {
				final FuzzyAutomatonModuleDescriptor famdv = (FuzzyAutomatonModuleDescriptor) value;
				if(famdv.getViewName().equals("Automatons")){
					this.fuzzyAutomatonModuleDescriptor = famdv;
				}
			}
		}

		createdSignatureCounter = 0;
		latestCreatedSignatureCounterValue = 0;
		createdNodeCounter = 0;

		this.fuzzySignature = new SimpleObjectProperty<>();
		this.fuzzyNodeToEdit = new SimpleObjectProperty<>();

		signatureTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			unbindNodeViewElementsFromControllerProperties();
			clearNodeViewElements();
            if (newValue != null) {
				setNodeViewElements(newValue.getValue());
				bindNodeViewElementsToControllerProperties();
			}
        });


		automatonTypeComboBox.setCellFactory(new Callback<ListView<FuzzyAutomaton>,ListCell<FuzzyAutomaton>>(){

			@Override
			public ListCell<FuzzyAutomaton> call(ListView<FuzzyAutomaton> p) {

				final ListCell<FuzzyAutomaton> cell = new ListCell<FuzzyAutomaton>(){

					@Override
					protected void updateItem(FuzzyAutomaton t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null)
							setText(t.getFuzzyAutomationName() + " : " + t.getUuid());
						else
							setText(null);
					}
				};
				return cell;
			}
		});

		automatonTypeComboBox.setButtonCell(
				new ListCell<FuzzyAutomaton>() {
					@Override
					protected void updateItem(FuzzyAutomaton t, boolean bln) {
						super.updateItem(t, bln);
						if (bln) {
							setText("");
						} else {
							setText(t.getFuzzyAutomationName());
						}
					}
				});
	}

	private void bindSignatureViewElementsToControllerProperties() {

		if(fuzzySignature.getValue() != null){
			signatureNameTextField.textProperty().bindBidirectional(fuzzySignature.getValue().fuzzySignatureNameProperty());
			signatureDescriptionTextArea.textProperty().bindBidirectional(fuzzySignature.getValue().fuzzySignatureDescriptionProperty());
		}
	}

	private void bindNodeViewElementsToControllerProperties() {

		if (fuzzyNodeToEdit.getValue() != null) {
			nodeNameTextField.textProperty().bindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeNameProperty());
			nodeDescriptionTextArea.textProperty().bindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeDescriptionProperty());

			aggregationOperatorComboBox.setItems(FXCollections.observableArrayList(AggregationType.values()));
			aggregationOperatorComboBox.valueProperty().bindBidirectional(fuzzyNodeToEdit.getValue().aggregationTypeProperty());
			automatonTypeComboBox.valueProperty().bindBidirectional(fuzzyNodeToEdit.getValue().fuzzyAutomatonProperty());
		}
	}

	private void unbindSignatureViewElementsFromControllerProperties() {

		if(fuzzySignature.getValue() != null){
			signatureNameTextField.textProperty().unbindBidirectional(fuzzySignature.getValue().fuzzySignatureNameProperty());
			signatureDescriptionTextArea.textProperty().unbindBidirectional(fuzzySignature.getValue().fuzzySignatureDescriptionProperty());
		}
	}

	private void unbindNodeViewElementsFromControllerProperties() {

		if (fuzzyNodeToEdit.getValue() != null) {
			nodeNameTextField.textProperty().unbindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeNameProperty());
			nodeDescriptionTextArea.textProperty().unbindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeDescriptionProperty());
			aggregationOperatorComboBox.valueProperty().unbindBidirectional(fuzzyNodeToEdit.get().aggregationTypeProperty());
			automatonTypeComboBox.valueProperty().unbindBidirectional(fuzzyNodeToEdit.get().fuzzyAutomatonProperty());
		}
	}

	private void setSignatureViewElements(FuzzySignature fuzzySignature) {

		this.fuzzySignature.set(fuzzySignature);
		signatureNameTextField.textProperty().set(fuzzySignature.fuzzySignatureNameProperty().get());
		signatureDescriptionTextArea.textProperty().set(fuzzySignature.fuzzySignatureDescriptionProperty().get());
	}

	private void setNodeViewElements(FuzzyNode fuzzyNode) {

		loadActualFuzzyAutomatons();
		this.fuzzyNodeToEdit.setValue(fuzzyNode);
		nodeNameTextField.textProperty().set(fuzzyNode.getFuzzyNodeName());
		nodeDescriptionTextArea.textProperty().set(fuzzyNode.getFuzzyNodeDescription());
		aggregationOperatorComboBox.valueProperty().set(fuzzyNode.getAggregationType());
		automatonTypeComboBox.valueProperty().set(fuzzyNode.getFuzzyAutomaton());
	}

	private void clearSignatureViewElements() {

		this.fuzzySignature.set(null);
		signatureNameTextField.textProperty().set(null);
		signatureDescriptionTextArea.textProperty().set(null);
	}

	private void clearNodeViewElements() {

		this.fuzzyNodeToEdit.setValue(null);
		nodeNameTextField.textProperty().set(null);
		nodeDescriptionTextArea.textProperty().set(null);
		aggregationOperatorComboBox.getItems().clear();
		automatonTypeComboBox.getItems().clear();
	}

	@FXML
	public void createSignature(){

		createdSignatureCounter++;

		final FuzzyNode rootNode = new FuzzyNode("Node" + getNextNodeCounterValue());
		final FuzzySignature fuzzySignature = new FuzzySignature(null, "Signature" + createdSignatureCounter, rootNode, "sample description");

		loadWithData(fuzzySignature);
	}

	@FXML
	public void clearSignature(){

		signatureTreeView.setRoot(null);
		signatureNameTextField.textProperty().set(null);
		signatureDescriptionTextArea.textProperty().set(null);
	}

	@FXML
	private void loadActualFuzzyAutomatons(){

		final Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = this.commonServices.getRegisteredStores();
		final WorkspaceInfo workspaceInfo = fuzzyAutomatonModuleDescriptor.getWorkspaceInfo();
		final ObservableList<? extends WorkspaceElement> workspaceElements = registeredStores.get(workspaceInfo);

		final ObservableList<FuzzyAutomaton> fuzzySetSystems = (ObservableList<FuzzyAutomaton>) workspaceElements;
		this.automatonTypeComboBox.setItems(FXCollections.observableArrayList(fuzzySetSystems));
		//this.automatonTypeComboBox.valueProperty().bindBidirectional(fuzzyNodeToEdit.get().fuzzyAutomatonProperty());
	}

	public int getNextNodeCounterValue(){

		if (createdSignatureCounter != latestCreatedSignatureCounterValue) {
			createdNodeCounter = 0;
			latestCreatedSignatureCounterValue = createdSignatureCounter;
		}
		createdNodeCounter++;
		return createdNodeCounter - 1;
	}

	private TreeItem<FuzzyNode> makeBranch(FuzzyNode fuzzyNode, TreeItem<FuzzyNode> parent) {
		TreeItem<FuzzyNode> item = new TreeItem<>(fuzzyNode);
		item.setExpanded(true);
		parent.getChildren().add(item);
		return item;
	}

    /*
     * Implemented interfaces
     */

	@Override
	public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

		unbindSignatureViewElementsFromControllerProperties();
		clearSignatureViewElements();

		if (modelToLoad == null)
			return;

		final FuzzySignature signatureToLoad = (FuzzySignature) modelToLoad;
		final FuzzyNode rootNodeOfTheTree = (signatureToLoad).getRootNodeOfTheTree();
		TreeItem<FuzzyNode> root = new TreeItem<>(rootNodeOfTheTree);
		root.setExpanded(true);
		FuzzySignatureEditorController tmp = this;

		signatureTreeView.setCellFactory(new Callback<TreeView<FuzzyNode>, TreeCell<FuzzyNode>>() {

			@Override
			public TreeCell<FuzzyNode> call(TreeView<FuzzyNode> arg0) {
				// custom tree cell that defines a context menu for the root tree item
				final FuzzyNodeTreeCell fuzzyNodeTreeCell = new FuzzyNodeTreeCell(tmp);
				return fuzzyNodeTreeCell;
			}

		});
		signatureTreeView.setRoot(root);

		bindSignatureViewElementsToControllerProperties();
		setSignatureViewElements(signatureToLoad);
	}

}