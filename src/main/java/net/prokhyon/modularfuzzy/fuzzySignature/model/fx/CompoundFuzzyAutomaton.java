package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.common.utils.Tuple4;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.optimalization.Individual;
import net.prokhyon.modularfuzzy.optimalization.EvolutionarilyOptimizable;
import net.prokhyon.modularfuzzy.optimalization.IndividualInitializationType;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessFunction;
import net.prokhyon.modularfuzzy.optimalization.utils.Order;
import net.prokhyon.modularfuzzy.optimalization.utils.PopulationGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompoundFuzzyAutomaton
        implements EvolutionarilyOptimizable<CompoundFuzzyState> {

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

    public List<CompoundFuzzyState> filterCompoundFuzzyStateOfAutomatonByStateType(FuzzyStateTypeEnum fuzzyStateTypeEnum){

        return compoundFuzzyStates.stream().filter(x -> x.getAggregatedStateType().equals(fuzzyStateTypeEnum)).collect(Collectors.toList());
    }

    @Override
    public List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initializePopulationGetWithFitness(Map<IndividualInitializationType, Integer> populationInitializationPlan,
                                                                                                         ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                                                         FitnessFunction fitnessFunction,
                                                                                                         FitnessEvaluationStrategy fitnessEvaluationStrategy) {

        final List<CompoundFuzzyState> initialFuzzyStates = filterCompoundFuzzyStateOfAutomatonByStateType(FuzzyStateTypeEnum.INITIAL);
        final List<CompoundFuzzyState> terminalFuzzyStates = filterCompoundFuzzyStateOfAutomatonByStateType(FuzzyStateTypeEnum.TERMINAL);

        if (initialFuzzyStates == null || initialFuzzyStates.isEmpty())
            throw new RuntimeException("No init states found");
        if (terminalFuzzyStates == null || terminalFuzzyStates.isEmpty())
            throw new RuntimeException("No terminal states found");

        Order order = null;
        if (fitnessEvaluationStrategy.equals(FitnessEvaluationStrategy.MAXIMIZE_FITNESS)){
            order = Order.DESCENDING;
        } else if (fitnessEvaluationStrategy.equals(FitnessEvaluationStrategy.MINIMIZE_FITNESS)){
            order = Order.ASCENDING;
        }

        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationWithFitness = new ArrayList<>();

        for (Map.Entry<IndividualInitializationType, Integer> initializationEntry : populationInitializationPlan.entrySet()) {

            final IndividualInitializationType individualType = initializationEntry.getKey();
            final int count = initializationEntry.getValue().intValue();

            Order finalOrder = order;
            initialPopulationWithFitness = IntStream.range(0, count)
                    .mapToObj(i -> generateChromosome(individualType, chromosomeElementCostFunction, initialFuzzyStates, terminalFuzzyStates, finalOrder))
                    .collect(Collectors.toList());
        }
        return initialPopulationWithFitness;
    }

    private Tuple2<Individual<CompoundFuzzyState>, List<Double>> generateChromosome(IndividualInitializationType individualType,
                                                                                    ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                                    List<CompoundFuzzyState> initialFuzzyStates,
                                                                                    List<CompoundFuzzyState> terminalFuzzyStates,
                                                                                    Order order) {

        Individual<CompoundFuzzyState> newIndividual = null;
        final CompoundFuzzyState randomlySelectedInitState = PopulationGenerator.selectRandomElementFromList(initialFuzzyStates);

        List<Tuple2<CompoundFuzzyState, Double>> stateAndFitnessSequence = null;

        switch (individualType){

            case NEAREST_NEIGHBOUR:
                stateAndFitnessSequence = getNthNearestNeighbourRecursively(0, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                break;
            case SECONDARY_NEAREST_NEIGHBOUR:
                stateAndFitnessSequence = getNthNearestNeighbourRecursively(1, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                break;
            case ALTERNATING_NEAREST_NEIGHBOUR_NN_START:
                stateAndFitnessSequence = getAlternatingNearestNeighbourRecursively(0, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                break;
            case ALTERNATING_NEAREST_NEIGHBOUR_SNN_START:
                stateAndFitnessSequence = getAlternatingNearestNeighbourRecursively(1, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                break;
            case RANDOM:
                break;
            case DOMAIN_SPECIFIC_CONSTRAINT_BASED:
            default:
                throw new IllegalArgumentException("IndividualType not supported: " + individualType.toString());
        }

        List<CompoundFuzzyState> stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
        List<Double> costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());

        if (stateSequence != null){
            stateSequence.add(0, randomlySelectedInitState);
            newIndividual = new Individual<>(stateSequence);
        }

        if(newIndividual != null) {
            return new Tuple2<>(newIndividual, costSequence);
        }
        throw new NotImplementedException();
    }

    private List<Tuple2<CompoundFuzzyState, Double>> getNthNearestNeighbourRecursively(int position,
                                                                       ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                       CompoundFuzzyState state,
                                                                       List<CompoundFuzzyState> terminalFuzzyStates,
                                                                       Order order) {

        List<Tuple2<CompoundFuzzyState,List<List<Double>>>> candidates = new ArrayList<>();
        for (CompoundFuzzyTransition outgoingEdge : state.getOutgoingEdges()) {

            final CompoundFuzzyState toState = outgoingEdge.getToState();
            final List<List<Double>> costVector = outgoingEdge.getCostVector();

            candidates.add(new Tuple2<>(toState, costVector));
        }

        List<Tuple2<CompoundFuzzyState, Double>> retList = new ArrayList<>();
        final Tuple2<CompoundFuzzyState, Double> tuple2 = PopulationGenerator.selectByEvaluatedFitnessPosition(chromosomeElementCostFunction, candidates, position, order);
        retList.add(tuple2);

        final CompoundFuzzyState selectedTargetState = tuple2._1;
        if(! terminalFuzzyStates.contains(selectedTargetState)){

            final List<Tuple2<CompoundFuzzyState, Double>> recursiveList
                    = getNthNearestNeighbourRecursively(position, chromosomeElementCostFunction, selectedTargetState, terminalFuzzyStates, order);

            retList.addAll(recursiveList);
        }
        return retList;
    }

    private List<Tuple2<CompoundFuzzyState, Double>> getAlternatingNearestNeighbourRecursively(int position,
                                                                                               ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                                               CompoundFuzzyState state,
                                                                                               List<CompoundFuzzyState> terminalFuzzyStates,
                                                                                               Order order) {

        List<Tuple2<CompoundFuzzyState,List<List<Double>>>> candidates = new ArrayList<>();
        for (CompoundFuzzyTransition outgoingEdge : state.getOutgoingEdges()) {

            final CompoundFuzzyState toState = outgoingEdge.getToState();
            final List<List<Double>> costVector = outgoingEdge.getCostVector();

            candidates.add(new Tuple2<>(toState, costVector));
        }

        List<Tuple2<CompoundFuzzyState, Double>> retList = new ArrayList<>();
        final Tuple2<CompoundFuzzyState, Double> tuple2 = PopulationGenerator.selectByEvaluatedFitnessPosition(chromosomeElementCostFunction, candidates, position, order);
        retList.add(tuple2);

        final CompoundFuzzyState selectedTargetState = tuple2._1;
        position = (position + 1) % 2;
        if(! terminalFuzzyStates.contains(selectedTargetState)){
            final List<Tuple2<CompoundFuzzyState, Double>> recursiveList
                    = getAlternatingNearestNeighbourRecursively(position, chromosomeElementCostFunction, selectedTargetState, terminalFuzzyStates, order);

            retList.addAll(recursiveList);
        }
        return retList;
    }

}
