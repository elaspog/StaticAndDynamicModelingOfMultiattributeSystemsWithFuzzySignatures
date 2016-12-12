package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;


import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

import java.util.ArrayList;
import java.util.List;

public class FuzzyAutomaton extends WorkspaceElement
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    private final StringProperty uuid;
    private final StringProperty fuzzyAutomationName;
    private final StringProperty fuzzyAutomatonDescription;
    private final ListProperty<FuzzyState> fuzzyStates;
    private final ListProperty<FuzzyTransition> fuzzyTransitions;


    public FuzzyAutomaton(String uuid, String fuzzyAutomatonName, String fuzzyAutomatonDescription,
                          List<FuzzyState> fuzzyStates, List<FuzzyTransition> fuzzyTransitions){
        super();
        this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
        this.fuzzyAutomationName = new SimpleStringProperty(fuzzyAutomatonName);
        this.fuzzyAutomatonDescription = new SimpleStringProperty(fuzzyAutomatonDescription);

        List<FuzzyState> copiedFuzzyStates = new ArrayList<>();
        List<FuzzyTransition> copiedFuzzyTransitions = new ArrayList<>();
        if (fuzzyStates != null) {
            for (FuzzyState fs : fuzzyStates) {
                copiedFuzzyStates.add(fs.deepCopy());
            }
        }
        if (fuzzyTransitions != null) {
            for (FuzzyTransition ft : fuzzyTransitions) {
                copiedFuzzyTransitions.add(ft.deepCopy());
            }
        }
        this.fuzzyTransitions = new SimpleListProperty<>(FXCollections.observableArrayList(copiedFuzzyTransitions));
        this.fuzzyStates = new SimpleListProperty<>(FXCollections.observableArrayList(copiedFuzzyStates));
    }

    public FuzzyAutomaton(FuzzyAutomaton otherFuzzyAutomaton){
        this(otherFuzzyAutomaton.getUuid(), otherFuzzyAutomaton.getFuzzyAutomationName(),
                otherFuzzyAutomaton.getFuzzyAutomatonDescription(), otherFuzzyAutomaton.getFuzzyStates(),
                otherFuzzyAutomaton.getFuzzyTransitions());
    }

    public FuzzyAutomaton deepCopy() {
        return new FuzzyAutomaton(this);
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton convert2DescriptorModel() {
        return null;
    }

    public String getUuid() {
        return uuid.get();
    }

    public StringProperty uuidProperty() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public String getFuzzyAutomationName() {
        return fuzzyAutomationName.get();
    }

    public StringProperty fuzzyAutomationNameProperty() {
        return fuzzyAutomationName;
    }

    public void setFuzzyAutomationName(String fuzzyAutomationName) {
        this.fuzzyAutomationName.set(fuzzyAutomationName);
    }

    public String getFuzzyAutomatonDescription() {
        return fuzzyAutomatonDescription.get();
    }

    public StringProperty fuzzyAutomatonDescriptionProperty() {
        return fuzzyAutomatonDescription;
    }

    public void setFuzzyAutomatonDescription(String fuzzyAutomatonDescription) {
        this.fuzzyAutomatonDescription.set(fuzzyAutomatonDescription);
    }

    public ObservableList<FuzzyState> getFuzzyStates() {
        return fuzzyStates.get();
    }

    public ListProperty<FuzzyState> fuzzyStatesProperty() {
        return fuzzyStates;
    }

    public void setFuzzyStates(ObservableList<FuzzyState> fuzzyStates) {
        this.fuzzyStates.set(fuzzyStates);
    }

    public ObservableList<FuzzyTransition> getFuzzyTransitions() {
        return fuzzyTransitions.get();
    }

    public ListProperty<FuzzyTransition> fuzzyTransitionsProperty() {
        return fuzzyTransitions;
    }

    public void setFuzzyTransitions(ObservableList<FuzzyTransition> fuzzyTransitions) {
        this.fuzzyTransitions.set(fuzzyTransitions);
    }

}