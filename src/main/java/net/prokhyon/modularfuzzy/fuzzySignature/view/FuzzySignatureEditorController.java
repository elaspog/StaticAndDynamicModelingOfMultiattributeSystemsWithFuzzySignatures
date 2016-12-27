package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;

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
	private ComboBox aggregationOperatorComboBox;

	@FXML
	private ComboBox automatonTypeComboBox;

	@FXML
	private Button createSignatureButton;

	@FXML
	private Button clearSignatureButton;

	@FXML
	private Button saveSignatureButton;

	@FXML
	private Button saveNodeButton;

    /*
     * Variables
     */

	private int createdSignatureCounter;
	private int latestCreatedSignatureCounterValue;
	private int createdNodeCounter;

    /*
     * Methods
     */

	@FXML
	private void initialize() {

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
	}

	private void bindSignatureViewElementsToControllerProperties() {
	}

	private void unbindSignatureViewElementsFromControllerProperties() {
	}

	private void bindNodeViewElementsToControllerProperties() {

		if (fuzzyNodeToEdit.getValue() != null) {
			nodeNameTextField.textProperty().bindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeNameProperty());
			nodeDescriptionTextArea.textProperty().bindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeDescriptionProperty());
			//aggregationOperatorComboBox
			//automatonTypeComboBox
		}
	}

	private void unbindNodeViewElementsFromControllerProperties() {

		if (fuzzyNodeToEdit.getValue() != null) {
			nodeNameTextField.textProperty().unbindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeNameProperty());
			nodeDescriptionTextArea.textProperty().unbindBidirectional(fuzzyNodeToEdit.getValue().fuzzyNodeDescriptionProperty());
			//aggregationOperatorComboBox
			//automatonTypeComboBox
		}
	}

	private void setSignatureViewElements(FuzzySignature fuzzySignature) {

		this.fuzzySignature.set(fuzzySignature);
		signatureNameTextField.textProperty().set(fuzzySignature.fuzzySignatureNameProperty().get());
		//signatureDescriptionTextArea.textProperty().set();
	}

	private void clearSignatureViewElements() {
	}

	private void setNodeViewElements(FuzzyNode fuzzyNode) {

		this.fuzzyNodeToEdit.setValue(fuzzyNode);
		nodeNameTextField.textProperty().set(fuzzyNode.getFuzzyNodeName());
		nodeDescriptionTextArea.textProperty().set(fuzzyNode.getDescription());
	}

	private void clearNodeViewElements() {

		this.fuzzyNodeToEdit.setValue(null);
		nodeNameTextField.textProperty().set(null);
		nodeDescriptionTextArea.textProperty().set(null);
	}


	public int getNextNodeCounterValue(){

		if (createdSignatureCounter != latestCreatedSignatureCounterValue) {
			createdNodeCounter = 0;
			latestCreatedSignatureCounterValue = createdSignatureCounter;
		}
		createdNodeCounter++;
		return createdNodeCounter - 1;
	}

	@FXML
	public void createSignature(){

		createdSignatureCounter++;

		final FuzzyNode rootNode = new FuzzyNode("Node" + getNextNodeCounterValue());
		final FuzzySignature fuzzySignature = new FuzzySignature(null, "Signature" + createdSignatureCounter, rootNode);

		loadWithData(fuzzySignature);
	}

	@FXML
	public void clearSignature(){

		signatureTreeView.setRoot(null);
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

		setSignatureViewElements(signatureToLoad);
	}

}