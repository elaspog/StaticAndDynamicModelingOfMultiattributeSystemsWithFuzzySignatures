package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.common.utils.Tuple4;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompoundFuzzyAutomaton {

    private List<FuzzyAutomaton> fuzzyAutomatonTuple;
    private List<CompoundFuzzyState> compoundFuzzyStates;
    private List<CompoundFuzzyTransition> compoundFuzzyTransitions;

    public CompoundFuzzyAutomaton(){
        reset();
    }

    public CompoundFuzzyAutomaton(FuzzyAutomaton automaton){
        this();
        extendExistingCompoundWith(automaton);
    }

    public void reset(){

        fuzzyAutomatonTuple = new ArrayList<>();
        compoundFuzzyStates = new ArrayList<>();
        compoundFuzzyTransitions = new ArrayList<>();
    }

    public void extendExistingCompoundWith(FuzzyAutomaton automatonToExtendWith) {

        final int automatonIndex = fuzzyAutomatonTuple.size();

        fuzzyAutomatonTuple.add(automatonToExtendWith);

        final List<FuzzyState> statesToExtendWith = automatonToExtendWith.getFuzzyStates();
        final List<FuzzyTransition> transitionsToExtendWith = automatonToExtendWith.getFuzzyTransitions();

        if(automatonIndex == 0){
            /// this is the first fuzzy automaton to be added to the compound automaton
            initializeCompoundAutomaton(automatonIndex, statesToExtendWith, transitionsToExtendWith);

        } else {
            /// the fuzzy automaton is to be multiplied by the compound automaton
            makeCartesianProductOfAutomatons(automatonIndex, automatonToExtendWith);
        }
    }

    private void makeCartesianProductOfAutomatons(int automatonIndex, FuzzyAutomaton automatonToExtendWith) {

        /// container of compound states of N-size and simple states of size 1
        List<Tuple2<CompoundFuzzyState, FuzzyState>> statePairsToBeProcessed = new ArrayList<>();

        for (CompoundFuzzyState compoundFuzzyState : AutomatonTools.getInitializationStatesOfCompoundAutomaton(this)) {
            for (FuzzyState fuzzyState : AutomatonTools.getInitStatesOfFuzzyAutomaton(automatonToExtendWith)) {

                /// add initial state pairs to process list
                statePairsToBeProcessed.add(new Tuple2<>(compoundFuzzyState, fuzzyState));
            }
        }

        /// container of N+1 sized compound states
        List<CompoundFuzzyState> processedCompoundStates = new ArrayList<>();

        /// buffer for storing transitions
        List<Tuple4<CompoundFuzzyState, CompoundFuzzyState, CompoundFuzzyTransition, FuzzyTransition>> transBuffer = new ArrayList<>();

        /// setting up states
        while (statePairsToBeProcessed.isEmpty() == false){

            /// get and remove the first element of the state pairs, which will become the actually processed N+1 sized compound source state
            final Tuple2<CompoundFuzzyState, FuzzyState> statePairToProcess = statePairsToBeProcessed.get(0);
            statePairsToBeProcessed.remove(statePairToProcess);

            /// get outgoing edges for the compound and simple automaton
            final List<CompoundFuzzyTransition> outgoingEdgesOfCompoundState = statePairToProcess._1.getOutgoingEdges();
            final List<FuzzyTransition> outgoingEdgesOfFuzzyState = AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(statePairToProcess._2, automatonToExtendWith);

            /// adding null transitions for all possible combination: (N+1)*(M+1) new outgoing transitions for a compound state
            outgoingEdgesOfCompoundState.add(0, null);
            outgoingEdgesOfFuzzyState.add(0, null);

            /// search for the actual source compound state between already processed N+1 sized compound states
            CompoundFuzzyState sourceCompoundState = AutomatonTools.searchForIncreasedSizeNode(processedCompoundStates, statePairToProcess._1, statePairToProcess._2);
            if (sourceCompoundState == null) {
                sourceCompoundState = AutomatonTools.createIncreasedSizedUnfinishedNode(statePairToProcess._1, statePairToProcess._2);
                processedCompoundStates.add(sourceCompoundState);
            }

            /// create all possible combinations of all outgoing edges, these edges are determined by the source states of the N-sized compound automaton and the simple automaton
            for (CompoundFuzzyTransition outgoingCompoundFuzzyTransition : outgoingEdgesOfCompoundState) {
                for (FuzzyTransition outgoingFuzzyTransition : outgoingEdgesOfFuzzyState) {

                    if (outgoingCompoundFuzzyTransition == null && outgoingFuzzyTransition == null){

                        /// the case of selfloop is not interesting for the new automaton
                        continue;
                    }

                    /// get target state for outgoing transition of compound state
                    final CompoundFuzzyState toCompoundState;
                    if (outgoingCompoundFuzzyTransition != null) {
                        toCompoundState = outgoingCompoundFuzzyTransition.getToState();
                    } else {
                        toCompoundState = statePairToProcess._1;
                    }

                    /// get target state for outgoing transition of simple state
                    final FuzzyState toSimpleState;
                    if (outgoingFuzzyTransition != null) {
                        toSimpleState = outgoingFuzzyTransition.getToState();
                    } else {
                        toSimpleState = statePairToProcess._2;
                    }

                    /// search for the target state combination along the transitions of compound state and simple state in processed list
                    CompoundFuzzyState targetCompoundState = AutomatonTools.searchForIncreasedSizeNode(processedCompoundStates, toCompoundState, toSimpleState);

                    if (targetCompoundState == null) {

                        /// each not found (and unprocessed) target state pair will be added to processing list
                        final Tuple2<CompoundFuzzyState, FuzzyState> targetStatePair = new Tuple2<>(toCompoundState, toSimpleState);
                        statePairsToBeProcessed.add(targetStatePair);

                        /// new target N+1 sized compound state is created for actual combination
                        targetCompoundState = AutomatonTools.createIncreasedSizedUnfinishedNode(toCompoundState, toSimpleState);
                        processedCompoundStates.add(targetCompoundState);
                    }

                    if (! isAlreadyStoredInTransitionBuffer(transBuffer, sourceCompoundState, outgoingCompoundFuzzyTransition, outgoingFuzzyTransition, targetCompoundState)){

                        /// add the transition combination to the transition list for further processing
                        transBuffer.add(new Tuple4<>(sourceCompoundState, targetCompoundState, outgoingCompoundFuzzyTransition, outgoingFuzzyTransition));
                    }
                }
            }
        }

        /// container of N+1 sized compound transitions
        List<CompoundFuzzyTransition> processedCompoundTransitions = new ArrayList<>();

        /// setting up transitions
        for (Tuple4<CompoundFuzzyState, CompoundFuzzyState, CompoundFuzzyTransition, FuzzyTransition> rec : transBuffer) {

            /// N+1 sized compound states
            final CompoundFuzzyState sourceCompoundState = rec._1;
            final CompoundFuzzyState targetCompoundState = rec._2;

            /// N sized transition
            final CompoundFuzzyTransition compoundFuzzyTransition = rec._3;

            /// 1 sized transition
            final FuzzyTransition simpleFuzzyTransition = rec._4;

            if ( ! processedCompoundStates.contains(sourceCompoundState))
                throw new RuntimeException("Compound source state not found: " + sourceCompoundState.toString());
            if ( ! processedCompoundStates.contains(targetCompoundState))
                throw new RuntimeException("Compound target state not found: " + targetCompoundState.toString());

            /// create new compound transition from nodes
            CompoundFuzzyTransition newCompoundTransition
                    = AutomatonTools.createCombinedCompoundTransition(automatonIndex, sourceCompoundState, targetCompoundState, compoundFuzzyTransition, simpleFuzzyTransition);

            sourceCompoundState.setOutgoingEdge(newCompoundTransition);
            targetCompoundState.setIncomingEdge(newCompoundTransition);

            processedCompoundTransitions.add(newCompoundTransition);
        }

        /// updating list of compound states and automatons
        compoundFuzzyStates = processedCompoundStates;
        compoundFuzzyTransitions = processedCompoundTransitions;
    }

    private boolean isAlreadyStoredInTransitionBuffer(List<Tuple4<CompoundFuzzyState, CompoundFuzzyState, CompoundFuzzyTransition, FuzzyTransition>> transBuffer,
                                                      CompoundFuzzyState sourceCompoundState, CompoundFuzzyTransition outgoingCompoundFuzzyTransition,
                                                      FuzzyTransition outgoingFuzzyTransition, CompoundFuzzyState targetCompoundState) {

        boolean isAlreadyStored = false;
        for (Tuple4<CompoundFuzzyState, CompoundFuzzyState, CompoundFuzzyTransition, FuzzyTransition> tuple : transBuffer) {
            if (tuple._1 == sourceCompoundState && tuple._2 == targetCompoundState){

                if ( tuple._3 == null && outgoingCompoundFuzzyTransition == null || outgoingCompoundFuzzyTransition.equals(tuple._3)){
                    if ((tuple._4 == null && outgoingFuzzyTransition == null) || outgoingFuzzyTransition.equals(tuple._4)){
                        isAlreadyStored = true;
                        break;
                    }
                }
            }
        }
        return isAlreadyStored;
    }

    private void initializeCompoundAutomaton(int automatonIndex, List<FuzzyState> statesToExtendWith, List<FuzzyTransition> transitionsToExtendWith) {

        for (FuzzyState fuzzyStateToInsert : statesToExtendWith) {
            compoundFuzzyStates.add(new CompoundFuzzyState(fuzzyStateToInsert));
        }

        for (FuzzyTransition fuzzyTransitionToInsert : transitionsToExtendWith) {

            final FuzzyState fromState = fuzzyTransitionToInsert.getFromState();
            final FuzzyState toState = fuzzyTransitionToInsert.getToState();
            CompoundFuzzyState fromCompoundFuzzyState = null;
            CompoundFuzzyState toCompoundFuzzyState = null;

            for (CompoundFuzzyState compoundFuzzyState : compoundFuzzyStates) {
                if (compoundFuzzyState.equalsInPosition(automatonIndex, fromState)){
                    fromCompoundFuzzyState = compoundFuzzyState;
                }
                if (compoundFuzzyState.equalsInPosition(automatonIndex, toState)){
                    toCompoundFuzzyState = compoundFuzzyState;
                }

                if (fromCompoundFuzzyState != null && toCompoundFuzzyState != null){
                    break;
                }
            }
            final CompoundFuzzyTransition compoundFuzzyTransition = new CompoundFuzzyTransition(fromCompoundFuzzyState, toCompoundFuzzyState, fuzzyTransitionToInsert);
            compoundFuzzyTransitions.add(compoundFuzzyTransition);

            fromCompoundFuzzyState.setOutgoingEdge(compoundFuzzyTransition);
            toCompoundFuzzyState.setIncomingEdge(compoundFuzzyTransition);
        }
    }


    public List<FuzzyAutomaton> getFuzzyAutomatonTuple() {
        return fuzzyAutomatonTuple;
    }

    public List<CompoundFuzzyState> getCompoundFuzzyStates() {
        return compoundFuzzyStates;
    }

    public List<CompoundFuzzyTransition> getCompoundFuzzyTransitions() {
        return compoundFuzzyTransitions;
    }

    public String getAggregatedAutomatonName(){

        return "(" + fuzzyAutomatonTuple.stream()
                .map(x -> x.getFuzzyAutomatonName())
                .collect(Collectors.joining(" × ")) + ")";
    }

}
