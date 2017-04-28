package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.common.utils.Tuple3;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessFunction;
import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.CriterionFunctionStrategy;
import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.MaximalIterationCountCriterionStrategy;
import net.prokhyon.modularfuzzy.optimalization.utils.DbmeaHelperUtils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DiscreteBacterialMemeticEvolutionaryAlgorithm <T1 extends EvolutionarilyOptimizable, T2 extends ChromosomeElement, COST_TYPE>
        implements EvolutionaryAlgorithm {


    public static final int DEFAULT_POPULATION_SIZE = 100;
    public static final int DEFAULT_STRATEGY_ITERATION_COUNT = 1000;

    private T1 evolutionarilyOptimalizable;
    private CriterionFunctionStrategy criterionFunctionStrategy;
    private FitnessFunction fitnessFunction;
    private ChromosomeElementCostFunction chromosomeElementCostFunction;
    private FitnessEvaluationStrategy fitnessEvaluationStrategy;
    private Map<Integer,        List<Tuple3<Integer, Individual<T2>, Double>>> iterationResults;
    private Map<Individual<T2>, List<Tuple3<Integer, Individual<T2>, Double>>> individualResults;
    private DBMEA_State dbmeaState = DBMEA_State.UNINITIALIZED;
    private int iteration = 0;

    /// State 1 variables - Initialized population
    private List<Tuple2<Individual<T2>, List<Double>>> initialPopulationPairedWithFitnessList;
    public List<Tuple2<Individual<T2>, List<Double>>> getInitialPopulationPairedWithFitnessList() {
        return initialPopulationPairedWithFitnessList;
    }

    public DiscreteBacterialMemeticEvolutionaryAlgorithm(T1 evolutionarilyOptimalizable,
                                                         FitnessFunction fitnessFunction,
                                                         ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                         FitnessEvaluationStrategy fitnessEvaluationStrategy){

        this(evolutionarilyOptimalizable, fitnessFunction, chromosomeElementCostFunction, new MaximalIterationCountCriterionStrategy(DEFAULT_STRATEGY_ITERATION_COUNT), fitnessEvaluationStrategy);
    }

    public DiscreteBacterialMemeticEvolutionaryAlgorithm(T1 evolutionarilyOptimalizable,
                                                         FitnessFunction fitnessFunction,
                                                         ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                         CriterionFunctionStrategy criterionFunctionStrategy,
                                                         FitnessEvaluationStrategy fitnessEvaluationStrategy){

        //if (evolutionarilyOptimalizable == null)
        //    throw new IllegalArgumentException("Unknown parameter: " + evolutionarilyOptimalizable);
        //if (fitnessFunction == null)
        //    throw new IllegalArgumentException("Unknown parameter: " + fitnessFunction);
        //if (criterionFunctionStrategy == null)
        //    throw new IllegalArgumentException("Unknown parameter: " + criterionFunctionStrategy);

        this.evolutionarilyOptimalizable = evolutionarilyOptimalizable;
        this.fitnessFunction = fitnessFunction;
        this.criterionFunctionStrategy = criterionFunctionStrategy;
        this.chromosomeElementCostFunction = chromosomeElementCostFunction;
        this.fitnessEvaluationStrategy = fitnessEvaluationStrategy;
        iterationResults = new HashMap<>();
        individualResults = new HashMap<>();
    }


    public void runOptimizationProcess(){

        if (criterionFunctionStrategy == null)
            throw new RuntimeException("No criterion function for automatic optimization.");

        if (dbmeaState.equals(DBMEA_State.UNINITIALIZED))
            throw new RuntimeException("No population was generated for automatic optimization.");

        while (true) {

            //bacterialMutation();
            //geneTransfer();

            iteration = iteration + 1;
            if (criterionFunctionStrategy.stopCriterionMeets(this) == true){
                break;
            }
        }

    }

    private void bacterialMutation(MutationType mutationType, Integer I_segment, Integer N_clones) {

        Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
        List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
        if (! possibleSegmentSizes.contains(I_segment))
            throw new RuntimeException("Segment size is not the divisor of count of possible chromosome elements." +
                    " Count of possible chromosomes = " + countOfPossibleChromosomeElements +
                    ", possible divisors = <" + possibleSegmentSizes.stream().map(x -> x.toString()).collect(Collectors.joining(", ")) + ">" +
                    ", I_segment parameter = " + I_segment);

        Integer possiblePermutationCountForSegment = DbmeaHelperUtils.getPossiblePermutationCountForNumber(I_segment);
        if (N_clones > possiblePermutationCountForSegment)
            throw new RuntimeException("The number of possible clones for a chromosome is less than the possible maximum." +
                    " possible maximum = " + possiblePermutationCountForSegment +
                    ", N_clones parameter = " + N_clones);

        switch(mutationType){

            case COHERENT:
                break;

            case LOOSE:
                break;

            default:
                break;
        }
    }

    public void generateInitialPopulationSubProcess(){

        Map<IndividualInitializationType, Integer> populationInitializationPlan = new HashMap<>();
        populationInitializationPlan.put(IndividualInitializationType.NEAREST_NEIGHBOUR, 1);
        populationInitializationPlan.put(IndividualInitializationType.SECONDARY_NEAREST_NEIGHBOUR, 1);
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_NN_START, 1);
        populationInitializationPlan.put(IndividualInitializationType.ALTERNATING_NEAREST_NEIGHBOUR_SNN_START, 1);
        populationInitializationPlan.put(IndividualInitializationType.RANDOM, DEFAULT_POPULATION_SIZE - 4);

        generateInitialPopulationSubProcess(populationInitializationPlan);
    }

    public void generateInitialPopulationSubProcess(Map<IndividualInitializationType, Integer> populationInitializationPlan,
                                                    Object ... domainDependentConfiguration){

        if (this.evolutionarilyOptimalizable == null)
            throw new IllegalArgumentException("Unknown parameter: " + evolutionarilyOptimalizable);

        if (this.criterionFunctionStrategy == null)
            throw new IllegalArgumentException("Unknown parameter: " + criterionFunctionStrategy);

        initialPopulationPairedWithFitnessList = evolutionarilyOptimalizable
                .initializePopulationGetWithFitness(populationInitializationPlan, chromosomeElementCostFunction, fitnessFunction, fitnessEvaluationStrategy, domainDependentConfiguration);
        dbmeaState = DBMEA_State.INITIALIZED;
    }

    @Override
    public int geCurrentIteration() {
        return iteration;
    }

    @Override
    public List<Double> getFitnessResultsByIteration() {
        throw new NotImplementedException();
    }

    @Override
    public CriterionFunctionStrategy getCriterionFunctionStrategy() {
        return criterionFunctionStrategy;
    }

    @Override
    public List<Integer> getPossibleSegmentSizes() {

        if (this.evolutionarilyOptimalizable == null)
            throw new IllegalArgumentException("The evolutionarily optimizable domain is missing.");

        Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
        return DbmeaHelperUtils.getDivisorsForNaturalNumber(countOfPossibleChromosomeElements);
    }

}
