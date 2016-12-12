package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;

public class FuzzyState extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState>{

    private final StringProperty fuzzyStateName;
    private final StringProperty fuzzyStateDescription;
    private final ObjectProperty<FuzzyState> fuzzyState;
    private final ObjectProperty<FuzzyStateTypeEnum> fuzzyStateType;

    public FuzzyState(String fuzzyStateName, String fuzzyStateDescription, FuzzyState fuzzyState, FuzzyStateTypeEnum fuzzyStateType) {
        super();
        this.fuzzyStateName = new SimpleStringProperty(fuzzyStateName);
        this.fuzzyStateDescription = new SimpleStringProperty(fuzzyStateDescription);
        this.fuzzyState = new SimpleObjectProperty<>(fuzzyState);
        this.fuzzyStateType = new SimpleObjectProperty<>(fuzzyStateType);
    }

    public FuzzyState(FuzzyState otherFuzzyState) {
        this(otherFuzzyState.getFuzzyStateName(), otherFuzzyState.getFuzzyStateDescription(),
                otherFuzzyState.getFuzzyState(), otherFuzzyState.getFuzzyStateType());
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState convert2DescriptorModel() {
        return null;
    }

    public FuzzyState deepCopy() {
        return new FuzzyState(this);
    }

    public String getFuzzyStateName() {
        return fuzzyStateName.get();
    }

    public StringProperty fuzzyStateNameProperty() {
        return fuzzyStateName;
    }

    public void setFuzzyStateName(String fuzzyStateName) {
        this.fuzzyStateName.set(fuzzyStateName);
    }

    public String getFuzzyStateDescription() {
        return fuzzyStateDescription.get();
    }

    public StringProperty fuzzyStateDescriptionProperty() {
        return fuzzyStateDescription;
    }

    public void setFuzzyStateDescription(String fuzzyStateDescription) {
        this.fuzzyStateDescription.set(fuzzyStateDescription);
    }

    public FuzzyState getFuzzyState() {
        return fuzzyState.get();
    }

    public ObjectProperty<FuzzyState> fuzzyStateProperty() {
        return fuzzyState;
    }

    public void setFuzzyState(FuzzyState fuzzyState) {
        this.fuzzyState.set(fuzzyState);
    }

    public FuzzyStateTypeEnum getFuzzyStateType() {
        return fuzzyStateType.get();
    }

    public ObjectProperty<FuzzyStateTypeEnum> fuzzyStateTypeProperty() {
        return fuzzyStateType;
    }

    public void setFuzzyStateType(FuzzyStateTypeEnum fuzzyStateType) {
        this.fuzzyStateType.set(fuzzyStateType);
    }
}
