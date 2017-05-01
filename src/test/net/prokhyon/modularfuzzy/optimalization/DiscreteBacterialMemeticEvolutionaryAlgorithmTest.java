package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.TestBaseForCompoundAutomatonAndOptimization;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyState;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;


public class DiscreteBacterialMemeticEvolutionaryAlgorithmTest extends TestBaseForCompoundAutomatonAndOptimization {

    static CompoundFuzzyAutomaton compoundFuzzyAutomaton234;
    static CompoundFuzzyAutomaton compoundFuzzyAutomaton243;
    static CompoundFuzzyAutomaton compoundFuzzyAutomaton324;
    static CompoundFuzzyAutomaton compoundFuzzyAutomaton342;
    static CompoundFuzzyAutomaton compoundFuzzyAutomaton423;
    static CompoundFuzzyAutomaton compoundFuzzyAutomaton432;

    private static FuzzyAutomaton fuzzyAutomatonForOptimization1;
    private static FuzzyAutomaton fuzzyAutomatonForOptimization2;
    private static CompoundFuzzyAutomaton compoundFuzzyAutomatonForOptimization1;
    private static CompoundFuzzyAutomaton compoundFuzzyAutomatonForOptimization2;

    @BeforeClass
    public static void runOnceBeforeClass() {

        fuzzyAutomatonForOptimization1 = initializeTestFuzzyAutomatonForOptimization1();
        fuzzyAutomatonForOptimization2 = initializeTestFuzzyAutomatonForOptimization2();
        compoundFuzzyAutomatonForOptimization1 = new CompoundFuzzyAutomaton(fuzzyAutomatonForOptimization1);
        compoundFuzzyAutomatonForOptimization2 = new CompoundFuzzyAutomaton(fuzzyAutomatonForOptimization2);

        testFuzzyAutomaton_2node = initializeTestFuzzyAutomaton_2_node();
        testFuzzyAutomaton_3node = initializeTestFuzzyAutomaton_3_node();
        testFuzzyAutomaton_4node = initializeTestFuzzyAutomaton_4_node();

        compoundFuzzyAutomaton234 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton243 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton324 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton342 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton423 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_4node);
        compoundFuzzyAutomaton432 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton234.extendExistingCompoundWith(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton234.extendExistingCompoundWith(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton243.extendExistingCompoundWith(testFuzzyAutomaton_4node);
        compoundFuzzyAutomaton243.extendExistingCompoundWith(testFuzzyAutomaton_3node);

        compoundFuzzyAutomaton324.extendExistingCompoundWith(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton324.extendExistingCompoundWith(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton342.extendExistingCompoundWith(testFuzzyAutomaton_4node);
        compoundFuzzyAutomaton342.extendExistingCompoundWith(testFuzzyAutomaton_2node);

        compoundFuzzyAutomaton423.extendExistingCompoundWith(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton423.extendExistingCompoundWith(testFuzzyAutomaton_3node);

        compoundFuzzyAutomaton432.extendExistingCompoundWith(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton432.extendExistingCompoundWith(testFuzzyAutomaton_2node);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton0() throws Exception {

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 1.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        dbmea.generateInitialPopulationSubProcess();
        List<Individual<CompoundFuzzyState>> population = dbmea.getInitialPopulation();

        Assert.assertEquals(dbmea.DEFAULT_POPULATION_SIZE, population.size());
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton1() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                        compoundFuzzyAutomaton234,
                        null,
                        DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                        FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<CompoundFuzzyState> chromosomeSequence = dbmea.getInitialPopulation().get(0).getChromosomeSequence();

        CompoundFuzzyState firstNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .get();

        CompoundFuzzyState lastNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> b)
                .get();

        Assert.assertEquals(firstNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(lastNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton2() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<CompoundFuzzyState> chromosomeSequence = dbmea.getInitialPopulation().get(0).getChromosomeSequence();

        CompoundFuzzyState firstNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .get();

        CompoundFuzzyState lastNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> b)
                .get();

        Assert.assertEquals(firstNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(lastNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton3() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_NN_START, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<CompoundFuzzyState> chromosomeSequence = dbmea.getInitialPopulation().get(0).getChromosomeSequence();

        CompoundFuzzyState firstNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .get();

        CompoundFuzzyState lastNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> b)
                .get();

        Assert.assertEquals(firstNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(lastNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton4() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_SNN_START, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<CompoundFuzzyState> chromosomeSequence = dbmea.getInitialPopulation().get(0).getChromosomeSequence();

        CompoundFuzzyState firstNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .findFirst()
                .get();

        CompoundFuzzyState lastNotNullCompoundFuzzyState = chromosomeSequence.stream()
                .filter(Objects::nonNull)
                .reduce((a, b) -> b)
                .get();

        Assert.assertEquals(firstNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(lastNotNullCompoundFuzzyState.getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void test_InitializationStrategy_NEAREST_NEIGHBOUR() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 1.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);

        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea_max.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea_min.getInitialPopulation().get(0);

        List<Double> costSequenceEvaluatedWithCostFunction1 = dbmea_max.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual1, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);
        List<Double> costSequenceEvaluatedWithCostFunction2 = dbmea_min.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual2, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);

        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual1.getChromosomeSequence().size());
        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual2.getChromosomeSequence().size());
        Assert.assertEquals(9.0, getCost(costSequenceEvaluatedWithCostFunction1), Double.MIN_VALUE);
        Assert.assertEquals(3.0, getCost(costSequenceEvaluatedWithCostFunction2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_SECONDARY_NEAREST_NEIGHBOUR() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 1.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);

        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea_max.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea_min.getInitialPopulation().get(0);

        List<Double> costSequenceEvaluatedWithCostFunction1 = dbmea_max.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual1, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);
        List<Double> costSequenceEvaluatedWithCostFunction2 = dbmea_min.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual2, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);

        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual1.getChromosomeSequence().size());
        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual2.getChromosomeSequence().size());
        Assert.assertEquals(6.0, getCost(costSequenceEvaluatedWithCostFunction1), Double.MIN_VALUE);
        Assert.assertEquals(6.0, getCost(costSequenceEvaluatedWithCostFunction2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_ALTERNATING_NEAREST_NEIGHBOUR_NN_START() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.5;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 1.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_NN_START, 1);

        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea_max.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea_min.getInitialPopulation().get(0);

        List<Double> costSequenceEvaluatedWithCostFunction1 = dbmea_max.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual1, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);
        List<Double> costSequenceEvaluatedWithCostFunction2 = dbmea_min.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual2, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);

        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual1.getChromosomeSequence().size());
        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual2.getChromosomeSequence().size());
        Assert.assertEquals(7.0, getCost(costSequenceEvaluatedWithCostFunction1), Double.MIN_VALUE);
        Assert.assertEquals(5.0, getCost(costSequenceEvaluatedWithCostFunction2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_ALTERNATING_NEAREST_NEIGHBOUR_SNN_START() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.5;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 1.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_SNN_START, 1);

        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea_max.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea_min.getInitialPopulation().get(0);

        List<Double> costSequenceEvaluatedWithCostFunction1 = dbmea_max.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual1, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);
        List<Double> costSequenceEvaluatedWithCostFunction2 = dbmea_min.getEvolutionarilyOptimizable()
                .getCostSequenceEvaluatedWithCostFunction(individual2, DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple);

        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual1.getChromosomeSequence().size());
        Assert.assertEquals(compoundFuzzyAutomatonForOptimization1.getCompoundFuzzyStates().size(), individual2.getChromosomeSequence().size());
        Assert.assertEquals(8.0, getCost(costSequenceEvaluatedWithCostFunction1), Double.MIN_VALUE);
        Assert.assertEquals(4.0, getCost(costSequenceEvaluatedWithCostFunction2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_RANDOM() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.5;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea_max.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea_min.getInitialPopulation().get(0);

        Assert.assertTrue(individual1.getChromosomeSequence().size() >= 0);
        Assert.assertTrue(individual1.getChromosomeSequence().size() <= 8);
        Assert.assertTrue(individual2.getChromosomeSequence().size() >= 0);
        Assert.assertTrue(individual2.getChromosomeSequence().size() <= 8);
    }

    @Test
    public void test_geneSequenceChanger() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.0;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> compoundFuzzyStateIndividual = dbmea_max.getInitialPopulation().get(0);
        List<CompoundFuzzyState> reversedChromosomeSequence = new ArrayList<>(compoundFuzzyStateIndividual.getChromosomeSequence().subList(3,6));
        Collections.reverse(reversedChromosomeSequence);

        Assert.assertEquals(3, reversedChromosomeSequence.size());

        Individual<CompoundFuzzyState> newIndividual
                = dbmea_max.geneSequenceChanger(compoundFuzzyStateIndividual, 3, 6, reversedChromosomeSequence);

        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(0), newIndividual.getChromosomeSequence().get(0));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(1), newIndividual.getChromosomeSequence().get(1));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(2), newIndividual.getChromosomeSequence().get(2));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(3), newIndividual.getChromosomeSequence().get(5));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(4), newIndividual.getChromosomeSequence().get(4));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(5), newIndividual.getChromosomeSequence().get(3));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(6), newIndividual.getChromosomeSequence().get(6));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(7), newIndividual.getChromosomeSequence().get(7));

        Assert.assertEquals(8, newIndividual.getChromosomeSequence().size());
        Assert.assertEquals(8, compoundFuzzyStateIndividual.getChromosomeSequence().size());
    }

    @Test
    public void test_modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.0;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization1,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> compoundFuzzyStateIndividual = dbmea_max.getInitialPopulation().get(0);
        List<CompoundFuzzyState> reversedChromosomeSequence = new ArrayList<>(compoundFuzzyStateIndividual.getChromosomeSequence().subList(3,6));
        Collections.reverse(reversedChromosomeSequence);

        List<Integer> indices = new ArrayList<>();
        indices.add(3);
        indices.add(4);
        indices.add(5);

        Assert.assertEquals(3, reversedChromosomeSequence.size());

        Individual<CompoundFuzzyState> newIndividual
                = dbmea_max.modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements(compoundFuzzyStateIndividual, indices, reversedChromosomeSequence);

        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(0), newIndividual.getChromosomeSequence().get(0));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(1), newIndividual.getChromosomeSequence().get(1));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(2), newIndividual.getChromosomeSequence().get(2));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(3), newIndividual.getChromosomeSequence().get(5));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(4), newIndividual.getChromosomeSequence().get(4));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(5), newIndividual.getChromosomeSequence().get(3));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(6), newIndividual.getChromosomeSequence().get(6));
        Assert.assertEquals(compoundFuzzyStateIndividual.getChromosomeSequence().get(7), newIndividual.getChromosomeSequence().get(7));

        Assert.assertEquals(8, newIndividual.getChromosomeSequence().size());
        Assert.assertEquals(8, compoundFuzzyStateIndividual.getChromosomeSequence().size());
    }

    @Test
    public void test_geneTransferOnIndividual() throws Exception {

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization2,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);

        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea.getInitialPopulation().get(1);

        List<CompoundFuzzyState> chromosomeSequence1 = individual1.getChromosomeSequence().stream().filter(Objects::nonNull).collect(Collectors.toList());
        List<CompoundFuzzyState> chromosomeSequence2 = individual2.getChromosomeSequence().stream().filter(Objects::nonNull).collect(Collectors.toList());

        Assert.assertTrue(chromosomeSequence1.get(0).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence1.get(1).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence1.get(2).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence1.get(3).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence2.get(0).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence2.get(1).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence2.get(2).getFuzzyStateTuple().size() == 1);
        Assert.assertTrue(chromosomeSequence2.get(3).getFuzzyStateTuple().size() == 1);

        Assert.assertTrue(chromosomeSequence1.get(0).getFuzzyStateTuple().contains(o2s00));
        Assert.assertTrue(chromosomeSequence1.get(1).getFuzzyStateTuple().contains(o2s11));
        Assert.assertTrue(chromosomeSequence1.get(2).getFuzzyStateTuple().contains(o2s21));
        Assert.assertTrue(chromosomeSequence1.get(3).getFuzzyStateTuple().contains(o2s40));

        Assert.assertTrue(chromosomeSequence2.get(0).getFuzzyStateTuple().contains(o2s00));
        Assert.assertTrue(chromosomeSequence2.get(1).getFuzzyStateTuple().contains(o2s12));
        Assert.assertTrue(chromosomeSequence2.get(2).getFuzzyStateTuple().contains(o2s22));
        Assert.assertTrue(chromosomeSequence2.get(3).getFuzzyStateTuple().contains(o2s40));

        Individual<CompoundFuzzyState> newIndividual = dbmea.geneTransferOnIndividual(individual1, individual2, 4, 2, 3);

        /// there are too many random factor in chromosome to check more
        Assert.assertNotEquals(newIndividual, individual1);
        Assert.assertNotEquals(newIndividual, individual2);
    }

    @Test
    public void test_randomAttributeGroupMutationOnIndividual() throws Exception {

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization2,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        List<Integer> permutation = new ArrayList<>(Arrays.asList(8, 1, 3, 0, 11, 4, 9, 6, 2, 7, 10, 5));

        Individual<CompoundFuzzyState> individual1 = dbmea.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea.randomAttributeGroupMutationOnIndividual(individual1, 4, 20, permutation);

        double v1 = dbmea.calculateFitnessValueOfIndividual(individual1);
        double v2 = dbmea.calculateFitnessValueOfIndividual(individual2);

        Assert.assertTrue(v2 >= v1);
    }

    @Test
    public void test_looseSegmentMutation() throws Exception {

        final Double PROBABILITY_OF_NULL_STATE = 0.00;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization2,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new LinkedHashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> individual1 = dbmea.getInitialPopulation().get(0);
        Individual<CompoundFuzzyState> individual2 = dbmea.looseSegmentMutationOnIndividual(individual1, 5);

        double v1 = dbmea.calculateFitnessValueOfIndividual(individual1);
        double v2 = dbmea.calculateFitnessValueOfIndividual(individual2);

        Assert.assertTrue(v2 >= v1);
    }

}