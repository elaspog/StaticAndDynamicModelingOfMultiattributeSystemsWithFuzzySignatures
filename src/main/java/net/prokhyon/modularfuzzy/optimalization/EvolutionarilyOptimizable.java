package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.common.utils.Tuple3;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessFunction;

import java.util.List;
import java.util.Map;

public interface EvolutionarilyOptimizable <CHROMOSOME_TYPE extends ChromosomeElement, COST_TYPE> {

    List<Individual<CHROMOSOME_TYPE>> initializePopulation(Map<IndividualInitializationType, Integer> populationInitializationPlan,
                                                           ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                           FitnessFunction<COST_TYPE, CHROMOSOME_TYPE> fitnessFunction,
                                                           FitnessEvaluationStrategy fitnessEvaluationStrategy,
                                                           Object ... domainSpecificConfiguration);

    Integer getCountOfPossibleChromosomeElements();

    List<COST_TYPE> getCostSequence(Individual<CHROMOSOME_TYPE> individual);

    List<Double> getCostSequenceEvaluatedWithCostFunction(Individual<CHROMOSOME_TYPE> individual,
                                                          ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction);

    Double getFitness(Individual<CHROMOSOME_TYPE> individual,
                      FitnessFunction<COST_TYPE, CHROMOSOME_TYPE> fitnessFunction,
                      ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction);

    Tuple3<List<COST_TYPE>, List<Double>, Double> getCostSequenceEvaluatedAndCostSequenceAndFitness(Individual<CHROMOSOME_TYPE> individual,
                                                                                                    FitnessFunction<COST_TYPE,CHROMOSOME_TYPE> fitnessFunction,
                                                                                                    ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction);

}
