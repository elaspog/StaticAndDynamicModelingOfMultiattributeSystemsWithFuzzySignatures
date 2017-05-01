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

    public static <T1, COST_TYPE> Tuple2<T1, COST_TYPE> selectFromMultilistByPositionWithHashMap(int i, Order order, List<Tuple2<COST_TYPE, T1>> searchList) {

        Map<COST_TYPE, List<T1>> mapToSelectFrom = new HashMap<>();
        for (Tuple2<COST_TYPE, T1> pairOfCostAndElement : searchList) {

            COST_TYPE cost = pairOfCostAndElement._1;
            T1 element = pairOfCostAndElement._2;

            if (mapToSelectFrom.keySet().contains(cost)){
                List<T1> listInMap = mapToSelectFrom.get(cost);
                listInMap.add(element);
            } else {
                List<T1> newValueForACostToPutIn = new ArrayList<>();
                newValueForACostToPutIn.add(element);
                mapToSelectFrom.put(cost, newValueForACostToPutIn);
            }
        }
        return selectFromMultilistByPosition(i, order, mapToSelectFrom);
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

    public static <KEY_TYPE, VALUE_TYPE> void addElementToMapWithListContent(Map<KEY_TYPE, List<VALUE_TYPE>> map, KEY_TYPE key, VALUE_TYPE value){

        List<VALUE_TYPE> values = map.get(key);
        if (values == null || values.isEmpty()){

            values = new ArrayList<>();
            values.add(value);
            map.put(key, values);

        } else {

            values.add(value);
        }
    }

    public static <E> List<List<E>> generatePerm(List<E> original) {

        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

}
