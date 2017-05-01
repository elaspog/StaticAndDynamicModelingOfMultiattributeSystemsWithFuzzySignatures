package net.prokhyon.modularfuzzy.optimalization.fitness;

public interface ChromosomeElementCostFunction<COST_TYPE> {

    Double calculateCost(COST_TYPE cost);
}
