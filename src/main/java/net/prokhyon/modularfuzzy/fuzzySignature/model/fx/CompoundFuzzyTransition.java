package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CompoundFuzzyTransition {

    private List<FuzzyTransition> transitions = new ArrayList<>();
    private CompoundFuzzyState fromState;
    private CompoundFuzzyState toState;


    public CompoundFuzzyTransition(CompoundFuzzyState fromCompoundFuzzyState, CompoundFuzzyState toCompoundFuzzyState, List<FuzzyTransition> fuzzyTransitions){

        fromState = fromCompoundFuzzyState;
        toState = toCompoundFuzzyState;
        transitions = fuzzyTransitions;
    }

    public CompoundFuzzyTransition(CompoundFuzzyState fromCompoundFuzzyState, CompoundFuzzyState toCompoundFuzzyState, FuzzyTransition fuzzyTransition) {
        this(fromCompoundFuzzyState, toCompoundFuzzyState, Arrays.asList(fuzzyTransition));
    }

    public CompoundFuzzyState getFromState() {
        return fromState;
    }

    public CompoundFuzzyState getToState() {
        return toState;
    }

    public List<FuzzyTransition> getTransitions() {
        return transitions;
    }

    public String getAggregatedTransitionName(){

        String ret = "(" + transitions.stream()
                .map(x -> ( x == null ? "null" : x.getFuzzyTransitionName()))
                .collect(Collectors.joining(" × ")) + ")";
        return ret;
    }

    public List<List<Double>> getCostVector(){

        return transitions.stream()
                .map(x -> x == null ? null : x.getCostVector())
                .collect(Collectors.toList());
    }

}
