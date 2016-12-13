package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

import java.util.ArrayList;
import java.util.List;

public class FuzzyTransition extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition> {

    private final StringProperty fuzzyTransitionName;
    private final StringProperty fuzzyTransitionDescription;
    private final ListProperty<Double> costVector;
    private final ObjectProperty<FuzzyState> fromState;
    private final ObjectProperty<FuzzyState> toState;

    public FuzzyTransition(String fuzzyTransitionName, String fuzzyTransitionDescription, List<Double> costVector,
                           FuzzyState fromState, FuzzyState toState) {
        super();
        this.fuzzyTransitionName = new SimpleStringProperty(fuzzyTransitionName);
        this.fuzzyTransitionDescription = new SimpleStringProperty(fuzzyTransitionDescription);
        this.fromState = new SimpleObjectProperty<>(fromState);
        this.toState = new SimpleObjectProperty<>(toState);
        if (costVector != null)
            this.costVector = new SimpleListProperty<>(FXCollections.observableArrayList(costVector));
        else
            this.costVector = new SimpleListProperty<>(FXCollections.observableArrayList(new ArrayList<Double>()));
    }

    public FuzzyTransition(FuzzyTransition otherFuzzyTransition) {
        this(otherFuzzyTransition.getFuzzyTransitionName(), otherFuzzyTransition.getFuzzyTransitionDescription(),
                otherFuzzyTransition.getCostVector(), otherFuzzyTransition.getFromState(),
                otherFuzzyTransition.getToState());
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition convert2DescriptorModel() {

        // TODO Handle start/to state ids here
        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition(
                getFuzzyTransitionName(), getFuzzyTransitionDescription(), null, null,
                costVector.get());
    }

    public FuzzyTransition deepCopy() {
        return new FuzzyTransition(this);
    }

    public String getFuzzyTransitionName() {
        return fuzzyTransitionName.get();
    }

    public StringProperty fuzzyTransitionNameProperty() {
        return fuzzyTransitionName;
    }

    public void setFuzzyTransitionName(String fuzzyTransitionName) {
        this.fuzzyTransitionName.set(fuzzyTransitionName);
    }

    public String getFuzzyTransitionDescription() {
        return fuzzyTransitionDescription.get();
    }

    public StringProperty fuzzyTransitionDescriptionProperty() {
        return fuzzyTransitionDescription;
    }

    public void setFuzzyTransitionDescription(String fuzzyTransitionDescription) {
        this.fuzzyTransitionDescription.set(fuzzyTransitionDescription);
    }

    public ObservableList<Double> getCostVector() {
        return costVector.get();
    }

    public ListProperty<Double> costVectorProperty() {
        return costVector;
    }

    public void setCostVector(ObservableList<Double> costVector) {
        this.costVector.set(costVector);
    }

    public FuzzyState getFromState() {
        return fromState.get();
    }

    public ObjectProperty<FuzzyState> fromStateProperty() {
        return fromState;
    }

    public void setFromState(FuzzyState fromState) {
        this.fromState.set(fromState);
    }

    public FuzzyState getToState() {
        return toState.get();
    }

    public ObjectProperty<FuzzyState> toStateProperty() {
        return toState;
    }

    public void setToState(FuzzyState toState) {
        this.toState.set(toState);
    }
}
