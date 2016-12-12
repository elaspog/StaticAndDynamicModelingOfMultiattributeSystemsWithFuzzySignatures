package net.prokhyon.modularfuzzy.fuzzyAutomaton.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;
import net.prokhyon.modularfuzzy.shell.services.ShellDialogServices;

public class FuzzyAutomatonEditorController implements LoadableDataController {

	private ObjectProperty<FuzzyAutomaton> fuzzyAutomaton;

	private ObjectProperty<FuzzyState> fuzzyStateToEdit;

	private ObjectProperty<FuzzyTransition> fuzzyTransitionToEdit;

	private FuzzyAutomaton originallyLoadedFuzzyAutomaton;

	@FXML
	private TextField automatonNameTextField;

	@FXML
	private TextArea automatonDescriptionTextArea;

	@FXML
	private TextField stateNameTextField;

	@FXML
	private TextArea stateDescriptionTextArea;

	@FXML
	private ListView<FuzzyState> statesListView;

	@FXML
	private ListView<FuzzyTransition> transitionsListView;

	@FXML
	private ListView<Double> costsListView;

	@FXML
	private ComboBox<FuzzySetSystem> fuzzySetSystem;

	@FXML
	private ComboBox<FuzzyState> fromState;

	@FXML
	private ComboBox<FuzzyState> toState;

	@FXML
	private ComboBox<FuzzySet> stateFuzzySet;

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
	private Button cancelButton;

	@FXML
	private Button saveButton;

	private int createdAutomatonCounter;
	private int createdStateCounter;
	private int createdTransitionCounter;


	@FXML
	private void initialize() {

		this.fuzzyAutomaton = new SimpleObjectProperty<>();
		this.fuzzyStateToEdit = new SimpleObjectProperty<>();
		this.fuzzyTransitionToEdit = new SimpleObjectProperty<>();

		bindViewElementsToControllerProperties();

		this.createdAutomatonCounter = 0;
		this.createdStateCounter = 0;
		this.createdTransitionCounter = 0;
	}


	void bindViewElementsToControllerProperties() {

	}

	void unbindViewElementsFromControllerProperties(){

		if (fuzzyAutomaton.getValue() != null){
			automatonNameTextField.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomationNameProperty());
			automatonDescriptionTextArea.textProperty().unbindBidirectional(fuzzyAutomaton.getValue().fuzzyAutomatonDescriptionProperty());

			statesListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyStatesProperty());
			transitionsListView.itemsProperty().unbindBidirectional(fuzzyAutomaton.get().fuzzyTransitionsProperty());
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
				"That's a custom fuzzy automaton", null, null));
		this.originallyLoadedFuzzyAutomaton = null;
	}

	@FXML
	private void clearAutomaton(){

		unbindViewElementsFromControllerProperties();
		this.fuzzyAutomaton.setValue(null);
		automatonNameTextField.textProperty().set(null);
		automatonDescriptionTextArea.textProperty().set(null);
		this.originallyLoadedFuzzyAutomaton = null;
	}

	@FXML
	private void saveAutomaton(){

		CommonServices commonServices = new ServiceFactory().getCommonServices();
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
			unbindViewElementsFromControllerProperties();
			bindViewElementsToControllerProperties();
			createdStateCounter++;
			fuzzyAutomaton.get().fuzzyStatesProperty().add(new FuzzyState("state" + createdStateCounter, null, null, null));
		}
	}

	@FXML
	private void editState(){

	}

	@FXML
	private void removeState(){

	}

	@FXML
	private void addTransition(){

		if (fuzzyAutomaton != null && fuzzyAutomaton.get() != null && fuzzyAutomaton.get().fuzzyTransitionsProperty() != null) {
			unbindViewElementsFromControllerProperties();
			bindViewElementsToControllerProperties();
			createdTransitionCounter++;
			fuzzyAutomaton.get().fuzzyTransitionsProperty().add(new FuzzyTransition("transition" + createdTransitionCounter, null));
		}
	}

	@FXML
	private void editTransition(){

	}

	@FXML
	private void removeTransition(){

	}

	@FXML
	private void addCostValue(){

	}

	@FXML
	private void removeCostValue(){

	}

	@FXML
	private void cancelButtonPressed(){

	}

	@FXML
	private void okButtonPressed(){

	}

}
