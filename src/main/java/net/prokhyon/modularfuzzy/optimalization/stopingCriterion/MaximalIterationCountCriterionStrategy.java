package net.prokhyon.modularfuzzy.optimalization.stopingCriterion;

import net.prokhyon.modularfuzzy.optimalization.EvolutionaryAlgorithm;

public class MaximalIterationCountCriterionStrategy implements CriterionFunctionStrategy {

    private int maximalIteration;

    public MaximalIterationCountCriterionStrategy(int maximalIteration){

        this.maximalIteration = maximalIteration;
    }

    @Override
    public boolean stopCriterionMeets(EvolutionaryAlgorithm evolutionaryAlgorithm) {

        final int currentIteration = evolutionaryAlgorithm.getCurrentIteration();
        if (currentIteration >= maximalIteration) {
            return true;
        }
        return false;
    }
}
