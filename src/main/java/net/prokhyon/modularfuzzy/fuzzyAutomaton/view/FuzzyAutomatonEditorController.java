package net.prokhyon.modularfuzzy.fuzzyAutomaton.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.prokhyon.modularfuzzy.api.LoadableDataController;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;

public class FuzzyAutomatonEditorController implements LoadableDataController {

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


	@FXML
	private void initialize() {

	}

	@Override
	public <T extends WorkspaceElement> void loadWithData(T modelToLoad) {

	}

	@FXML
	private void createAutomatonButton(){

	}

	@FXML
	private void clearAutomatonButton(){

	}

	@FXML
	private void saveAutomatonButton(){

	}

	@FXML
	private void addStateButton(){

	}

	@FXML
	private void removeStateButton(){

	}

	@FXML
	private void addEdgeButton(){

	}

	@FXML
	private void removeEdgeButton(){

	}

	@FXML
	private void addCostValueButton(){

	}

	@FXML
	private void removeCostValue(){

	}

	@FXML
	private void cancelButton(){

	}

	@FXML
	private void okButton(){

	}

}
