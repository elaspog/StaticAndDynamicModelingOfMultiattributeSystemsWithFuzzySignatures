package net.prokhyon.modularfuzzy.optimalization.utils;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.optimalization.ChromosomeElement;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;

import java.util.*;

public class DbmeaHelperUtils {

    public static <T> T selectRandomElementFromList(List<T> list){

        if(list == null) {
            throw new IllegalArgumentException("List parameter is not initialized.");
        }

        Random randomizer = new Random();
        final int randomInt = randomizer.nextInt(list.size());
        final T randomlySelectedItem = list.get(randomInt);
        return randomlySelectedItem;
    }

    public static <T1 extends ChromosomeElement, T2> Tuple2<T1, Double> selectByEvaluatedFitnessPosition(final ChromosomeElementCostFunction<T2> costEvaluator,
                                                                                         final List<Tuple2<T1, T2>> candidates,
                                                                                         final int i,
                                                                                         final Order order) {

        final int size = candidates.size();
        if (i < 0 || i > size)
            throw new IllegalArgumentException("Value of i is does not fit to given data: " + i + " of " + size + ".");

        Map<Double, List<T1>> resultMap = new TreeMap<>();

        for (Tuple2<T1, T2> entry : candidates) {

            final T1 returnCandidate = entry._1;
            final T2 costData = entry._2;

            final Double keyFitness = costEvaluator.calculateCost(costData);

            List<T1> possibleConflictCheckerList = resultMap.get(keyFitness);
            if (possibleConflictCheckerList == null) {
                final ArrayList<T1> newCandidateList = new ArrayList<>();
                newCandidateList.add(returnCandidate);
                resultMap.put(keyFitness, newCandidateList);
            } else {
                possibleConflictCheckerList.add(returnCandidate);
            }
        }

        return selectFromMultilistByPosition(i, order, resultMap);
    }

    public static <T1, COST_TYPE> Tuple2<T1, COST_TYPE> selectFromMultilistByPosition(int i, Order order, Map<COST_TYPE, List<T1>> resultMap) {

        final int size = resultMap.size();
        if (i < 0 || i > size)
            throw new IllegalArgumentException("Value of i is does not fit to given data: " + i + " of " + size + ".");

        COST_TYPE resultFitness = null;
        List<T1> resultElementChoseList = null;

        if (order.equals(Order.ASCENDING)){
            /// Selection for ascending order
            int accum = 0;
            for (Map.Entry<COST_TYPE, List<T1>> resultPair : resultMap.entrySet()) {
                accum = accum + resultPair.getValue().size();
                resultElementChoseList = resultPair.getValue();
                resultFitness = resultPair.getKey();
                if (i < accum) {
                    resultElementChoseList = resultPair.getValue();
                    resultFitness = resultPair.getKey();
                    break;
                }
            }
        } else if (order.equals(Order.DESCENDING)){
            /// Selection for descending order
            int accum = resultMap.size() - 1;
            for (Map.Entry<COST_TYPE, List<T1>> resultPair : resultMap.entrySet()) {
                accum = accum - resultPair.getValue().size();
                resultElementChoseList = resultPair.getValue();
                resultFitness = resultPair.getKey();
                if (i > accum) {
                    resultElementChoseList = resultPair.getValue();
                    resultFitness = resultPair.getKey();
                    break;
                }
            }
        }

        return new Tuple2<>(selectRandomElementFromList(resultElementChoseList), resultFitness);
    }

    public static List<Integer> getDivisorsForNaturalNumber(Integer num){

        List<Integer> possibleDivisors = new ArrayList<>();
        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                possibleDivisors.add(i);
            }
        }
        return possibleDivisors;
    }

    public static Integer getPossiblePermutationCountForNumber(Integer num){

        if (num < 1)
            throw new RuntimeException("Error in determining the count of permutations for zero or less number: " + num);

        int retVal = 1;
        if (num == 1) {
            return retVal;
        } else {
            for (int i = num; i > 1; i--){
                retVal = retVal * i;
            }
        }
        return retVal;
    }

}
