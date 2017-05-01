package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.TestBaseForCompoundAutomatonAndOptimization;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyState;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndividualTest extends TestBaseForCompoundAutomatonAndOptimization {

    private static FuzzyAutomaton fuzzyAutomatonForOptimization;
    private static CompoundFuzzyAutomaton compoundFuzzyAutomatonForOptimization;

    @BeforeClass
    public static void runOnceBeforeClass() {

        fuzzyAutomatonForOptimization = initializeTestFuzzyAutomatonForOptimization1();
        compoundFuzzyAutomatonForOptimization = new CompoundFuzzyAutomaton(fuzzyAutomatonForOptimization);
    }

    @Test
    public void test_equals() throws Exception {

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();

        final Double PROBABILITY_OF_NULL_STATE = 0.5;
        final Double GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH = 10.0;

        DiscreteBacterialMemeticEvolutionaryAlgorithm<CompoundFuzzyAutomaton, CompoundFuzzyState, List<List<Double>>> dbmea_max
                = new DiscreteBacterialMemeticEvolutionaryAlgorithm<>(
                compoundFuzzyAutomatonForOptimization,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::fitnessFunction,
                DiscreteBacterialMemeticEvolutionaryAlgorithmTest::calculateCostSimple,
                FitnessEvaluationStrategy.MAXIMIZE_FITNESS,
                PROBABILITY_OF_NULL_STATE, GAP_COST_PER_SEGMENT_OF_SHORTEST_PATH);

        populationInitializationPlan.clear();
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, 1);
        dbmea_max.generateInitialPopulationSubProcess(populationInitializationPlan);

        Individual<CompoundFuzzyState> compoundFuzzyStateIndividual = dbmea_max.getInitialPopulation().get(0);

        Individual<CompoundFuzzyState> individual1 = new Individual<>(new ArrayList<>(compoundFuzzyStateIndividual.getChromosomeSequence()));
        Individual<CompoundFuzzyState> individual2 = new Individual<>(new ArrayList<>(compoundFuzzyStateIndividual.getChromosomeSequence()));

        Assert.assertTrue(individual1.equals(individual2));
        Assert.assertTrue(individual2.equals(individual1));

        List<Individual<CompoundFuzzyState>> list = new ArrayList<>();
        list.add(individual1);
        Assert.assertTrue(list.contains(individual2));

        CompoundFuzzyState compoundFuzzyState = compoundFuzzyStateIndividual.getChromosomeSequence().get(4);
        compoundFuzzyStateIndividual.getChromosomeSequence().remove(compoundFuzzyStateIndividual);
        compoundFuzzyStateIndividual.getChromosomeSequence().add(0, compoundFuzzyState);

        Assert.assertTrue(individual1.equals(individual2));
        Assert.assertTrue(individual2.equals(individual1));

        individual2 = new Individual<>(new ArrayList<>(compoundFuzzyStateIndividual.getChromosomeSequence()));

        Assert.assertFalse(individual1.equals(individual2));
        Assert.assertFalse(individual2.equals(individual1));

        list = new ArrayList<>();
        list.add(individual1);
        Assert.assertFalse(list.contains(individual2));

    }

}