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
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

import java.util.Map;

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
	private Spinner<Integer> costVectorDimensionSpinner;

	@FXML
	private Spinner<Double> costValueSpinner;

	@FXML
	private RadioButton initialStateRadioButton;

	@FXML
	private RadioButton normalStateRadioButton;

	@FXML
	private RadioButton terminalStateRadioButton;

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
				if(fsmdv.getViewName().equals("Fuzzy Sets")){
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

	}


	void bindViewElementsToControllerProperties() {

		BooleanBinding isNotLoadedAutomaton = Bindings.isNull(fuzzyAutomaton);
		BooleanBinding isStateNotSetSelectedForEditing = Bindings.isNull(fuzzyStateToEdit);
		BooleanBinding isTransitionNotSetSelectedForEditing = Bindings.isNull(fuzzyTransitionToEdit);
		BooleanBinding isInEditingMode = Bindings.isNotNull(fuzzyStateToEdit).or(Bindings.isNotNull(fuzzyTransitionToEdit));
		BooleanBinding isNothingSelectedForEditing = isStateNotSetSelectedForEditing.and(isTransitionNotSetSelectedForEditing);
		BooleanBinding isSelectedState = Bindings.isNull(statesListView.getSelectionModel().selectedItemProperty());
		BooleanBinding isSelectedTransition = Bindings.isNull(transitionsListView.getSelectionModel().selectedItemProperty());

		createAutomatonButton.disableProperty().bind(isInEditingMode);

		automatonNameTextField.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		automatonDescriptionTextArea.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		statesListView.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		transitionsListView.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		clearAutomatonButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		saveAutomatonButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		fuzzySetSystemComboBox.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		costVectorDimensionSpinner.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		addStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));
		addTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode));

		editStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedState));
		removeStateButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedState));
		editTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedTransition));
		removeTransitionButton.disableProperty().bind(isNotLoadedAutomaton.or(isInEditingMode).or(isSelectedTransition));

		stateOrTransitionNameTextField.disableProperty().bind(isNothingSelectedForEditing);
		stateOrTransitionDescriptionTextArea.disableProperty().bind(isNothingSelectedForEditing);
		saveButton.disableProperty().bind(isNothingSelectedForEditing);

		initialStateRadioButton.disableProperty().bind(isStateNotSetSelectedForEditing);
		normalStateRadioButton.disableProperty().bind(isStateNotSetSelectedForEditing);
		terminalStateRadioButton.disableProperty().bind(isStateNotSetSelectedForEditing);
		stateFuzzySetComboBox.disableProperty().bind(isStateNotSetSelectedForEditing);

		addCostValueButton.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		removeCostValueButton.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		fromStateComboBox.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		toStateComboBox.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		costsListView.disableProperty().bind(isTransitionNotSetSelectedForEditing);
		costValueSpinner.disableProperty().bind(isTransitionNotSetSelectedForEditing);

	}

	void unbindAutomatonViewElementsFromControllerProperties(){

		if (fuzzyAutomaton.getValue() != null){
			automatonNameTextField.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomationNameProperty());
			automatonDescriptionTextArea.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomatonDescriptionProperty());

			statesListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyStatesProperty());
			transitionsListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyTransitionsProperty());
		}
	}

	void unbindStateViewElementsFromControllerProperties(){

		if (fuzzyStateToEdit.getValue() != null) {
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzyStateNameProperty());
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyStateToEdit.get().fuzzyStateDescriptionProperty());
		}
	}

	void unbindTransitionViewElementsFromControllerProperties(){

		if (fuzzyTransitionToEdit.getValue() != null) {
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionNameProperty());
			stateOrTransitionNameTextField.textProperty().unbindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionDescriptionProperty());
		}
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
	}

	@FXML
	private void createAutomatonButton(){

		createdAutomatonCounter++;
		loadWithData(new FuzzyAutomaton(null, "automaton" + Integer.toString(createdAutomatonCounter),
				"That's a custom fuzzy automaton", null, null, null));
		this.originallyLoadedFuzzyAutomaton = null;
	}

	@FXML
	private void clearAutomaton(){

		unbindAutomatonViewElementsFromControllerProperties();
		this.fuzzyAutomaton.setValue(null);
		automatonNameTextField.textProperty().set(null);
		automatonDescriptionTextArea.textProperty().set(null);
		this.originallyLoadedFuzzyAutomaton = null;
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
	}

	@FXML
	private void editState(){

		ObservableList<FuzzyState> selectedItems = statesListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() != 1)
			return;

		fuzzyStateToEdit.setValue(selectedItems.get(0));
		stateOrTransitionNameTextField.textProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzyStateNameProperty());
		stateOrTransitionDescriptionTextArea.textProperty().bindBidirectional(fuzzyStateToEdit.get().fuzzyStateDescriptionProperty());
	}

	@FXML
	private void removeState(){

		ObservableList<FuzzyState> selectedItems = statesListView.getSelectionModel().getSelectedItems();
		fuzzyAutomaton.get().fuzzyStatesProperty().removeAll(selectedItems);
	}

	@FXML
	private void addTransition(){

		if (fuzzyAutomaton != null && fuzzyAutomaton.get() != null && fuzzyAutomaton.get().fuzzyTransitionsProperty() != null) {
			unbindAutomatonViewElementsFromControllerProperties();
			bindViewElementsToControllerProperties();
			createdTransitionCounter++;
			fuzzyAutomaton.get().fuzzyTransitionsProperty().add(new FuzzyTransition("transition" + createdTransitionCounter, null));
		}
	}

	@FXML
	private void editTransition(){

		ObservableList<FuzzyTransition> selectedItems = transitionsListView.getSelectionModel().getSelectedItems();
		if (selectedItems.size() != 1)
			return;

		fuzzyTransitionToEdit.setValue(selectedItems.get(0));
		stateOrTransitionNameTextField.textProperty().bindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionNameProperty());
		stateOrTransitionDescriptionTextArea.textProperty().bindBidirectional(fuzzyTransitionToEdit.get().fuzzyTransitionDescriptionProperty());
	}

	@FXML
	private void removeTransition(){

		ObservableList<FuzzyTransition> selectedItems = transitionsListView.getSelectionModel().getSelectedItems();
		fuzzyAutomaton.get().fuzzyTransitionsProperty().removeAll(selectedItems);
	}

	@FXML
	private void addCostValue(){

	}

	@FXML
	private void removeCostValue(){

	}

	@FXML
	private void saveButtonPressed(){

		unbindStateViewElementsFromControllerProperties();
		unbindTransitionViewElementsFromControllerProperties();
		fuzzyStateToEdit.setValue(null);
		fuzzyTransitionToEdit.setValue(null);
		stateOrTransitionNameTextField.textProperty().set(null);
		stateOrTransitionDescriptionTextArea.textProperty().set(null);
	}

	@FXML
	private void loadActualFuzzySetSystems(){

		final Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> registeredStores = this.commonServices.getRegisteredStores();
		final WorkspaceInfo workspaceInfo = fuzzySetModuleDescriptor.getWorkspaceInfo();
		final ObservableList<? extends WorkspaceElement> workspaceElements = registeredStores.get(workspaceInfo);

		final ObservableList<FuzzySetSystem> fuzzySetSystems = (ObservableList<FuzzySetSystem>) workspaceElements;
		this.fuzzySetSystemComboBox.setItems(FXCollections.observableArrayList(fuzzySetSystems));
		fuzzySetSystemComboBox.valueProperty().bind(fuzzyAutomaton.get().fuzzySetSystemProperty());
	}

}
