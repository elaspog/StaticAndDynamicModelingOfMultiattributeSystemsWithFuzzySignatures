package net.prokhyon.modularfuzzy.optimalization.utils;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.CompoundFuzzyState;
import net.prokhyon.modularfuzzy.optimalization.Individual;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PrintHelpers {

    public static void printFitnessSum(List<Double> FitnessValuesAlongTheChromosome) {

        System.out.print("Fitness: " + FitnessValuesAlongTheChromosome.stream()
                .filter(Objects::nonNull)
                .mapToDouble(x->x)
                .sum());
    }

    public static void printChromosomeSequence(Individual<CompoundFuzzyState> individual){

        for (CompoundFuzzyState compoundFuzzyState : individual.getChromosomeSequence()) {
            printCompoundFuzzySystem(compoundFuzzyState);
        }
    }

    public static void printCompoundFuzzySystem(CompoundFuzzyState compoundFuzzyState){

        String names = "";
        String types = "";

        if (compoundFuzzyState != null) {
            for (FuzzyState fuzzyState : compoundFuzzyState.getFuzzyStateTuple()) {
                names += fuzzyState.getFuzzyStateName();
                types += fuzzyState.getFuzzyStateType().toString().substring(0, 1);
            }
        } else {
            names += "NULL";
            types += "NULL";
        }
        System.out.println(names + " " + types);
    }

    public static void printIndividualAndFitnessPairs(List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList) {

        for (Tuple2<Individual<CompoundFuzzyState>, List<Double>> compoundFuzzyStateIndividual : initialPopulationPairedWithFitnessList) {

            System.out.println("Individual: ");
            System.out.println("state cnt: " + compoundFuzzyStateIndividual._1.getChromosomeSequence().size());
            System.out.println("cost cnt : " + compoundFuzzyStateIndividual._2.stream().filter(Objects::nonNull).collect(Collectors.toList()).size());
            printChromosomeSequence(compoundFuzzyStateIndividual._1);
            printFitnessSum(compoundFuzzyStateIndividual._2);
            System.out.println();
        }
    }


    public static void printIndividualAndFitnessSteps(List<Tuple2<Individual<CompoundFuzzyState>, List<Double>>> initialPopulationPairedWithFitnessList) {

        for (Tuple2<Individual<CompoundFuzzyState>, List<Double>> x : initialPopulationPairedWithFitnessList) {

            System.out.println("States:");
            for (CompoundFuzzyState compoundFuzzyState : x._1.getChromosomeSequence()) {
                System.out.println((compoundFuzzyState == null ? "-" :compoundFuzzyState.getAggregatedStateName()));
            }
            System.out.println();
            System.out.println("Costs:");
            for (Double aDouble : x._2) {
                System.out.println(aDouble);
            }
        }
        System.out.println();

        for (Tuple2<Individual<CompoundFuzzyState>, List<Double>> compoundFuzzyStateIndividual : initialPopulationPairedWithFitnessList) {

            System.out.println("Individual: ");
            System.out.println("\tstate cnt: " + compoundFuzzyStateIndividual._1.getChromosomeSequence().size());
            System.out.println("\tcost cnt : " + compoundFuzzyStateIndividual._2.stream().filter(Objects::nonNull).collect(Collectors.toList()).size());
            printChromosomeWithFitness(compoundFuzzyStateIndividual);
            System.out.println();
            printFitnessSum(compoundFuzzyStateIndividual._2);
            System.out.println();
        }
    }

    private static void printChromosomeWithFitness(Tuple2<Individual<CompoundFuzzyState>, List<Double>> compoundFuzzyStateIndividual) {

        List<CompoundFuzzyState> chromosomeSequence = compoundFuzzyStateIndividual._1.getChromosomeSequence();
        List<Double> doubles = compoundFuzzyStateIndividual._2;

        String output = "";

        for (int i = 0; i < chromosomeSequence.size(); i++){

            String names = "";
            String types = "";
            String cost = "";

            if (chromosomeSequence.get(i) != null) {
                for (FuzzyState fuzzyState : chromosomeSequence.get(i).getFuzzyStateTuple()) {
                    names += fuzzyState.getFuzzyStateName();
                    types += fuzzyState.getFuzzyStateType().toString().substring(0, 1);
                }
                cost += "\n\t" + doubles.get(i);
            } else {
                names += "NULL";
                types += "NULL";
            }
            output += "\n" + names + " " + types + " " + cost;
        }
        System.out.println(output);
    }

}
