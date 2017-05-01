package net.prokhyon.modularfuzzy.optimalization.fitness;

import net.prokhyon.modularfuzzy.optimalization.ChromosomeElement;
import net.prokhyon.modularfuzzy.optimalization.EvolutionarilyOptimizable;
import net.prokhyon.modularfuzzy.optimalization.Individual;

public interface FitnessFunction <COST_TYPE, CHROMOSOME_TYPE extends ChromosomeElement> {

    double calculateFitnessOfChromosomeOfIndividual(Individual<CHROMOSOME_TYPE> individual,
                                                    ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                    EvolutionarilyOptimizable<CHROMOSOME_TYPE, COST_TYPE> evolutionarilyOptimizable);

}
