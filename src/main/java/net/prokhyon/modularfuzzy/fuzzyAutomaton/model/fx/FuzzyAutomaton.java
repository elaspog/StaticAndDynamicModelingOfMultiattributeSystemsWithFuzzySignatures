package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;


import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuzzyAutomaton extends WorkspaceFxRootElementBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    private final StringProperty uuid;
    private final StringProperty fuzzyAutomatonName;
    private final StringProperty fuzzyAutomatonDescription;
    private final ObjectProperty<Integer> costVectorDimensionObj;
    private final IntegerProperty costVectorDimensionInt;
    private final ListProperty<FuzzyState> fuzzyStates;
    private final ListProperty<FuzzyTransition> fuzzyTransitions;
    private final ObjectProperty<FuzzySetSystem> fuzzySetSystem;


    public FuzzyAutomaton(String uuid, String fuzzyAutomatonName, String fuzzyAutomatonDescription,
                          List<FuzzyState> fuzzyStates, List<FuzzyTransition> fuzzyTransitions,
                          FuzzySetSystem fuzzySetSystem, Integer costVectorDimension){
        super();
        this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
        this.fuzzyAutomatonName = new SimpleStringProperty(fuzzyAutomatonName);
        this.fuzzyAutomatonDescription = new SimpleStringProperty(fuzzyAutomatonDescription);
        this.fuzzySetSystem = new SimpleObjectProperty<>(fuzzySetSystem);
        this.costVectorDimensionObj = new SimpleObjectProperty(costVectorDimension);
        this.costVectorDimensionInt = IntegerProperty.integerProperty(costVectorDimensionObj);
        this.fuzzyStates = new SimpleListProperty<>(FXCollections.observableArrayList(fuzzyStates != null ? fuzzyStates : new ArrayList<>()));
        this.fuzzyTransitions = new SimpleListProperty<>(FXCollections.observableArrayList(fuzzyTransitions != null ? fuzzyTransitions : new ArrayList<>()));
    }

    public FuzzyAutomaton(FuzzyAutomaton otherFuzzyAutomaton){
        this(otherFuzzyAutomaton.getUuid(), otherFuzzyAutomaton.getFuzzyAutomatonName(),
                otherFuzzyAutomaton.getFuzzyAutomatonDescription(), otherFuzzyAutomaton.getFuzzyStates(),
                otherFuzzyAutomaton.getFuzzyTransitions(), otherFuzzyAutomaton.getFuzzySetSystem(),
                otherFuzzyAutomaton.getCostVectorDimensionObj());
    }

    public FuzzyAutomaton deepCopy() {

        final Tuple2<Map<FuzzyState, FuzzyState>, List<FuzzyState>> tuple2 = cleverCopyFuzzyStates(this.getFuzzyStates());

        final Map<FuzzyState, FuzzyState> pairsOfOldAndCopiedStates = tuple2._1;
        final List<FuzzyState> copiedStates = tuple2._2;

        final FuzzySetSystem fuzzySetSystem = this.getFuzzySetSystem() != null ? this.getFuzzySetSystem().deepCopy() : null;

        return new FuzzyAutomaton(this.getUuid(), this.getFuzzyAutomatonName(),
                this.getFuzzyAutomatonDescription(), copiedStates,
                deepCopyFuzzyTransitions(this.getFuzzyTransitions(), pairsOfOldAndCopiedStates),
                fuzzySetSystem,
                this.getCostVectorDimensionObj());
    }

    private Tuple2<Map<FuzzyState, FuzzyState>, List<FuzzyState>> cleverCopyFuzzyStates(List<FuzzyState> fuzzyStates){

        Map<FuzzyState, FuzzyState> retMap = new HashMap<>();
        List<FuzzyState> copiedFuzzyStates = new ArrayList<>();

        if (fuzzyStates != null) {
            for (FuzzyState fuzzyState : fuzzyStates) {
                final FuzzyState clonedFuzzyState = fuzzyState.deepCopy();
                copiedFuzzyStates.add(clonedFuzzyState);
                retMap.put(fuzzyState, clonedFuzzyState);
            }
        }
        return new Tuple2<>(retMap, copiedFuzzyStates);
    }

    private List<FuzzyTransition> deepCopyFuzzyTransitions(List<FuzzyTransition> fuzzyTransitions, Map<FuzzyState, FuzzyState> pairsOfOldAndCopiedStates){

        if (fuzzyTransitions != null) {
            List<FuzzyTransition> copiedFuzzyTransitions = new ArrayList<>();
            for (FuzzyTransition ft : fuzzyTransitions) {
                copiedFuzzyTransitions.add(ft.deepCopy(pairsOfOldAndCopiedStates));
            }
            return copiedFuzzyTransitions;
        }
        return fuzzyTransitions;
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton convert2DescriptorModel() {

        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState> descriptorFuzzyStates = new ArrayList<>();
        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition> descriptorFuzzyTransitions = new ArrayList<>();
        for (FuzzyState fxFuzzyState : this.getFuzzyStates()){
            final net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState fuzzyState = fxFuzzyState.convert2DescriptorModel();
            descriptorFuzzyStates.add(fuzzyState);
        }
        for (FuzzyTransition fxFuzzyTransition : this.getFuzzyTransitions()){
            final net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition fuzzyTransition = fxFuzzyTransition.convert2DescriptorModel();
            descriptorFuzzyTransitions.add(fuzzyTransition);
        }

        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton(this.getUuid(),
                this.getFuzzyAutomatonName(), this.getFuzzyAutomatonDescription(), this.fuzzySetSystem.get().getUuid(), descriptorFuzzyStates,
                descriptorFuzzyTransitions, this.getCostVectorDimensionObj());
    }

    @Override
    public String getUuid() {
        return uuid.get();
    }

    public StringProperty uuidProperty() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public String getFuzzyAutomatonName() {
        return fuzzyAutomatonName.get();
    }

    public StringProperty fuzzyAutomatonNameProperty() {
        return fuzzyAutomatonName;
    }

    public void setFuzzyAutomatonName(String fuzzyAutomatonName) {
        this.fuzzyAutomatonName.set(fuzzyAutomatonName);
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

    public Integer getCostVectorDimensionObj() {
        return costVectorDimensionObj.get();
    }

    public ObjectProperty<Integer> costVectorDimensionObjProperty() {
        return costVectorDimensionObj;
    }

    public void setCostVectorDimensionObj(Integer costVectorDimensionObj) {
        this.costVectorDimensionObj.set(costVectorDimensionObj);
    }

    public int getCostVectorDimensionInt() {
        return costVectorDimensionInt.get();
    }

    public IntegerProperty costVectorDimensionIntProperty() {
        return costVectorDimensionInt;
    }

    public void setCostVectorDimensionInt(int costVectorDimensionInt) {
        this.costVectorDimensionInt.set(costVectorDimensionInt);
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

    public FuzzySetSystem getFuzzySetSystem() {
        return fuzzySetSystem.get();
    }

    public ObjectProperty<FuzzySetSystem> fuzzySetSystemProperty() {
        return fuzzySetSystem;
    }

    public void setFuzzySetSystem(FuzzySetSystem fuzzySetSystem) {
        this.fuzzySetSystem.set(fuzzySetSystem);
    }

    @Override
    public String getListElementIdentifier() {
        String str = fuzzyAutomatonName.get();
        str += " (" + fuzzyStates.size() + ", " + fuzzyTransitions.size() + ")";
        // TODO temporary soulution, in future the value is always present
        str += " [";
        if (fuzzySetSystem.get() != null)
            str += fuzzySetSystem.get().getFuzzySystemName();
        str += "]";
        return str;
    }

}
