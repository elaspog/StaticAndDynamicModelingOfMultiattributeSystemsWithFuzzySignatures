package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.common.utils.Tuple3;
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
import net.prokhyon.modularfuzzy.optimalization.utils.DbmeaHelperUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CompoundFuzzyAutomaton
        implements EvolutionarilyOptimizable<CompoundFuzzyState, List<List<Double>>> {

    private List<FuzzyAutomaton> fuzzyAutomatonTuple;
    private List<CompoundFuzzyState> compoundFuzzyStates;
    private List<CompoundFuzzyTransition> compoundFuzzyTransitions;
    private List<Object> domainSpecificConfiguration;

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
    public List<Individual<CompoundFuzzyState>> initializePopulation(Map<IndividualInitializationType, Integer> populationInitializationPlan,
                                                                     ChromosomeElementCostFunction<List<List<Double>>> chromosomeElementCostFunction,
                                                                     FitnessFunction<List<List<Double>>, CompoundFuzzyState> fitnessFunction,
                                                                     FitnessEvaluationStrategy fitnessEvaluationStrategy,
                                                                     Object... domainSpecificConfiguration) {

        this.domainSpecificConfiguration = Arrays.asList(domainSpecificConfiguration);

        /// necessary only if populationInitializationPlan contains any not RANDOM type initialization
        // TODO check if necessary
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
            List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> collect = IntStream.range(0, count)
                    .mapToObj(i -> generateChromosome(individualType, chromosomeElementCostFunction, initialFuzzyStates, terminalFuzzyStates, finalOrder, domainSpecificConfiguration))
                    .collect(Collectors.toList());

            initialPopulationWithFitness.addAll(collect);
        }
        return initialPopulationWithFitness.stream()
                .map(x -> x._1)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getCountOfPossibleChromosomeElements() {

        return compoundFuzzyStates.size();
    }

    @Override
    public List<List<List<Double>>> getCostSequence(Individual<CompoundFuzzyState> individual) {

        List<List<List<Double>>> ret = new ArrayList<>();

        for (int i = 0; i < individual.getChromosomeSequence().size() - 1; i++) {

            CompoundFuzzyState actual = individual.getChromosomeSequence().get(i);
            Optional<CompoundFuzzyState> next = individual.getChromosomeSequence().subList(i+1, individual.getChromosomeSequence().size()).stream()
                    .filter(Objects::nonNull)
                    .findFirst();

            if(actual != null && next.isPresent()){

                final CompoundFuzzyState subsequentCompoundFuzzyState = next.get();
                final List<CompoundFuzzyState> subsequentCompoundFuzzyStates = actual.getOutgoingEdges().stream()
                        .map(x -> x.getToState())
                        .collect(Collectors.toList());

                if (subsequentCompoundFuzzyStates.contains(subsequentCompoundFuzzyState)){

                    /// calculate the cost of the edge when the selected state is connected to the found subsequent state

                    final Optional<CompoundFuzzyTransition> fuzzyTransition = actual.getOutgoingEdges().stream()
                            .filter(x -> x.getToState().equals(subsequentCompoundFuzzyState))
                            .findFirst();

                    if (fuzzyTransition.isPresent()){

                        final List<List<Double>> costVector = fuzzyTransition.get().getCostVector();
                        ret.add(costVector);

                    } else {
                        throw new RuntimeException("Error in algorithm");
                    }

                } else {

                    ret.add(null);
                }
            }
        }
        return ret;
    }

    @Override
    public List<Double> getCostSequenceEvaluatedWithCostFunction(Individual<CompoundFuzzyState> individual,
                                                                 ChromosomeElementCostFunction<List<List<Double>>> chromosomeElementCostFunction) {

        List<Double> ret = new ArrayList<>();
        Double disconnectionCost = (Double) this.domainSpecificConfiguration.get(1);

        for (int i = 0; i < individual.getChromosomeSequence().size() - 1; i++) {

            CompoundFuzzyState actual = individual.getChromosomeSequence().get(i);
            Optional<CompoundFuzzyState> next = individual.getChromosomeSequence().subList(i+1, individual.getChromosomeSequence().size()).stream()
                    .filter(Objects::nonNull)
                    .findFirst();

            if(actual != null && next.isPresent()){

                final CompoundFuzzyState subsequentCompoundFuzzyState = next.get();
                final List<CompoundFuzzyState> subsequentCompoundFuzzyStates = actual.getOutgoingEdges().stream()
                        .map(x -> x.getToState())
                        .collect(Collectors.toList());

                if (subsequentCompoundFuzzyStates.contains(subsequentCompoundFuzzyState)){

                    /// calculate the cost of the edge when the selected state is connected to the found subsequent state

                    final Optional<CompoundFuzzyTransition> fuzzyTransition = actual.getOutgoingEdges().stream()
                            .filter(x -> x.getToState().equals(subsequentCompoundFuzzyState))
                            .findFirst();

                    if (fuzzyTransition.isPresent()){

                        final List<List<Double>> costVector = fuzzyTransition.get().getCostVector();
                        ret.add(chromosomeElementCostFunction.calculateCost(costVector));

                    } else {
                        throw new RuntimeException("Error in algorithm");
                    }

                } else {

                    /// generate a cost when the selected state is not connected to the found subsequent state
                    Integer pathLength = getShortestPathLengthBetweenStatesByBFS(actual, subsequentCompoundFuzzyState);
                    int multiplicator = pathLength == null ? compoundFuzzyStates.size() : pathLength;
                    ret.add(multiplicator * disconnectionCost);
                }
            }
        }
        return ret;
    }

    @Override
    public Double getFitness(Individual<CompoundFuzzyState> individual,
                             FitnessFunction<List<List<Double>>, CompoundFuzzyState> fitnessFunction,
                             ChromosomeElementCostFunction<List<List<Double>>> chromosomeElementCostFunction) {

        /// this function could be much more complex
        List<Double> costSequenceEvaluatedWithCostFunction = getCostSequenceEvaluatedWithCostFunction(individual, chromosomeElementCostFunction);
        return costSequenceEvaluatedWithCostFunction.stream().mapToDouble(x -> x).sum();
    }

    @Override
    public Tuple3<List<List<List<Double>>>, List<Double>, Double> getCostSequenceEvaluatedAndCostSequenceAndFitness(Individual<CompoundFuzzyState> individual,
                                                                                                              FitnessFunction<List<List<Double>>, CompoundFuzzyState> fitnessFunction,
                                                                                                              ChromosomeElementCostFunction<List<List<Double>>> chromosomeElementCostFunction) {

        List<List<List<Double>>> costSequence = getCostSequence(individual);
        List<Double> costSequenceEvaluatedWithCostFunction = getCostSequenceEvaluatedWithCostFunction(individual, chromosomeElementCostFunction);
        Double fitness = getFitness(individual, fitnessFunction, chromosomeElementCostFunction);

        return new Tuple3<>(costSequence, costSequenceEvaluatedWithCostFunction, fitness);
    }

    private Tuple2<Individual<CompoundFuzzyState>, List<Double>> generateChromosome(IndividualInitializationType individualType,
                                                                                    ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                                    List<CompoundFuzzyState> initialFuzzyStates,
                                                                                    List<CompoundFuzzyState> terminalFuzzyStates,
                                                                                    Order order,
                                                                                    Object[] domainSpecificConfiguration) {

        CompoundFuzzyState randomlySelectedInitState;
        List<Tuple2<CompoundFuzzyState, Double>> stateAndFitnessSequence;
        Individual<CompoundFuzzyState> newIndividual = null;
        List<CompoundFuzzyState> stateSequence;
        List<Double> costSequence;

        switch (individualType){

            case NEAREST_NEIGHBOUR:
                randomlySelectedInitState = DbmeaHelperUtils.selectRandomElementFromList(initialFuzzyStates);
                stateAndFitnessSequence = getNthNearestNeighbourRecursively(0, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
                costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());
                extendChromosomeWithNullsIfNecessary(randomlySelectedInitState, stateSequence, costSequence);
                break;

            case SECONDARY_NEAREST_NEIGHBOUR:
                randomlySelectedInitState = DbmeaHelperUtils.selectRandomElementFromList(initialFuzzyStates);
                stateAndFitnessSequence = getNthNearestNeighbourRecursively(1, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
                costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());
                extendChromosomeWithNullsIfNecessary(randomlySelectedInitState, stateSequence, costSequence);
                break;

            case ALTERNATING_NEAREST_NEIGHBOUR_NN_START:
                randomlySelectedInitState = DbmeaHelperUtils.selectRandomElementFromList(initialFuzzyStates);
                stateAndFitnessSequence = getAlternatingNearestNeighbourRecursively(0, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
                costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());
                extendChromosomeWithNullsIfNecessary(randomlySelectedInitState, stateSequence, costSequence);
                break;

            case ALTERNATING_NEAREST_NEIGHBOUR_SNN_START:
                randomlySelectedInitState = DbmeaHelperUtils.selectRandomElementFromList(initialFuzzyStates);
                stateAndFitnessSequence = getAlternatingNearestNeighbourRecursively(1, chromosomeElementCostFunction, randomlySelectedInitState, terminalFuzzyStates, order);
                stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
                costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());
                extendChromosomeWithNullsIfNecessary(randomlySelectedInitState, stateSequence, costSequence);
                break;

            case RANDOM:
                if (domainSpecificConfiguration.length != 2){
                    throw new IllegalArgumentException("Bad parametrization for generating random objects.");
                }
                final Double probabilityOfEmptyChromosomeElements = (Double) domainSpecificConfiguration[0];
                final Double disconnectionCost = (Double) domainSpecificConfiguration[1];

                final int size = compoundFuzzyStates.size();
                if (size > 1 && probabilityOfEmptyChromosomeElements <= 1.0 && probabilityOfEmptyChromosomeElements >= 0.0) {
                    stateAndFitnessSequence = getRandomPathRecursively(probabilityOfEmptyChromosomeElements, size, chromosomeElementCostFunction, disconnectionCost);
                } else {
                    throw new IllegalArgumentException("The following parameters can't be passed to getRandomPathRecursively(): " + size + " " + probabilityOfEmptyChromosomeElements);
                }
                stateSequence = stateAndFitnessSequence.stream().map(x -> x._1).collect(Collectors.toList());
                costSequence = stateAndFitnessSequence.stream().map(x -> x._2).collect(Collectors.toList());
                break;

            case DOMAIN_SPECIFIC_CONSTRAINT_BASED:
            default:
                throw new IllegalArgumentException("IndividualType not supported: " + individualType.toString());
        }

        if (stateSequence != null){
            newIndividual = new Individual<>(stateSequence);
        }

        if(newIndividual != null) {
            return new Tuple2<>(newIndividual, costSequence);
        }
        return null;
    }

    private void extendChromosomeWithNullsIfNecessary(CompoundFuzzyState initState, List<CompoundFuzzyState> stateSequence, List<Double> costSequence) {

        if (stateSequence != null){
            stateSequence.add(0, initState);
        }

        Random randomizer = new Random();
        while (compoundFuzzyStates.size() - stateSequence.size() > 0){

            int insertPosition = randomizer.nextInt(stateSequence.size() + 1);
            stateSequence.add(insertPosition, null);

            if (insertPosition <= 1)
                costSequence.add(0, null);
            else
                costSequence.add(insertPosition-1, null);
        }
    }


    private List<Tuple2<CompoundFuzzyState, Double>> getRandomPathRecursively(final double probabilityOfEmptyChromosomeElements,
                                                                              final int iterationCounter,
                                                                              ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                              Double domainSpecificConfiguration) {

        List<Tuple2<CompoundFuzzyState, Double>> randomPathRecursively = null;
        if (iterationCounter != 1){

            randomPathRecursively = getRandomPathRecursively(probabilityOfEmptyChromosomeElements, iterationCounter - 1, chromosomeElementCostFunction, domainSpecificConfiguration);

        } else if (randomPathRecursively == null) {

            /// returned array is initialized in the last call of recursion
            randomPathRecursively = new ArrayList<>();
        }

        Tuple2<CompoundFuzzyState, Double> elementInPosition;
        final double generatedProbability = new Random().nextDouble();
        if (generatedProbability <= probabilityOfEmptyChromosomeElements){

            /// actual element of the chromosome will be a special null state
            elementInPosition = new Tuple2<>(null, null);

        } else {

            final List<CompoundFuzzyState> alreadyStoredNotEmptyChromosomeElements = randomPathRecursively.stream()
                    .filter(pair -> pair._1 != null)
                    .map(x -> x._1)
                    .collect(Collectors.toList());

            final Optional<CompoundFuzzyState> nextStateToCompareWith = alreadyStoredNotEmptyChromosomeElements.stream()
                    .findFirst();

            /// get previously not chosen states
            final List<CompoundFuzzyState> remainderStates = new ArrayList<>(compoundFuzzyStates.subList(0, compoundFuzzyStates.size()));
            remainderStates.removeAll(alreadyStoredNotEmptyChromosomeElements);

            /// get a random element from previously not chosen states
            CompoundFuzzyState selectedCompoundFuzzyState = null;
            if (remainderStates.size() > 0) {
                selectedCompoundFuzzyState = DbmeaHelperUtils.selectRandomElementFromList(remainderStates);
            } else {
                throw new RuntimeException("Error in algorithm");
            }

            Double calculatedCost = null;

            /// calculate cost if there is a valid subsequent (not a special null) state
            if(nextStateToCompareWith.isPresent()){

                final List<CompoundFuzzyState> subsequentCompoundFuzzyStates = selectedCompoundFuzzyState.getOutgoingEdges().stream()
                        .map(x -> x.getToState())
                        .collect(Collectors.toList());

                final CompoundFuzzyState subsequentCompoundFuzzyState = nextStateToCompareWith.get();
                if (subsequentCompoundFuzzyStates.contains(subsequentCompoundFuzzyState)){

                    /// calculate the cost of the edge when the selected state is connected to the found subsequent state

                    final Optional<CompoundFuzzyTransition> fuzzyTransition = selectedCompoundFuzzyState.getOutgoingEdges().stream()
                            .filter(x -> x.getToState().equals(subsequentCompoundFuzzyState))
                            .findFirst();

                    if (fuzzyTransition.isPresent()){
                        final List<List<Double>> costVector = fuzzyTransition.get().getCostVector();
                        calculatedCost = chromosomeElementCostFunction.calculateCost(costVector);
                    } else {
                        throw new RuntimeException("Error in algorithm");
                    }

                } else {

                    /// generate a cost when the selected state is not connected to the found subsequent state
                    Integer pathLength1 = getShortestPathLengthBetweenStatesByBFS(selectedCompoundFuzzyState, subsequentCompoundFuzzyState);
                    //Integer pathLength2 = getShortestPathLengthBetweenStatesByDijkstra(selectedCompoundFuzzyState, subsequentCompoundFuzzyState);
                    //if (pathLength1 != pathLength2){
                    //    throw new RuntimeException("Error in algorithm");
                    //}

                    int multiplicator = pathLength1 == null ? compoundFuzzyStates.size() : pathLength1;
                    calculatedCost = multiplicator * domainSpecificConfiguration;
                }
            }

            elementInPosition = new Tuple2<>(selectedCompoundFuzzyState, calculatedCost);
        }

        randomPathRecursively.add(0, elementInPosition);
        return randomPathRecursively;
    }

    Integer getShortestPathLengthBetweenStatesByBFS(CompoundFuzzyState fromState, CompoundFuzzyState toState){

        return getShortestPathLengthBetweenStatesByBFS(fromState, toState, this.compoundFuzzyStates);
    }

    static Integer getShortestPathLengthBetweenStatesByBFS(CompoundFuzzyState fromState, CompoundFuzzyState toState, List<CompoundFuzzyState> compoundFuzzyStates) {

        HashMap<CompoundFuzzyState, Integer> resultMap = new HashMap<>();
        Queue<CompoundFuzzyState> queue = new LinkedList<>();
        Set<CompoundFuzzyState> output = new HashSet<>();
        queue.add(fromState);
        resultMap.put(fromState, 0);

        while (! queue.isEmpty()){

            CompoundFuzzyState elementToProcess = queue.poll();
            Integer storedDistanceOfElementToProcess = resultMap.get(elementToProcess);

            List<CompoundFuzzyState> neighbours = elementToProcess.getOutgoingEdges().stream()
                    .map(x -> x.getToState())
                    .collect(Collectors.toList());

            for (CompoundFuzzyState neighbour : neighbours) {
                if(! output.contains(neighbour) && ! queue.contains(neighbour)){
                    queue.add(neighbour);
                }
                Integer storedDistanceOfNeighbour = resultMap.get(neighbour);
                if (storedDistanceOfNeighbour == null){
                    resultMap.put(neighbour, storedDistanceOfElementToProcess + 1);
                } else {
                    int minDistance = Math.min(storedDistanceOfElementToProcess + 1, storedDistanceOfNeighbour);
                    resultMap.put(neighbour, minDistance);
                }
            }
            output.add(elementToProcess);
        }
        if (resultMap.containsKey(toState)) {
            return resultMap.get(toState);
        } else {
            return null;
        }
    }

    Integer getShortestPathLengthBetweenStatesByDijkstra(CompoundFuzzyState fromState, CompoundFuzzyState toState){

        return getShortestPathLengthBetweenStatesByDijkstra(fromState, toState, this.compoundFuzzyStates);
    }

    static Integer getShortestPathLengthBetweenStatesByDijkstra(CompoundFuzzyState fromState, CompoundFuzzyState toState, List<CompoundFuzzyState> compoundFuzzyStates) {

        /// Dijkstra algorithm (with cost=1 on edges)

        Table<CompoundFuzzyState, CompoundFuzzyState, Optional<Integer>> adjacencyMatrix_C = HashBasedTable.create();
        Map<CompoundFuzzyState, Optional<Integer>> array_D = new HashMap<>();
        List<CompoundFuzzyState> array_Ready = new ArrayList<>();
        array_Ready.add(fromState);

        /// initialize cost matrix with nulls
        for (CompoundFuzzyState source : compoundFuzzyStates) {
            for (CompoundFuzzyState target : compoundFuzzyStates) {
                if (source.equals(target)) {
                    /// source equals target target
                    adjacencyMatrix_C.put(source, target, Optional.of(0));
                } else {
                    adjacencyMatrix_C.put(source, target, Optional.ofNullable(null));
                }
            }
        }

        /// initialize cost matrix with costs
        for (CompoundFuzzyState source : compoundFuzzyStates) {
            final List<CompoundFuzzyTransition> outgoingEdges = source.getOutgoingEdges();
            for (CompoundFuzzyTransition outgoingEdge : outgoingEdges) {
                final CompoundFuzzyState target = outgoingEdge.getToState();
                if (! source.equals(target)) {
                    adjacencyMatrix_C.put(source, target, Optional.of(1));   /// cost = 1, to search the shortest length
                } else {
                    throw new RuntimeException("Error in automaton: loop edge is present on state: " + source.getAggregatedStateName());
                }
            }
        }

        /// initializing D array
        for (CompoundFuzzyState state : compoundFuzzyStates) {
            final Optional<Integer> C_value = adjacencyMatrix_C.get(fromState, state);
            array_D.put(state, C_value);
        }


        while(compoundFuzzyStates.size() != array_Ready.size()){

            /// get previously not chosen states
            final List<CompoundFuzzyState> notReadyStates1 = new ArrayList<>(compoundFuzzyStates.subList(0, compoundFuzzyStates.size()));
            notReadyStates1.removeAll(array_Ready);

            /// prepare data for minimal element search
            Map<Integer, List<CompoundFuzzyState>> toMinimalizeFromList = new HashMap<>();
            for (CompoundFuzzyState currentNotReadyState : notReadyStates1) {

                Optional<Integer> value_D = array_D.get(currentNotReadyState);

                if (! value_D.isPresent())
                    continue;

                List<CompoundFuzzyState> retrievedCompoundFuzzyState = toMinimalizeFromList.get(value_D.get());

                if(retrievedCompoundFuzzyState == null){
                    retrievedCompoundFuzzyState = new ArrayList<>();
                    retrievedCompoundFuzzyState.add(currentNotReadyState);
                    toMinimalizeFromList.put(value_D.get(), retrievedCompoundFuzzyState);
                } else {
                    retrievedCompoundFuzzyState.add(currentNotReadyState);
                }
            }

            /// if no more element to minimize, exit with value in D array
            if (toMinimalizeFromList.size() == 0) {
                Optional<Integer> retValue = array_D.get(toState);
                if (retValue.isPresent()){
                    return retValue.get();
                } else {
                    return null;
                }
            }

            /// selecting minimal element
            Tuple2<CompoundFuzzyState, Integer> minimalElementPairInArrayD = DbmeaHelperUtils.selectFromMultilistByPosition(0, Order.ASCENDING, toMinimalizeFromList);
            CompoundFuzzyState minimalElementInArrayD = minimalElementPairInArrayD._1;
            array_Ready.add(minimalElementInArrayD);

            /// updating D array
            final List<CompoundFuzzyState> notReadyStates2 = new ArrayList<>(compoundFuzzyStates.subList(0, compoundFuzzyStates.size()));
            notReadyStates2.removeAll(array_Ready);
            for (CompoundFuzzyState w : notReadyStates2) {
                Optional<Integer> D_w = array_D.get(w);
                Optional<Integer> D_x = array_D.get(minimalElementInArrayD);
                Optional<Integer> C_xw = adjacencyMatrix_C.get(minimalElementInArrayD, w);

                if (C_xw.isPresent()){
                    int min;
                    if(D_w.isPresent()){
                        min = Math.min(D_w.get(), D_x.get() + C_xw.get());
                    } else {
                        min = D_x.get() + C_xw.get();
                    }
                    array_D.put(w, Optional.of(min));
                } else {
                    array_D.put(w, D_w);
                }
            }
        }
        Optional<Integer> integer = array_D.get(toState);
        return integer.get();
    }

    static private List<Tuple2<CompoundFuzzyState, Double>> getNthNearestNeighbourRecursively(int position,
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
        final Tuple2<CompoundFuzzyState, Double> tuple2 = DbmeaHelperUtils.selectByEvaluatedFitnessPosition(chromosomeElementCostFunction, candidates, position, order);
        retList.add(tuple2);

        final CompoundFuzzyState selectedTargetState = tuple2._1;
        if(! terminalFuzzyStates.contains(selectedTargetState)){

            final List<Tuple2<CompoundFuzzyState, Double>> recursiveList
                    = getNthNearestNeighbourRecursively(position, chromosomeElementCostFunction, selectedTargetState, terminalFuzzyStates, order);

            retList.addAll(recursiveList);
        }
        return retList;
    }

    static private List<Tuple2<CompoundFuzzyState, Double>> getAlternatingNearestNeighbourRecursively(int position,
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
        final Tuple2<CompoundFuzzyState, Double> tuple2 = DbmeaHelperUtils.selectByEvaluatedFitnessPosition(chromosomeElementCostFunction, candidates, position, order);
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
