package net.prokhyon.modularfuzzy.optimalization.fitness;

public interface ChromosomeElementCostFunction<T> {

    Double calculateCost(T cost);
}
