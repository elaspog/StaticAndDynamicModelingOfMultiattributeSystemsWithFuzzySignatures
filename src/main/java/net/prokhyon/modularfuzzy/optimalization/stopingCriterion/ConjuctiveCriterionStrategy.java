package net.prokhyon.modularfuzzy.optimalization.stopingCriterion;

import net.prokhyon.modularfuzzy.optimalization.EvolutionaryAlgorithm;

import java.util.ArrayList;
import java.util.List;

public class ConjuctiveCriterionStrategy implements CriterionFunctionStrategy {

    private List<CriterionFunctionStrategy> criterionFunctionStrategies;

    public ConjuctiveCriterionStrategy(CriterionFunctionStrategy... cfs){

        criterionFunctionStrategies = new ArrayList<>();
        for (CriterionFunctionStrategy cf : cfs) {
            criterionFunctionStrategies.add(cf);
        }
    }

    @Override
    public boolean stopCriterionMeets(EvolutionaryAlgorithm evolutionaryAlgorithm) {

        return criterionFunctionStrategies.stream().allMatch(x -> x.stopCriterionMeets(evolutionaryAlgorithm) == true);
    }
}
