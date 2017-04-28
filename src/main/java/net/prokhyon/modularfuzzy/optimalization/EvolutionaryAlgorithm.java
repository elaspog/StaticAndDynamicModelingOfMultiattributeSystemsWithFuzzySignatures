package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.CriterionFunctionStrategy;

import java.util.List;

public interface EvolutionaryAlgorithm {

    int geCurrentIteration();

    List<Double> getFitnessResultsByIteration();

    CriterionFunctionStrategy getCriterionFunctionStrategy();

    List<Integer> getPossibleSegmentSizes();
}
