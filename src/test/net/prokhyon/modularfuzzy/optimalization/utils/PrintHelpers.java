package net.prokhyon.modularfuzzy.optimalization.utils;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyState;
import net.prokhyon.modularfuzzy.optimalization.Individual;

import java.util.List;

public class PrintHelpers {

    public static void printFitnessValue(List<Double> FitnessValuesAlongTheChromosome) {

        System.out.print("Fitness: " + FitnessValuesAlongTheChromosome.stream().mapToDouble(x->x).sum());
    }

    public static void printChromosomeSequence(Individual<CompoundFuzzyState> individual){

        for (CompoundFuzzyState compoundFuzzyState : individual.getChromosomeSequence()) {
            printCompoundFuzzySystem(compoundFuzzyState);
        }
    }

    public static void printCompoundFuzzySystem(CompoundFuzzyState compoundFuzzyState){

        String names = "";
        String types = "";

        for (FuzzyState fuzzyState : compoundFuzzyState.getFuzzyStateTuple()) {
            names += fuzzyState.getFuzzyStateName();
            types += fuzzyState.getFuzzyStateType().toString().substring(0,1);
        }
        System.out.println(names + " " + types);
    }

    public static void printIndividualAndFitnessPairs(List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList) {

        for (Tuple2<Individual<CompoundFuzzyState>, List<Double>> compoundFuzzyStateIndividual : initialPopulationPairedWithFitnessList) {

            System.out.println("Individual: ");
            System.out.println(compoundFuzzyStateIndividual._1.getChromosomeSequence().size());
            System.out.println(compoundFuzzyStateIndividual._2.size());
            printChromosomeSequence(compoundFuzzyStateIndividual._1);
            printFitnessValue(compoundFuzzyStateIndividual._2);
            System.out.println();
        }
    }

}
