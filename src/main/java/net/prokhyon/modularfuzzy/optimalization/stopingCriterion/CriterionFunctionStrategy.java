package net.prokhyon.modularfuzzy.optimalization.stopingCriterion;

import net.prokhyon.modularfuzzy.optimalization.EvolutionaryAlgorithm;

public interface CriterionFunctionStrategy {

    boolean stopCriterionMeets(EvolutionaryAlgorithm evolutionaryAlgorithm);
}
