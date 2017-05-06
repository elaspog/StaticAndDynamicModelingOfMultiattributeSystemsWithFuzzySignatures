package net.prokhyon.modularfuzzy.fuzzySignature.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.errors.ModelError;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.modules.WorkspaceInfo;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.AggregationType;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.net.URL;
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
	private Spinner<Integer> costVectorConstraintSpinner;

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

	@FXML
	private WebView signatureViewer;

	@FXML
	private WebView compoundAutomatonViewer;

	@FXML
	private TabPane tabPane;

	@FXML
	private AnchorPane signatureViewerAnchorPane;

	@FXML
	private AnchorPane compoundAutomatonViewerAnchorPane;

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

	private FuzzySignatureModuleDescriptor fuzzySignatureModuleDescriptor;

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
			if (key == FuzzySignatureModuleDescriptor.class ) {
				final FuzzySignatureModuleDescriptor fsmdv = (FuzzySignatureModuleDescriptor) value;
				if(fsmdv.getViewName().equals("Signatures")){
					this.fuzzySignatureModuleDescriptor = fsmdv;
				}
			}
		}

		createdSignatureCounter = 0;
		latestCreatedSignatureCounterValue = 0;
		createdNodeCounter = 0;

		costVectorConstraintSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		costVectorConstraintSpinner.setEditable(true);

		this.fuzzySignature = new SimpleObjectProperty<>();
		this.fuzzyNodeToEdit = new SimpleObjectProperty<>();

		signatureTreeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

			unbindNodeViewElementsFromControllerProperties();
			clearNodeViewElements();
            if (newValue != null) {

				aggregationOperatorComboBox.disableProperty().unbind();
				automatonTypeComboBox.disableProperty().unbind();

				BooleanBinding isNotSelectedNodeInTree = Bindings.isEmpty(signatureTreeView.getSelectionModel().getSelectedItems());
				BooleanBinding isNotLoadedSignature = Bindings.isNull(fuzzySignature);

				setNodeViewElements(newValue.getValue());
				bindNodeViewElementsToControllerProperties();

				final BooleanBinding aggregationBinding = newValue.leafProperty().or(isNotLoadedSignature).or(isNotSelectedNodeInTree);
				final BooleanBinding automatonBinding = Bindings.not(newValue.leafProperty()).or(isNotLoadedSignature).or(isNotSelectedNodeInTree);

				setComboBoxBindings(aggregationBinding, automatonBinding);
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
							setText(t.getFuzzyAutomatonName() + " : " + t.getUuid());
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
							setText(t.getFuzzyAutomatonName());
						}
					}
				});

		aggregationOperatorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> signatureTreeView.refresh());
		automatonTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> signatureTreeView.refresh());
		nodeDescriptionTextArea.textProperty().addListener((observable, oldValue, newValue) -> signatureTreeView.refresh());
		nodeNameTextField.textProperty().addListener((observable, oldValue, newValue) -> signatureTreeView.refresh());

		bindViewElementsToControllerProperties();

		initGraphVisualization();

		tabPane.getSelectionModel().selectedItemProperty().addListener(
				(observable, oldValue, newValue) -> { showSignatureGraphVisualization(); showCompoundAutomatonGraphVisualization(); });

		signatureViewerAnchorPane.widthProperty().addListener((observable, oldValue, newValue) -> showSignatureGraphVisualization());
		signatureViewerAnchorPane.heightProperty().addListener((observable, oldValue, newValue) -> showSignatureGraphVisualization());

		compoundAutomatonViewerAnchorPane.widthProperty().addListener((observable, oldValue, newValue) -> showCompoundAutomatonGraphVisualization());
		compoundAutomatonViewerAnchorPane.heightProperty().addListener((observable, oldValue, newValue) -> showCompoundAutomatonGraphVisualization());
	}

	private void bindViewElementsToControllerProperties() {

		BooleanBinding isLoadedSignature = Bindings.isNotNull(fuzzySignature);
		BooleanBinding isNotLoadedSignature = Bindings.isNull(fuzzySignature);
		BooleanBinding isNotSelectedNodeInTree = Bindings.isEmpty(signatureTreeView.getSelectionModel().getSelectedItems());

		signatureTreeView.disableProperty().bind(isNotLoadedSignature);
		createSignatureButton.disableProperty().bind(isLoadedSignature);
		clearSignatureButton.disableProperty().bind(isNotLoadedSignature);
		saveSignatureButton.disableProperty().bind(isNotLoadedSignature);
		costVectorConstraintSpinner.disableProperty().bind(isNotLoadedSignature);
		signatureNameTextField.disableProperty().bind(isNotLoadedSignature);
		signatureDescriptionTextArea.disableProperty().bind(isNotLoadedSignature);
		nodeNameTextField.disableProperty().bind(isNotLoadedSignature.or(isNotSelectedNodeInTree));
		nodeDescriptionTextArea.disableProperty().bind(isNotLoadedSignature.or(isNotSelectedNodeInTree));

		setComboBoxBindings(isNotLoadedSignature.or(isNotSelectedNodeInTree), isNotLoadedSignature.or(isNotSelectedNodeInTree));
	}

	private void setComboBoxBindings(BooleanBinding aggregationBinding, BooleanBinding automatonBinding){

		aggregationOperatorComboBox.disableProperty().bind(aggregationBinding);
		automatonTypeComboBox.disableProperty().bind(automatonBinding);
	}

	private void bindSignatureViewElementsToControllerProperties() {

		if(fuzzySignature.getValue() != null){
			signatureNameTextField.textProperty().bindBidirectional(fuzzySignature.getValue().fuzzySignatureNameProperty());
			signatureDescriptionTextArea.textProperty().bindBidirectional(fuzzySignature.getValue().fuzzySignatureDescriptionProperty());
			costVectorConstraintSpinner.getValueFactory().valueProperty().bindBidirectional(fuzzySignature.getValue().costVectorDimensionObjProperty());
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
			costVectorConstraintSpinner.getValueFactory().valueProperty().unbindBidirectional(fuzzySignature.get().costVectorDimensionObjProperty());
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
		signatureTreeView.setRoot(null);
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

		final FuzzyNode rootNode = new FuzzyNode("Node" + getNextNodeCounterValue(), null, null, null, null, null);
		final FuzzySignature fuzzySignature = new FuzzySignature(null, "Signature" + createdSignatureCounter, rootNode, "sample description", 0);

		loadWithData(fuzzySignature);
	}

	@FXML
	private void saveSignature(){

		final Map<WorkspaceInfo, ObservableList<? extends WorkspaceFxRootElementBase>> registeredStores = this.commonServices.getRegisteredStores();
		final WorkspaceInfo workspaceInfo = fuzzySignatureModuleDescriptor.getWorkspaceInfo();
		final ObservableList<? extends WorkspaceFxRootElementBase> workspaceElements = registeredStores.get(workspaceInfo);
		final ObservableList<FuzzySignature> fuzzySignatures = (ObservableList<FuzzySignature>) workspaceElements;

		ShellDialogServices shellDialogServices = new ServiceFactory().getShellDialogServices();
		FuzzySignature fuzzySignature = this.fuzzySignature.get();
		if (fuzzySignature != null){
			final FuzzySignature fs = fuzzySignature.deepCopy();
			String copiedUuid = fs.getUuid();

			FuzzySignature alreadyLoadedSignature = null;
			for (FuzzySignature signature : fuzzySignatures) {
				String checkedUuid = signature.getUuid();
				if (checkedUuid.equals(copiedUuid)) {
					alreadyLoadedSignature = signature;
				}
			}

			if (alreadyLoadedSignature != null ){
				int choice = shellDialogServices.selectFromOptions(
						"Model conflict",
						"This model has an original in model store.",
						"Would you like to overwrite it, or create a new one instead?",
						"Overwrite", "Create new");
				if (choice == 1){
					commonServices.updateModelInRegisteredStore(alreadyLoadedSignature, fs);
				} else if (choice == 2){
					fs.setUuid(CommonUtils.initializeUUIDPropertyFromString(null).get());
					commonServices.addModelToRegisteredStore(fs);
				}
			} else {
				commonServices.addModelToRegisteredStore(fs);
			}
		}
	}

	@FXML
	public void clearSignature(){

		clearSignatureViewElements();
		clearNodeViewElements();
	}

	@FXML
	private void loadActualFuzzyAutomatons(){

		final Map<WorkspaceInfo, ObservableList<? extends WorkspaceFxRootElementBase>> registeredStores = this.commonServices.getRegisteredStores();
		final WorkspaceInfo workspaceInfo = fuzzyAutomatonModuleDescriptor.getWorkspaceInfo();
		final ObservableList<? extends WorkspaceFxRootElementBase> workspaceElements = registeredStores.get(workspaceInfo);

		final ObservableList<FuzzyAutomaton> fuzzyAutomatons = (ObservableList<FuzzyAutomaton>) workspaceElements;
		this.automatonTypeComboBox.setItems(FXCollections.observableArrayList(fuzzyAutomatons));
	}

	public int getNextNodeCounterValue(){

		if (createdSignatureCounter != latestCreatedSignatureCounterValue) {
			createdNodeCounter = 0;
			latestCreatedSignatureCounterValue = createdSignatureCounter;
		}
		createdNodeCounter++;
		return createdNodeCounter - 1;
	}

	private void initGraphVisualization() {

		final URL uri1 = getClass().getResource("SignatureVisualizer.html");
		final URL uri2 = getClass().getResource("CompoundAutomatonVisualizer.html");
		signatureViewer.getEngine().load(uri1.toString());
		compoundAutomatonViewer.getEngine().load(uri2.toString());
		//automaton_viewer.setZoom(javafx.stage.Screen.getPrimary().getDpi() / 96);
	}

	private int signatureJsGuiWebViewWidth = 0;
	private int signatureJsGuiWebViewHeight = 0;
	private int signatureJsGuiWebViewPadding = 0;
	private int automatonJsGuiWebViewWidth = 0;
	private int automatonJsGuiWebViewHeight = 0;
	private int automatonJsGuiWebViewPadding = 0;

	private void updateSignatureJsGuiWebViewSizes(){

		signatureJsGuiWebViewWidth = (int) signatureViewer.getWidth() - 20;
		signatureJsGuiWebViewHeight = (int) signatureViewer.getHeight() - 20;
		signatureJsGuiWebViewPadding = 20;
	}

	private void updateAutomatonJsGuiWebViewSizes(){

		automatonJsGuiWebViewWidth = (int) compoundAutomatonViewer.getWidth() - 20;
		automatonJsGuiWebViewHeight = (int) compoundAutomatonViewer.getHeight() - 20;
		automatonJsGuiWebViewPadding = 20;
	}

	private void showSignatureGraphVisualization() {

		if (fuzzySignature != null && fuzzySignature.getValue() != null) {

			updateSignatureJsGuiWebViewSizes();
			String jsonData = GraphVisualizationsHelperUtil.generateSignatureJsonForJavascriptGui(fuzzySignature.getValue());

			signatureViewer.getEngine().executeScript("initialize('" + jsonData + "', " + signatureJsGuiWebViewWidth + ", " + signatureJsGuiWebViewHeight + ", " + signatureJsGuiWebViewPadding + ");");
			signatureViewer.getEngine().executeScript("visualize();");
		}
	}

	private void showCompoundAutomatonGraphVisualization() {

		if (selectedCompoundNode != null){
			updateAutomatonJsGuiWebViewSizes();

			String jsonStatesVisualization = GraphVisualizationsHelperUtil.generateStatesJsonForJavascriptGui(selectedCompoundNode);
			String jsonTransitionsVisualization = GraphVisualizationsHelperUtil.generateTransitionsJsonForJavascriptGui(selectedCompoundNode);

			compoundAutomatonViewer.getEngine().executeScript("initialize('" + jsonStatesVisualization + "','" + jsonTransitionsVisualization + "', " + automatonJsGuiWebViewWidth + ", " + automatonJsGuiWebViewHeight + ", " + automatonJsGuiWebViewPadding + ");");
			compoundAutomatonViewer.getEngine().executeScript("visualize();");
		}
	}

    /*
     * Implemented interfaces
     */

	@Override
	public <T extends WorkspaceFxRootElementBase> void loadWithData(T modelToLoad) {

		unbindSignatureViewElementsFromControllerProperties();
		clearSignatureViewElements();

		if (modelToLoad == null)
			return;

		final FuzzySignature signatureToLoad = ((FuzzySignature) modelToLoad).deepCopy();
		final FuzzyNode rootNodeOfTheTree = signatureToLoad.getRootNodeOfTheTree();
		TreeItem<FuzzyNode> root = loadTreeRecursively(rootNodeOfTheTree, null);
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
		bindSignatureViewElementsToControllerProperties();

		showSignatureGraphVisualization();
	}

	private TreeItem<FuzzyNode> loadTreeRecursively(FuzzyNode node, TreeItem<FuzzyNode> parentItem){

		final TreeItem<FuzzyNode> fuzzyNodeTreeItem = new TreeItem<>(node);
		for (FuzzyNode fuzzyNode : node.getChildNodes()) {
			loadTreeRecursively(fuzzyNode, fuzzyNodeTreeItem);
		}
		if (parentItem != null)
			parentItem.getChildren().add(fuzzyNodeTreeItem);
		fuzzyNodeTreeItem.setExpanded(true);
		return fuzzyNodeTreeItem;
	}

	private CompoundFuzzyAutomaton selectedCompoundNode = null;

	public void showCompoundAutomaton(FuzzyNode thisNode) {

		if (thisNode == null)
			throw new RuntimeException("Error in Signature's UI logic.");

		CompoundFuzzyAutomaton cartesianProduct = null;
		try {
			cartesianProduct = thisNode.generateRecursivelyCartesianProductOfAutomatons();

		} catch (ModelError modelError) {

			new ServiceFactory().getShellDialogServices()
					.informWarningDialog("ModelError", "Error while generating cartesian product", modelError.getMessage());
		}
		if (cartesianProduct != null){

			selectedCompoundNode = cartesianProduct;
			tabPane.getSelectionModel().select(2);
		}
	}

}