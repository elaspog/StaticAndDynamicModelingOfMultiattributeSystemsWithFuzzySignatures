package net.prokhyon.modularfuzzy.optimalization.fitness;

import net.prokhyon.modularfuzzy.optimalization.ChromosomeElement;
import net.prokhyon.modularfuzzy.optimalization.EvolutionarilyOptimizable;
import net.prokhyon.modularfuzzy.optimalization.Individual;

public interface FitnessFunction <COST_TYPE> {

    <T extends ChromosomeElement>
    double calculateFitnessOfChromosomeOfIndividual(Individual<T> individual,
                                                    ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction);

}
