package net.prokhyon.modularfuzzy.optimalization.stopingCriterion;

import net.prokhyon.modularfuzzy.optimalization.EvolutionaryAlgorithm;

import java.util.List;

public class MinimalErrorGrowthInTimeWindowCriterionStrategy implements CriterionFunctionStrategy {

    private double minimalError;
    private int iterationWindowSize;

    public MinimalErrorGrowthInTimeWindowCriterionStrategy(double minimalError, int iterationWindowSize){

        this.minimalError = minimalError;
        this.iterationWindowSize = iterationWindowSize;
    }

    @Override
    public boolean stopCriterionMeets(EvolutionaryAlgorithm evolutionaryAlgorithm) {

        int iteration = evolutionaryAlgorithm.getCurrentIteration();
        final List<Double> fitnessResultsByIteration = evolutionaryAlgorithm.getFitnessResultsByIteration(iteration);
        final int size = fitnessResultsByIteration.size();
        int fromIndex = Math.max(size - iterationWindowSize, 0);

        final List<Double> segmentOfFitnessValues = fitnessResultsByIteration.subList(fromIndex, size);

        final double maxValue = segmentOfFitnessValues.stream().mapToDouble(x -> x).max().getAsDouble();
        final double minValue = segmentOfFitnessValues.stream().mapToDouble(x -> x).min().getAsDouble();

        if (maxValue - minValue >= minimalError){
            return false;
        }
        return true;
    }
}
