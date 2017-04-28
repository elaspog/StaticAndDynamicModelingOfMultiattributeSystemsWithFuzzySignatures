package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.TestBaseForCompoundAutomatonAndOptimization;
import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
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

    private static FuzzyAutomaton fuzzyAutomatonForOptimization;
    private static CompoundFuzzyAutomaton compoundFuzzyAutomatonForOptimization;

    @BeforeClass
    public static void runOnceBeforeClass() {

        fuzzyAutomatonForOptimization = initializeTestFuzzyAutomatonForOptimization();
        compoundFuzzyAutomatonForOptimization = new CompoundFuzzyAutomaton(fuzzyAutomatonForOptimization);

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

    public static Double calculateCost(List<List<Double>> costData) {

        final List<Double> components_0 = costData.stream().filter(v -> v != null).map(v -> v.get(0)).collect(Collectors.toList());
        final List<Double> components_1 = costData.stream().filter(v -> v != null).map(v -> v.get(1)).collect(Collectors.toList());

        final double max = Collections.max(components_0).doubleValue();
        final double sum = components_1.stream().mapToDouble(Double::doubleValue).sum();

        return max * sum;
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton1() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                        compoundFuzzyAutomaton234,
                        null,
                        DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                        FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList = dbmea.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(initialPopulationPairedWithFitnessList);

        final List<CompoundFuzzyState> chromosomeSequence = initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence();
        Assert.assertEquals(chromosomeSequence.get(0).getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(chromosomeSequence.get(chromosomeSequence.size()-1).getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton2() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList = dbmea.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(initialPopulationPairedWithFitnessList);

        final List<CompoundFuzzyState> chromosomeSequence = initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence();
        Assert.assertEquals(chromosomeSequence.get(0).getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(chromosomeSequence.get(chromosomeSequence.size()-1).getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton3() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_NN_START, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList = dbmea.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(initialPopulationPairedWithFitnessList);

        final List<CompoundFuzzyState> chromosomeSequence = initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence();
        Assert.assertEquals(chromosomeSequence.get(0).getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(chromosomeSequence.get(chromosomeSequence.size()-1).getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }

    @Test
    public void testGenerateInitialPopulationSubProcessOnCompoundAutomaton4() throws Exception {

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomaton234,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCost,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_SNN_START, 1);
        dbmea.generateInitialPopulationSubProcess(populationInitializationPlan);

        final List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList = dbmea.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(initialPopulationPairedWithFitnessList);

        final List<CompoundFuzzyState> chromosomeSequence = initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence();
        Assert.assertEquals(chromosomeSequence.get(0).getAggregatedStateType(), FuzzyStateTypeEnum.INITIAL);
        Assert.assertEquals(chromosomeSequence.get(chromosomeSequence.size()-1).getAggregatedStateType(), FuzzyStateTypeEnum.TERMINAL);
    }


    public static Double calculateCostSimple(List<List<Double>> costData) {

        return costData.stream()
                .flatMap(x -> x.stream())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static Double getCost(List<Double> costData) {

        List<List<Double>> backLead = new ArrayList<>();
        backLead.add(costData);
        return calculateCostSimple(backLead);
    }

    @Test
    public void test_InitializationStrategy_NEAREST_NEIGHBOUR() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_max_initialPopulationPairedWithFitnessList;
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_min_initialPopulationPairedWithFitnessList;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        dbmea_max_initialPopulationPairedWithFitnessList = dbmea_max.getInitialPopulationPairedWithFitnessList();
        dbmea_min_initialPopulationPairedWithFitnessList = dbmea_min.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_max_initialPopulationPairedWithFitnessList);
        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_min_initialPopulationPairedWithFitnessList);

        Assert.assertEquals(4, dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(4, dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(9.0, getCost(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
        Assert.assertEquals(3.0, getCost(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_SECONDARY_NEAREST_NEIGHBOUR() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_max_initialPopulationPairedWithFitnessList;
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_min_initialPopulationPairedWithFitnessList;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        dbmea_max_initialPopulationPairedWithFitnessList = dbmea_max.getInitialPopulationPairedWithFitnessList();
        dbmea_min_initialPopulationPairedWithFitnessList = dbmea_min.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_max_initialPopulationPairedWithFitnessList);
        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_min_initialPopulationPairedWithFitnessList);

        Assert.assertEquals(4, dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(4, dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(6.0, getCost(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
        Assert.assertEquals(6.0, getCost(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_ALTERNATING_NEAREST_NEIGHBOUR_NN_START() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_max_initialPopulationPairedWithFitnessList;
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_min_initialPopulationPairedWithFitnessList;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_NN_START, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        dbmea_max_initialPopulationPairedWithFitnessList = dbmea_max.getInitialPopulationPairedWithFitnessList();
        dbmea_min_initialPopulationPairedWithFitnessList = dbmea_min.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_max_initialPopulationPairedWithFitnessList);
        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_min_initialPopulationPairedWithFitnessList);

        Assert.assertEquals(4, dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(4, dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(7.0, getCost(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
        Assert.assertEquals(5.0, getCost(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_ALTERNATING_NEAREST_NEIGHBOUR_SNN_START() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_max_initialPopulationPairedWithFitnessList;
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_min_initialPopulationPairedWithFitnessList;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_SNN_START, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan);

        dbmea_max_initialPopulationPairedWithFitnessList = dbmea_max.getInitialPopulationPairedWithFitnessList();
        dbmea_min_initialPopulationPairedWithFitnessList = dbmea_min.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_max_initialPopulationPairedWithFitnessList);
        //PrintHelpers.printIndividualAndFitnessPairs(dbmea_min_initialPopulationPairedWithFitnessList);

        Assert.assertEquals(4, dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(4, dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size());
        Assert.assertEquals(8.0, getCost(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
        Assert.assertEquals(4.0, getCost(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._2), Double.MIN_VALUE);
    }

    @Test
    public void test_InitializationStrategy_RANDOM() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_max_initialPopulationPairedWithFitnessList;
        List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> dbmea_min_initialPopulationPairedWithFitnessList;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS);

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_min
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                null,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MINIMIZE_FITNESS);

        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);
        dbmea_min.generateInitialPopulationSubProcess(populationInitializationPlan, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        dbmea_max_initialPopulationPairedWithFitnessList = dbmea_max.getInitialPopulationPairedWithFitnessList();
        dbmea_min_initialPopulationPairedWithFitnessList = dbmea_min.getInitialPopulationPairedWithFitnessList();

        //PrintHelpers.printIndividualAndFitnessSteps(dbmea_max_initialPopulationPairedWithFitnessList);
        //PrintHelpers.printIndividualAndFitnessSteps(dbmea_min_initialPopulationPairedWithFitnessList);

        Assert.assertTrue(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size() >= 0);
        Assert.assertTrue(dbmea_max_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size() <= 8);
        Assert.assertTrue(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size() >= 0);
        Assert.assertTrue(dbmea_min_initialPopulationPairedWithFitnessList.get(0)._1.getChromosomeSequence().size() <= 8);
    }

}