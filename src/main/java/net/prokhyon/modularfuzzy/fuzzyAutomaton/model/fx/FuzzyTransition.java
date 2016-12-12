package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

public class FuzzyTransition extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition> {

    private final StringProperty fuzzyTransitionName;
    private final StringProperty fuzzyTransitionDescription;

    public FuzzyTransition(String fuzzyTransitionName, String fuzzyTransitionDescription) {
        super();
        this.fuzzyTransitionName = new SimpleStringProperty(fuzzyTransitionName);
        this.fuzzyTransitionDescription = new SimpleStringProperty(fuzzyTransitionDescription);
    }

    public FuzzyTransition(FuzzyTransition otherFuzzyTransition) {
        this(otherFuzzyTransition.getFuzzyTransitionName(), otherFuzzyTransition.getFuzzyTransitionDescription());
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition convert2DescriptorModel() {
        return null;
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
}
