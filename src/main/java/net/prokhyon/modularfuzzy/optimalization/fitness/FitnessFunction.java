package net.prokhyon.modularfuzzy.optimalization.fitness;

import net.prokhyon.modularfuzzy.optimalization.ChromosomeElement;
import net.prokhyon.modularfuzzy.optimalization.Individual;

public interface FitnessFunction {

    <T extends ChromosomeElement>
    double calculateFitnessOfChromosomeOfIndividual(Individual<T> individual,
                                                    ChromosomeElementCostFunction<?> chromosomeElementCostFunction);

}
