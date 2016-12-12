package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

import java.util.UUID;

public class FuzzyAutomaton extends WorkspaceElement
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    private final StringProperty uuid;
    private final StringProperty fuzzyAutomationName;
    private final StringProperty fuzzyAutomatonDescription;

    private StringProperty initializeUUIDPropertyFromString(String uuid){

        StringProperty fuzzySetUUIDProp = null;
        try{
            String uuidStr = UUID.fromString(uuid).toString();
            fuzzySetUUIDProp = new SimpleStringProperty(uuidStr);
        } catch (Exception exception){
            fuzzySetUUIDProp = new SimpleStringProperty(UUID.randomUUID().toString());
        }
        return fuzzySetUUIDProp;
    }

    public FuzzyAutomaton(String uuid, String fuzzyAutomatonName, String fuzzyAutomatonDescription){
        super();
        this.uuid = initializeUUIDPropertyFromString(uuid);
        this.fuzzyAutomationName = new SimpleStringProperty(fuzzyAutomatonName);
        this.fuzzyAutomatonDescription = new SimpleStringProperty(fuzzyAutomatonDescription);

    }

    public FuzzyAutomaton(FuzzyAutomaton otherFuzzyAutomaton){
        this(otherFuzzyAutomaton.getUuid(), otherFuzzyAutomaton.getFuzzyAutomationName(),
                otherFuzzyAutomaton.getFuzzyAutomatonDescription());
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

}
