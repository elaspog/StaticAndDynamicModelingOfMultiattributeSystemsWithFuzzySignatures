package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyState;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessFunction;

import java.util.List;
import java.util.Map;

public interface EvolutionarilyOptimizable <T extends ChromosomeElement> {

    List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initializePopulationGetWithFitness(Map<IndividualInitializationType, Integer> populationInitializationPlan,
                                                                                                  ChromosomeElementCostFunction chromosomeElementCostFunction,
                                                                                                  FitnessFunction fitnessFunction,
                                                                                                  FitnessEvaluationStrategy fitnessEvaluationStrategy);

}
