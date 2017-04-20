package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompoundFuzzyState {

    private List<FuzzyState> fuzzyStateTuple = new ArrayList<>();
    private List<CompoundFuzzyTransition> incomingEdges = new ArrayList<>();
    private List<CompoundFuzzyTransition> outgoingEdges = new ArrayList<>();

    public CompoundFuzzyState(){
    }

    public CompoundFuzzyState(FuzzyState fuzzyState){

        fuzzyStateTuple.add(fuzzyState);
    }

    public boolean equalsInPosition(int position, FuzzyState fuzzyState){

        if (position < fuzzyStateTuple.size() && position >= 0)
            if (fuzzyStateTuple.get(position).equals(fuzzyState))
                return true;

        return false;
    }

    public boolean setEdge(CompoundFuzzyTransition compoundFuzzyTransition){

        boolean retValue = false;
        retValue = retValue || setOutgoingEdge(compoundFuzzyTransition);
        retValue = retValue || setIncomingEdge(compoundFuzzyTransition);
        return retValue;
    }

    public boolean setIncomingEdge(CompoundFuzzyTransition compoundFuzzyTransition) {

        boolean retValue = false;
        if (this.equals(compoundFuzzyTransition.getToState())){
            incomingEdges.add(compoundFuzzyTransition);
            retValue = true;
        }
        return retValue;
    }

    public boolean setOutgoingEdge(CompoundFuzzyTransition compoundFuzzyTransition) {

        boolean retValue = false;
        if (this.equals(compoundFuzzyTransition.getFromState())){
            outgoingEdges.add(compoundFuzzyTransition);
            retValue = true;
        }
        return retValue;
    }

    public List<FuzzyState> getFuzzyStateTuple() {
        return fuzzyStateTuple;
    }

    public List<CompoundFuzzyTransition> getIncomingEdges() {
        return incomingEdges;
    }

    public List<CompoundFuzzyTransition> getOutgoingEdges() {
        return outgoingEdges;
    }

    public String getAggregatedStateName(){

        return "(" + fuzzyStateTuple.stream()
                .map(x -> x.getFuzzyStateName())
                .collect(Collectors.joining(" × ")) + ")";
    }

    public FuzzyStateTypeEnum getAggregatedStateType() {

        if (fuzzyStateTuple.stream().allMatch(x -> x.getFuzzyStateType().equals(FuzzyStateTypeEnum.INITIAL)))
            return FuzzyStateTypeEnum.INITIAL;

        if (fuzzyStateTuple.stream().allMatch(x -> x.getFuzzyStateType().equals(FuzzyStateTypeEnum.TERMINAL)))
            return FuzzyStateTypeEnum.TERMINAL;

        return FuzzyStateTypeEnum.NORMAL;
    }
}
