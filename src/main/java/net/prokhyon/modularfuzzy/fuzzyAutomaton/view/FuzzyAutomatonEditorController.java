package net.prokhyon.modularfuzzy.fuzzyAutomaton.view;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;

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
	private ListView<FuzzySet> statesListView;

	@FXML
	private ListView<FuzzySet> edgesListView;

	@FXML
	private ListView<FuzzySet> costsListView;

	@FXML
	private ComboBox<FuzzySetSystemTypeEnum> fuzzySetSystem;

	@FXML
	private ComboBox<FuzzySetSystemTypeEnum> fromState;

	@FXML
	private ComboBox<FuzzySetSystemTypeEnum> toState;

	@FXML
	private ComboBox<FuzzySetSystemTypeEnum> fromFuzzySet;

	@FXML
	private ComboBox<FuzzySetSystemTypeEnum> toFuzzySet;

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
	private Button removeStateButton;

	@FXML
	private Button addEdgeButton;

	@FXML
	private Button removeEdgeButton;

	@FXML
	private Button addCostValueButton;

	@FXML
	private Button removeCostValueButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button okButton;

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
	}

	@FXML
	private void createAutomatonButton(){

		createdAutomatonCounter++;
		loadWithData(new FuzzyAutomaton(null, "automaton" + Integer.toString(createdAutomatonCounter),
				"That's a custom fuzzy automaton"));
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

	}

	@FXML
	private void addState(){

	}

	@FXML
	private void removeState(){

	}

	@FXML
	private void addEdge(){

	}

	@FXML
	private void removeEdge(){

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
