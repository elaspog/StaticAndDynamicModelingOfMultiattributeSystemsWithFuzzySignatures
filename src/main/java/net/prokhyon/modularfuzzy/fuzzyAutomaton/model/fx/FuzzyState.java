package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;

public class FuzzyState extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState>{

    private final StringProperty fuzzyStateName;
    private final StringProperty fuzzyStateDescription;
    private final ObjectProperty<FuzzySet> fuzzySet;
    private final ObjectProperty<FuzzyStateTypeEnum> fuzzyStateType;

    public FuzzyState(String fuzzyStateName, String fuzzyStateDescription, FuzzySet fuzzySet, FuzzyStateTypeEnum fuzzyStateType) {
        super();
        this.fuzzyStateName = new SimpleStringProperty(fuzzyStateName);
        this.fuzzyStateDescription = new SimpleStringProperty(fuzzyStateDescription);
        this.fuzzySet = new SimpleObjectProperty<>(fuzzySet);
        this.fuzzyStateType = new SimpleObjectProperty<>(fuzzyStateType);
    }

    public FuzzyState(FuzzyState otherFuzzyState) {
        this(otherFuzzyState.getFuzzyStateName(), otherFuzzyState.getFuzzyStateDescription(),
                otherFuzzyState.getFuzzySet(), otherFuzzyState.getFuzzyStateType());
    }

    public FuzzyState deepCopy() {

        final FuzzySet fuzzySet = this.getFuzzySet() != null ? this.getFuzzySet().deepCopy() : null;
        return new FuzzyState( this.getFuzzyStateName(), this.getFuzzyStateDescription(), fuzzySet, this.getFuzzyStateType());
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState convert2DescriptorModel() {

        // TODO Handle reference to FuzzySetSystem's set (fuzzyValue)
        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState(getFuzzyStateName(),
                getFuzzyStateDescription(),  getFuzzyStateType(), getFuzzySet().getFuzzySetName());
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

    public FuzzySet getFuzzySet() {
        return fuzzySet.get();
    }

    public ObjectProperty<FuzzySet> fuzzySetProperty() {
        return fuzzySet;
    }

    public void setFuzzySet(FuzzySet fuzzySet) {
        this.fuzzySet.set(fuzzySet);
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
