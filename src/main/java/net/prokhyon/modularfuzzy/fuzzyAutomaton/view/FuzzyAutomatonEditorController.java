package net.prokhyon.modularfuzzy.fuzzyAutomaton.view;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class FuzzyAutomatonEditorController implements LoadableDataController {

	private CommonServices commonServices;

	private FuzzySetModuleDescriptor fuzzySetModuleDescriptor;

	private ObjectProperty<FuzzyAutomaton> fuzzyAutomaton;

	private ObjectProperty<FuzzyState> fuzzyStateToEdit;

	private ObjectProperty<FuzzyTransition> fuzzyTransitionToEdit;

	private FuzzyAutomaton originallyLoadedFuzzyAutomaton;

	@FXML
	private TextField automatonNameTextField;

	@FXML
	private TextArea automatonDescriptionTextArea;

	@FXML
	private TextField stateOrTransitionNameTextField;

	@FXML
	private TextArea stateOrTransitionDescriptionTextArea;

	@FXML
	private ListView<FuzzyState> statesListView;

	@FXML
	private ListView<FuzzyTransition> transitionsListView;

	@FXML
	private ListView<Double> costsListView;

	@FXML
	private ComboBox<FuzzySetSystem> fuzzySetSystemComboBox;

	@FXML
	private ComboBox<FuzzyState> fromStateComboBox;

	@FXML
	private ComboBox<FuzzyState> toStateComboBox;

	@FXML
	private ComboBox<FuzzySet> stateFuzzySetComboBox;

	@FXML
	private ComboBox<FuzzyStateTypeEnum> stateTypeComboBox;

	@FXML
	private Spinner<Integer> costVectorDimensionSpinner;

	@FXML
	private Spinner<Double> costValueSpinner;

	@FXML
	private Button createAutomatonButton;

	@FXML
	private Button clearAutomatonButton;

	@FXML
	private Button saveAutomatonButton;

	@FXML
	private Button addStateButton;

	@FXML
	private Button editStateButton;

	@FXML
	private Button removeStateButton;

	@FXML
	private Button addTransitionButton;

	@FXML
	private Button editTransitionButton;

	@FXML
	private Button removeTransitionButton;

	@FXML
	private Button addCostValueButton;

	@FXML
	private Button removeCostValueButton;

	@FXML
	private Button moveSelectedCostValueUp;

	@FXML
	private Button moveSelectedCostValueDown;

	@FXML
	private Button saveButton;

	private int createdAutomatonCounter;
	private int createdStateCounter;
	private int createdTransitionCounter;


	@FXML
	private void initialize() {

		this.commonServices = new ServiceFactory().getCommonServices();
		final Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = this.commonServices.getPseudoModules();
		for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> classModuleDescriptorEntry : pseudoModules.entrySet()) {
			final Class<? extends ModuleDescriptor> key = classModuleDescriptorEntry.getKey();
			final ModuleDescriptor value = classModuleDescriptorEntry.getValue();
			if (key == FuzzySetModuleDescriptor.class ) {
				final FuzzySetModuleDescriptor fsmdv = (FuzzySetModuleDescriptor) value;
				if(fsmdv.getViewName().equals("Sets")){
					this.fuzzySetModuleDescriptor = fsmdv;
				}
			}
		}

		this.fuzzyAutomaton = new SimpleObjectProperty<>();
		this.fuzzyStateToEdit = new SimpleObjectProperty<>();
		this.fuzzyTransitionToEdit = new SimpleObjectProperty<>();

		bindViewElementsToControllerProperties();

		this.createdAutomatonCounter = 0;
		this.createdStateCounter = 0;
		this.createdTransitionCounter = 0;

		costVectorDimensionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE));
		costValueSpinner.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(Double.MIN_VALUE, Double.MAX_VALUE, 0.0, 0.1));
		costVectorDimensionSpinner.setEditable(true);
		costValueSpinner.setEditable(true);

		fuzzySetSystemComboBox.setCellFactory(new Callback<ListView<FuzzySetSystem>,ListCell<FuzzySetSystem>>(){

			@Override
			public ListCell<FuzzySetSystem> call(ListView<FuzzySetSystem> p) {

				final ListCell<FuzzySetSystem> cell = new ListCell<FuzzySetSystem>(){

					@Override
					protected void updateItem(FuzzySetSystem t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null)
							setText(t.getFuzzySystemName() + " : " + t.getUuid());
						else
							setText(null);
					}
				};
				return cell;
			}
		});

		fuzzySetSystemComboBox.setButtonCell(
				new ListCell<FuzzySetSystem>() {
					@Override
					protected void updateItem(FuzzySetSystem t, boolean bln) {
						super.updateItem(t, bln);
						if (bln) {
							setText("");
						} else {
							setText(t.getFuzzySystemName());
						}
					}
				});

		statesListView.setCellFactory(new Callback<ListView<FuzzyState>,ListCell<FuzzyState>>(){

			@Override
			public ListCell<FuzzyState> call(ListView<FuzzyState> p) {

				final ListCell<FuzzyState> cell = new ListCell<FuzzyState>(){

					@Override
					protected void updateItem(FuzzyState t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							String str = null;
							try {
								str = t.getFuzzySet().getFuzzySetName();
							} catch (Exception e) {}
							setText(t.getFuzzyStateName() + " : " + str);
						} else
							setText(null);
					}
				};
				return cell;
			}
		});

		transitionsListView.setCellFactory(new Callback<ListView<FuzzyTransition>,ListCell<FuzzyTransition>>(){

			@Override
			public ListCell<FuzzyTransition> call(ListView<FuzzyTransition> p) {

				final ListCell<FuzzyTransition> cell = new ListCell<FuzzyTransition>(){

					@Override
					protected void updateItem(FuzzyTransition t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							String states = "(";
							if (t.getFromState() != null) states += t.getFromState().getFuzzyStateName();
							states += "->";
							if (t.getToState() != null)	states += t.getToState().getFuzzyStateName();
							states += ")";
							final String costs =  "[" + t.getCostVector().stream().map(d -> d.toString()).collect(Collectors.joining(", ")) + "]";
							//final List<String> costParts = Arrays.stream(t.getCostVector().toArray()).map(s -> s.toString()).collect(Collectors.toList());
							//final String costs = "[" + String.join(",", costParts) + "]";

							setText(t.getFuzzyTransitionName() + " : " + states + " : "  + costs );
						} else
							setText(null);
					}
				};
				return cell;
			}
		});

		statesListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

			refreshStateEditorFields(newSelection);
		});

		transitionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {

			refreshTransitionEditorFields(newSelection);
		});

		final Callback<ListView<FuzzyState>, ListCell<FuzzyState>> callback = new Callback<ListView<FuzzyState>, ListCell<FuzzyState>>() {

			@Override
			public ListCell<FuzzyState> call(ListView<FuzzyState> p) {

				final ListCell<FuzzyState> cell = new ListCell<FuzzyState>() {

					@Override
					protected void updateItem(FuzzyState t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getFuzzyStateName());
						} else
							setText(null);
					}
				};
				return cell;
			}
		};
		fromStateComboBox.setCellFactory(callback);
		toStateComboBox.setCellFactory(callback);
		fromStateComboBox.setButtonCell(getListCell());
		toStateComboBox.setButtonCell(getListCell());

		stateFuzzySetComboBox.setCellFactory(new Callback<ListView<FuzzySet>,ListCell<FuzzySet>>(){

			@Override
			public ListCell<FuzzySet> call(ListView<FuzzySet> p) {

				final ListCell<FuzzySet> cell = new ListCell<FuzzySet>(){

					@Override
					protected void updateItem(FuzzySet t, boolean bln) {
						super.updateItem(t, bln);
						if (t != null) {
							setText(t.getFuzzySetName());
						} else
							setText(null);
					}
				};
				return cell;
			}
		});

		stateFuzzySetComboBox.setButtonCell(
				new ListCell<FuzzySet>() {
					@Override
					protected void updateItem(FuzzySet t, boolean bln) {
						super.updateItem(t, bln);
						if (bln) {
							setText("");
						} else {
							setText(t.getFuzzySetName());
						}
					}
				});

	}

	private void clearStateOrTransitionEditingFields() {

		stateOrTransitionNameTextField.textProperty().set(null);
		stateOrTransitionDescriptionTextArea.textProperty().set(null);
		stateTypeComboBox.valueProperty().setValue(null);
		stateFuzzySetComboBox.valueProperty().setValue(null);
		fromStateComboBox.valueProperty().set(null);
		toStateComboBox.valueProperty().set(null);
		costsListView.itemsProperty().set(null);
	}

	private ListCell<FuzzyState> getListCell() {

		return new ListCell<FuzzyState>() {
			@Override
			protected void updateItem(FuzzyState t, boolean bln) {
				super.updateItem(t, bln);
				if (bln) {
					setText("");
				} else {
					setText(t.getFuzzyStateName());
				}
			}
		};
	}


	void bindViewElementsToControllerProperties() {

		BooleanBinding isNotLoadedAutomaton = Bindings.isNull(fuzzyAutomaton);
		BooleanBinding isStateNotSetSelectedForEditing = Bindings.isNull(fuzzyStateToEdit);
		BooleanBinding isTransitionNotSetSelectedForEditing = Bindings.isNull(fuzzyTransitionToEdit);
		BooleanBinding isInEditingMode = Bindings.isNotNull(fuzzyStateToEdit).or(Bindings.isNotNull(fuzzyTransitionToEdit));
		BooleanBinding isNothingSelectedForEditing = isStateNotSetSelectedForEditing.and(isTransitionNotSetSelectedForEditing);
		BooleanBinding isSelectedState = Bindings.isNull(statesListView.getSelectionModel().selectedItemProperty());
		BooleanBinding isSelectedTransition = Bindings.isNull(transitionsListView.getSelectionModel().selectedItemProperty());
		BooleanBinding isStateOrTransitionCreated = Bindings.isNotEmpty(transitionsListView.getItems()).or(Bindings.isNotEmpty(statesListView.getItems()));
		BooleanBinding isSetSystemNotSelected = Bindings.isNull(fuzzySetSystemComboBox.getSelectionModel().selectedItemProperty());
		BooleanBinding isBadDimension = Bindings.when(costVectorDimensionSpinner.valueProperty().isEqualTo(new Integer(0))).then(true).otherwise(false);
		BooleanBinding isCostVectorComponentNotSelected = Bindings.isEmpty(costsListView.getSelectionModel().getSelectedItems());

		createAutomatonButton.disableProperty().bind(isInEditingMode);

		automatonNameTextField.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		automatonDescriptionTextArea.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		statesListView.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		transitionsListView.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		clearAutomatonButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		saveAutomatonButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		fuzzySetSystemComboBox.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isStateOrTransitionCreated));
		costVectorDimensionSpinner.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		addStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSetSystemNotSelected).or(isBadDimension));
		addTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSetSystemNotSelected).or(isBadDimension));

		editStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedState));
		removeStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedState));
		editTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedTransition));
		removeTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedTransition));

		stateOrTransitionNameTextField.disableProperty().bind(isNothingSelectedForEditing);
		stateOrTransitionDescriptionTextArea.disableProperty().bind(isNothingSelectedForEditing);
		saveButton.disableProperty().bind(isNothingSelectedForEditing);

		stateTypeComboBox.disableProperty().bind(isStateNotSetSelectedForEditing);
		stateFuzzySetComboBox.disableProperty().bind(isStateNotSetSelectedForEditing);

		addCostValueButton.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		removeCostValueButton.disableProperty().bind(isTransitionNotSetSelectedForEditing.or(isCostVectorComponentNotSelected));
		moveSelectedCostValueUp.disableProperty().bind(isTransitionNotSetSelectedForEditing.or(isCostVectorComponentNotSelected));
		moveSelectedCostValueDown.disableProperty().bind(isTransitionNotSetSelectedForEditing.or(isCostVectorComponentNotSelected));
		fromStateComboBox.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		toStateComboBox.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		costsListView.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		costValueSpinner.disableProperty().bind(isTransitionNotSetSelectedForEditing);
	}

	void unbindAutomatonViewElementsFromControllerProperties(){

		if (fuzzyAutomaton.getValue() != null){
			automatonNameTextField.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomationNameProperty());
			automatonDescriptionTextArea.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomatonDescriptionProperty());
			costVectorDimensionSpinner.getValueFactory().valueProperty().unbindBidirectional(fuzzyAutomaton.get().costVectorDimensionObjProperty());

			statesListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyStatesProperty());
			transitionsListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyTransitionsProperty());
		}
	}

	void unbindStateViewElementsFromControllerProperties(){

		if (fuzzyStateToEdit.getValue() != null) {
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzyStateNameProperty());
			stateOrTransitionDescriptionTextArea.textProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzyStateDescriptionProperty());

			stateTypeComboBox.valueProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzyStateTypeProperty());
			stateFuzzySetComboBox.valueProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzySetProperty());
		}
	}

	void unbindTransitionViewElementsFromControllerProperties(){

		if (fuzzyTransitionToEdit.getValue() != null) {
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionNameProperty());
			stateOrTransitionDescriptionTextArea.textProperty().unbindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionDescriptionProperty());

			fromStateComboBox.valueProperty().unbindBidirectional(fuzzyTransitionToEdit.get().fromStateProperty());
			toStateComboBox.valueProperty().unbindBidirectional(fuzzyTransitionToEdit.get().toStateProperty());
			costsListView.itemsProperty().unbindBidirectional(fuzzyTransitionToEdit.get().costVectorProperty());
		}
	}

	void updateTransitionComboBox(){

		final ObservableList<FuzzyState> fuzzyStates = fuzzyAutomaton.get().fuzzyStatesProperty();
		this.fromStateComboBox.setItems(FXCollections.observableArrayList(fuzzyStates));
		this.toStateComboBox.setItems(FXCollections.observableArrayList(fuzzyStates));
	}

	@Override
	public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

		clearAutomaton();
		if (modelToLoad == null)
			return;

		this.originallyLoadedFuzzyAutomaton = (FuzzyAutomaton) modelToLoad;
		FuzzyAutomaton fa = ((FuzzyAutomaton)modelToLoad).deepCopy();
		this.fuzzyAutomaton.set(fa);

		automatonNameTextField.textProperty().bindBidirectional(fuzzyAutomaton.get().fuzzyAutomationNameProperty());
		automatonDescriptionTextArea.textProperty().bindBidirectional(fuzzyAutomaton.get().fuzzyAutomatonDescriptionProperty());
		statesListView.itemsProperty().bindBidirectional(fuzzyAutomaton.get().fuzzyStatesProperty());
		transitionsListView.itemsProperty().bindBidirectional(fuzzyAutomaton.get().fuzzyTransitionsProperty());

		costVectorDimensionSpinner.getValueFactory().valueProperty().bindBidirectional(fuzzyAutomaton.get().costVectorDimensionObjProperty());
	}

	@FXML
	private void createAutomaton(){

		createdAutomatonCounter++;
		loadWithData(new FuzzyAutomaton(null, "automaton" + Integer.toString(createdAutomatonCounter),
				"That's a custom fuzzy automaton", null, null, null, 0));
		this.originallyLoadedFuzzyAutomaton = null;
	}

	@FXML
	private void clearAutomaton(){

		unbindAutomatonViewElementsFromControllerProperties();
		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();

		this.fuzzyAutomaton.setValue(null);
		this.fuzzyStateToEdit.setValue(null);
		this.fuzzyTransitionToEdit.setValue(null);
		this.originallyLoadedFuzzyAutomaton = null;

		automatonNameTextField.textProperty().set(null);
		automatonDescriptionTextArea.textProperty().set(null);
		fuzzySetSystemComboBox.getItems().clear();
		stateTypeComboBox.getItems().clear();
		stateFuzzySetComboBox.getItems().clear();
		fromStateComboBox.getItems().clear();
		toStateComboBox.getItems().clear();
		statesListView.getItems().clear();
		transitionsListView.getItems().clear();
		// TODO costsListView.getItems().clear();
	}

	@FXML
	private void saveAutomaton(){

		ShellDialogServices shellDialogServices = new ServiceFactory().getShellDialogServices();
		FuzzyAutomaton fuzzyAutomaton = this.fuzzyAutomaton.get();
		if (fuzzyAutomaton != null){
			final FuzzyAutomaton fa = fuzzyAutomaton.deepCopy();
			if (this.originallyLoadedFuzzyAutomaton != null ){
				int choice = shellDialogServices.selectFromOptions(
						"Model conflict",
						"This model has an original in model store.",
						"Would you like to overwrite it, or create a new one instead?",
						"Overwrite", "Create new");
				if (choice == 1){
					commonServices.updateModelInRegisteredStore(this.originallyLoadedFuzzyAutomaton, fa);
				} else if (choice == 2){
					commonServices.addModelToRegisteredStore(fa);
				}
			} else {
				commonServices.addModelToRegisteredStore(fa);
				this.originallyLoadedFuzzyAutomaton = fa;
			}
		}
	}

	@FXML
	private void addState(){

		if (fuzzyAutomaton != null && fuzzyAutomaton.get() != null && fuzzyAutomaton.get().fuzzyStatesProperty() != null) {
			unbindAutomatonViewElementsFromControllerProperties();
			bindViewElementsToControllerProperties();
			createdStateCounter++;
			fuzzyAutomaton.get().fuzzyStatesProperty().add(new FuzzyState("state" + createdStateCounter, null, null, null));
		}
		updateTransitionComboBox();
	}

	@FXML
	private void editState(){

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();

		ObservableList<FuzzyState> selectedItems = statesListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() != 1)
			return;

		fuzzyStateToEdit.setValue(selectedItems.get(0));
		stateOrTransitionNameTextField.textProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzyStateNameProperty());
		stateOrTransitionDescriptionTextArea.textProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzyStateDescriptionProperty());

		stateTypeComboBox.setItems(FXCollections.observableArrayList(FuzzyStateTypeEnum.values()));
		stateTypeComboBox.valueProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzyStateTypeProperty());

		stateFuzzySetComboBox.setItems(FXCollections.observableArrayList(this.fuzzyAutomaton.get().fuzzySetSystemProperty().get().fuzzySetsProperty()));
		stateFuzzySetComboBox.valueProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzySetProperty());
	}

	@FXML
	private void removeState(){

		ObservableList<FuzzyState> selectedItems = statesListView.getSelectionModel().getSelectedItems();
		fuzzyAutomaton.get().fuzzyStatesProperty().removeAll(selectedItems);
		updateTransitionComboBox();
	}

	@FXML
	private void addTransition(){

		if (fuzzyAutomaton != null && fuzzyAutomaton.get() != null && fuzzyAutomaton.get().fuzzyTransitionsProperty() != null) {
			unbindAutomatonViewElementsFromControllerProperties();
			bindViewElementsToControllerProperties();
			createdTransitionCounter++;
			fuzzyAutomaton.get().fuzzyTransitionsProperty().add(new FuzzyTransition("transition" + createdTransitionCounter, null, null, null, null));
		}
	}

	@FXML
	private void editTransition(){

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();

		ObservableList<FuzzyTransition> selectedItems = transitionsListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() != 1)
			return;

		fuzzyTransitionToEdit.setValue(selectedItems.get(0));
		stateOrTransitionNameTextField.textProperty().bindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionNameProperty());
		stateOrTransitionDescriptionTextArea.textProperty().bindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionDescriptionProperty());

		fromStateComboBox.setItems(FXCollections.observableArrayList(fuzzyAutomaton.get().getFuzzyStates()));
		fromStateComboBox.valueProperty().bindBidirectional(fuzzyTransitionToEdit.get().fromStateProperty());
		toStateComboBox.setItems(FXCollections.observableArrayList(fuzzyAutomaton.get().getFuzzyStates()));
		toStateComboBox.valueProperty().bindBidirectional(fuzzyTransitionToEdit.get().toStateProperty());
		costsListView.itemsProperty().bindBidirectional(fuzzyTransitionToEdit.get().costVectorProperty());
	}

	@FXML
	private void removeTransition(){

		ObservableList<FuzzyTransition> selectedItems = transitionsListView.getSelectionModel().getSelectedItems();
		fuzzyAutomaton.get().fuzzyTransitionsProperty().removeAll(selectedItems);
	}

	@FXML
	private void addCostValue(){

		fuzzyTransitionToEdit.getValue().getCostVector().add(costValueSpinner.getValue().doubleValue());
	}

	@FXML
	private void removeCostValue(){

		fuzzyTransitionToEdit.get().getCostVector().remove(costsListView.getSelectionModel().getSelectedIndices().get(0).intValue());
	}

	@FXML
	private void moveCostValueUp(){

		final int actualIndex = costsListView.getSelectionModel().getSelectedIndices().get(0).intValue();
		if (actualIndex > 0){
			final int newIndex = actualIndex - 1;
			Collections.swap(fuzzyTransitionToEdit.get().getCostVector(), actualIndex, newIndex);
			costsListView.getSelectionModel().select(newIndex);
		}
	}

	@FXML
	private void moveCostValueDown(){

		final int actualIndex = costsListView.getSelectionModel().getSelectedIndices().get(0).intValue();
		final int maxIndex = costsListView.getItems().size() - 1;
		if (actualIndex < maxIndex){
			final int newIndex = actualIndex + 1;
			Collections.swap(fuzzyTransitionToEdit.get().getCostVector(), actualIndex, newIndex);
			costsListView.getSelectionModel().select(newIndex);
		}
	}

	@FXML
	private void saveButtonPressed(){

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();
		fuzzyStateToEdit.setValue(null);
		fuzzyTransitionToEdit.setValue(null);
		stateOrTransitionNameTextField.textProperty().set(null);
		stateOrTransitionDescriptionTextArea.textProperty().set(null);

		this.forceListRefreshOn(statesListView);
		this.forceListRefreshOn(transitionsListView);
	}

	@FXML
	private void loadActualFuzzySetSystems(){

		final Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = this.commonServices.getRegisteredStores();
		final WorkspaceInfo workspaceInfo = fuzzySetModuleDescriptor.getWorkspaceInfo();
		final ObservableList<? extends WorkspaceElement> workspaceElements = registeredStores.get(workspaceInfo);

		final ObservableList<FuzzySetSystem> fuzzySetSystems = (ObservableList<FuzzySetSystem>) workspaceElements;
		this.fuzzySetSystemComboBox.setItems(FXCollections.observableArrayList(fuzzySetSystems));
		this.fuzzySetSystemComboBox.valueProperty().bindBidirectional(fuzzyAutomaton.get().fuzzySetSystemProperty());
	}

	// TODO This is a quick hack. An extractor based solution should be used here
	private <T> void forceListRefreshOn(ListView<T> lsv) {
		ObservableList<T> items = lsv.<T>getItems();
		lsv.<T>setItems(null);
		lsv.<T>setItems(items);
	}

	@FXML
	private void statesListViewGetsFocus(){

		final FuzzyState selectedItem = statesListView.getSelectionModel().getSelectedItem();
		refreshStateEditorFields(selectedItem);
	}

	@FXML
	private void transitionsListViewGetsFocus(){

		final FuzzyTransition selectedItem = transitionsListView.getSelectionModel().getSelectedItem();
		refreshTransitionEditorFields(selectedItem);
	}

	private void refreshStateEditorFields(FuzzyState newSelection) {

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();
		clearStateOrTransitionEditingFields();
		if (newSelection != null){
			stateOrTransitionNameTextField.textProperty().set(newSelection.getFuzzyStateName());
			stateOrTransitionDescriptionTextArea.textProperty().set(newSelection.getFuzzyStateDescription());
			stateTypeComboBox.valueProperty().setValue(newSelection.getFuzzyStateType());
			stateFuzzySetComboBox.valueProperty().setValue(newSelection.getFuzzySet());
		}
	}

	private void refreshTransitionEditorFields(FuzzyTransition newSelection) {

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();
		clearStateOrTransitionEditingFields();
		if (newSelection != null){
			stateOrTransitionNameTextField.textProperty().set(newSelection.getFuzzyTransitionName());
			stateOrTransitionDescriptionTextArea.textProperty().set(newSelection.getFuzzyTransitionDescription());
			fromStateComboBox.valueProperty().setValue(newSelection.getFromState());
			toStateComboBox.valueProperty().setValue(newSelection.getToState());
			costsListView.itemsProperty().setValue(newSelection.getCostVector());
		}
	}

}
