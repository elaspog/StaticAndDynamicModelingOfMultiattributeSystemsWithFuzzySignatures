package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxElementBase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FuzzyTransition extends WorkspaceFxElementBase
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
        this.costVector = new SimpleListProperty<>(FXCollections.observableArrayList(costVector != null ? costVector : new ArrayList<Double>()));
    }

    public FuzzyTransition(FuzzyTransition otherFuzzyTransition) {
        this(otherFuzzyTransition.getFuzzyTransitionName(), otherFuzzyTransition.getFuzzyTransitionDescription(),
                otherFuzzyTransition.getCostVector(), otherFuzzyTransition.getFromState(),
                otherFuzzyTransition.getToState());
    }

    public FuzzyTransition deepCopy(Map<FuzzyState, FuzzyState> pairsOfOldAndCopiedStates) {
        return new FuzzyTransition(this.getFuzzyTransitionName(), this.getFuzzyTransitionDescription(),
                copyCostVector(this.getCostVector()), pairsOfOldAndCopiedStates.get(this.getFromState()),
                pairsOfOldAndCopiedStates.get(this.getToState()));
    }

    private List<Double> copyCostVector(List<Double> costVector){

        if (costVector != null) {
            List<Double> copiedCostVector = new ArrayList<>();
            for (Double d : costVector) {
                copiedCostVector.add(d);
            }
            return copiedCostVector;
        }
        return costVector;
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition convert2DescriptorModel() {

        final String fromStateName = fromState != null ? fromState.get().getFuzzyStateName() : null;
        final String toStateName = toState != null ? toState.get().getFuzzyStateName() : null;

        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition(
                getFuzzyTransitionName(), getFuzzyTransitionDescription(), fromStateName, toStateName,
                costVector.get());
    }

    public String getFuzzyTransitionName() {
        return fuzzyTransitionName.get();
    }

    public StringProperty fuzzyTransitionNameProperty() {
        return fuzzyTransitionName;
    }

    public String getFuzzyTransitionDescription() {
        return fuzzyTransitionDescription.get();
    }

    public StringProperty fuzzyTransitionDescriptionProperty() {
        return fuzzyTransitionDescription;
    }

    public ObservableList<Double> getCostVector() {
        return costVector.get();
    }

    public ListProperty<Double> costVectorProperty() {
        return costVector;
    }

    public FuzzyState getFromState() {
        return fromState.get();
    }

    public ObjectProperty<FuzzyState> fromStateProperty() {
        return fromState;
    }

    public FuzzyState getToState() {
        return toState.get();
    }

    public ObjectProperty<FuzzyState> toStateProperty() {
        return toState;
    }

}
