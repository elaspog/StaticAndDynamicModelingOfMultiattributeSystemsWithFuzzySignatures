package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.CriterionFunctionStrategy;

import java.util.List;

public interface EvolutionaryAlgorithm <EVOLUTIONARILY_OPTIMIZABLE_TYPE extends EvolutionarilyOptimizable<CHROMOSOME_TYPE, COST_TYPE>,
                                        CHROMOSOME_TYPE extends ChromosomeElement,
                                        COST_TYPE> {

    EVOLUTIONARILY_OPTIMIZABLE_TYPE getEvolutionarilyOptimizable();

    int getCurrentIteration();

    void stepIteration();

    List<Double> getFitnessResultsByIteration(int iteration);

    CriterionFunctionStrategy getCriterionFunctionStrategy();

    List<Integer> getPossibleSegmentSizes();

    void evaluatePopulationAndStoreResults(List<Individual<CHROMOSOME_TYPE>> individualsToEvaluate);

    List<Individual<CHROMOSOME_TYPE>> selectPopulationByIteration(int generation);

    List<Individual<CHROMOSOME_TYPE>> selectLastPopulation();

    double calculateFitnessValueOfIndividual(Individual<CHROMOSOME_TYPE> individual);

}
