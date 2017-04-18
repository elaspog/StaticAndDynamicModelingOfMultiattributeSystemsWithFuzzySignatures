package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AutomatonTools {

    static CompoundFuzzyState searchForIncreasedSizeNode(List<CompoundFuzzyState> nPlusOneSizedListToSearch, CompoundFuzzyState nSizedState, FuzzyState fuzzyState) {

        CompoundFuzzyState compoundStateToEdit = null;
        List<CompoundFuzzyState> candidates = new ArrayList<>();

        outerloop:
        for (CompoundFuzzyState compoundFuzzyState : nPlusOneSizedListToSearch) {

            final int size = compoundFuzzyState.getFuzzyStateTuple().size();
            if (size - 1 != nSizedState.getFuzzyStateTuple().size())
                throw new RuntimeException("Algorithmic error - incompatible sizes.");

            if (! compoundFuzzyState.getFuzzyStateTuple().get(size-1).equals(fuzzyState)){
                continue;
            } else {
                for (int i = 0; i < size - 1; i++){
                    if (! compoundFuzzyState.getFuzzyStateTuple().get(i).equals(nSizedState.getFuzzyStateTuple().get(i))){
                        continue outerloop;
                    }
                }
            }
            candidates.add(compoundFuzzyState);
        }

        if(candidates.size() > 1) {
            throw new RuntimeException("Algorithmic error - multiple matches for a compound node.");
        } else if (candidates.size() == 1) {
            compoundStateToEdit = candidates.get(0);
        }

        return compoundStateToEdit;
    }

    static CompoundFuzzyState createIncreasedSizedUnfinishedNode(CompoundFuzzyState nSizedState, FuzzyState fuzzyState) {

        CompoundFuzzyState compoundStateToEdit = new CompoundFuzzyState();
        compoundStateToEdit.getFuzzyStateTuple().addAll(nSizedState.getFuzzyStateTuple());
        compoundStateToEdit.getFuzzyStateTuple().add(fuzzyState);
        return compoundStateToEdit;
    }

    static CompoundFuzzyState searchForOrCreateNewUnfinishedIncreasedSizeNode(List<CompoundFuzzyState> nPlusOneSizedListToSearch, CompoundFuzzyState nSizedState, FuzzyState fuzzyState) {

        CompoundFuzzyState compoundStateToEdit = searchForIncreasedSizeNode(nPlusOneSizedListToSearch, nSizedState, fuzzyState);

        if (compoundStateToEdit == null){
            compoundStateToEdit = createIncreasedSizedUnfinishedNode(nSizedState, fuzzyState);
        }
        return compoundStateToEdit;
    }

    static List<FuzzyTransition> getOutgoingEdgesForAStateOfAnAutomaton(FuzzyState fuzzyState, FuzzyAutomaton automatonToExtendWith) {

        final List<FuzzyTransition> fuzzyTransitions = automatonToExtendWith.getFuzzyTransitions();
        return fuzzyTransitions.stream().filter( x -> x.getFromState().equals(fuzzyState)).collect(Collectors.toList());
    }

    static List<FuzzyState> getInitStatesOfFuzzyAutomaton(FuzzyAutomaton automaton) {

        return getInitStatesOfFuzzyAutomaton(automaton.getFuzzyStates());
    }

    private static List<FuzzyState> getInitStatesOfFuzzyAutomaton(List<FuzzyState> fuzzyStates) {

        return fuzzyStates.stream()
                .filter(x -> x.getFuzzyStateType().equals(FuzzyStateTypeEnum.INITIAL))
                .collect(Collectors.toList());
    }

    static List<CompoundFuzzyState> getInitializationStatesOfCompoundAutomaton(CompoundFuzzyAutomaton compoundFuzzyAutomaton){

        return getInitializationStatesOfCompoundAutomaton(compoundFuzzyAutomaton.getCompoundFuzzyStates());
    }

    private static List<CompoundFuzzyState> getInitializationStatesOfCompoundAutomaton(List<CompoundFuzzyState> compoundFuzzyStates) {

        return compoundFuzzyStates.stream()
                .filter(x -> x.getFuzzyStateTuple().stream()
                        .allMatch(y -> y.getFuzzyStateType().equals(FuzzyStateTypeEnum.INITIAL)))
                .collect(Collectors.toList());
    }

    public static CompoundFuzzyTransition createCombinedCompoundTransition(
            int automatonIndex, CompoundFuzzyState sourceCompoundState, CompoundFuzzyState targetCompoundState,
            CompoundFuzzyTransition compoundFuzzyTransition, FuzzyTransition simpleFuzzyTransition) {

        final List<FuzzyTransition> oldTransitions;

        if (compoundFuzzyTransition != null){
            oldTransitions = compoundFuzzyTransition.getTransitions();
        } else {
            oldTransitions = new ArrayList<>();
            for (int i = 0 ; i < automatonIndex; i++){
                oldTransitions.add(null);
            }
        }

        List<FuzzyTransition> newTransitionList = new ArrayList<>();
        newTransitionList.addAll(oldTransitions);
        newTransitionList.add(simpleFuzzyTransition);

        return new CompoundFuzzyTransition(sourceCompoundState, targetCompoundState, newTransitionList);

    }

}
