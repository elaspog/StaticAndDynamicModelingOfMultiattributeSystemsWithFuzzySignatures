package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.common.utils.Tuple2;
import net.prokhyon.modularfuzzy.common.utils.Tuple3;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessEvaluationStrategy;
import net.prokhyon.modularfuzzy.optimalization.fitness.FitnessFunction;
import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.CriterionFunctionStrategy;
import net.prokhyon.modularfuzzy.optimalization.stopingCriterion.MaximalIterationCountCriterionStrategy;
import net.prokhyon.modularfuzzy.optimalization.utils.DbmeaHelperUtils;
import net.prokhyon.modularfuzzy.optimalization.utils.Order;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DiscreteBacterialMemeticEvolutionaryAlgorithm <EVOLUTIONARILY_OPTIMIZABLE_TYPE extends EvolutionarilyOptimizable<CHROMOSOME_TYPE, COST_TYPE>,
                                                            CHROMOSOME_TYPE extends ChromosomeElement,
                                                            COST_TYPE>
        implements EvolutionaryAlgorithm<EVOLUTIONARILY_OPTIMIZABLE_TYPE, CHROMOSOME_TYPE, COST_TYPE> {


    public static final int DEFAULT_POPULATION_SIZE = 100;
    public static final int DEFAULT_STRATEGY_ITERATION_COUNT = 1000;

    private EVOLUTIONARILY_OPTIMIZABLE_TYPE evolutionarilyOptimalizable;
    private CriterionFunctionStrategy criterionFunctionStrategy;
    private FitnessFunction<COST_TYPE, CHROMOSOME_TYPE> fitnessFunction;
    private ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction;
    private FitnessEvaluationStrategy fitnessEvaluationStrategy;
    private DBMEA_State dbmeaState = DBMEA_State.UNINITIALIZED;
    private int iteration = 0;

    // will be passed to methods of EvolutionarilyOptimizable interface
    private final Object[] domainSpecificConfiguration;

    /// <iteration, individual, fitness>
    private Map<Integer,        List<Tuple3<Integer, Individual<CHROMOSOME_TYPE>, Double>>> iterationResults;
    private Map<Individual<CHROMOSOME_TYPE>, List<Tuple3<Integer, Individual<CHROMOSOME_TYPE>, Double>>> individualResults;

    /// Initialized population
    private List<Individual<CHROMOSOME_TYPE>> initialPopulation;
    public List<Individual<CHROMOSOME_TYPE>> getInitialPopulation() {
        return initialPopulation;
    }

    public DiscreteBacterialMemeticEvolutionaryAlgorithm(EVOLUTIONARILY_OPTIMIZABLE_TYPE evolutionarilyOptimalizable,
                                                         FitnessFunction<COST_TYPE, CHROMOSOME_TYPE> fitnessFunction,
                                                         ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                         FitnessEvaluationStrategy fitnessEvaluationStrategy,
                                                         Object ... domainSpecificConfiguration){

        this(evolutionarilyOptimalizable, fitnessFunction, chromosomeElementCostFunction, new MaximalIterationCountCriterionStrategy(DEFAULT_STRATEGY_ITERATION_COUNT), fitnessEvaluationStrategy, domainSpecificConfiguration);
    }

    public DiscreteBacterialMemeticEvolutionaryAlgorithm(EVOLUTIONARILY_OPTIMIZABLE_TYPE evolutionarilyOptimalizable,
                                                         FitnessFunction<COST_TYPE, CHROMOSOME_TYPE> fitnessFunction,
                                                         ChromosomeElementCostFunction<COST_TYPE> chromosomeElementCostFunction,
                                                         CriterionFunctionStrategy criterionFunctionStrategy,
                                                         FitnessEvaluationStrategy fitnessEvaluationStrategy,
                                                         Object ... domainSpecificConfiguration){

        this.evolutionarilyOptimalizable = evolutionarilyOptimalizable;
        this.fitnessFunction = fitnessFunction;
        this.criterionFunctionStrategy = criterionFunctionStrategy;
        this.chromosomeElementCostFunction = chromosomeElementCostFunction;
        this.fitnessEvaluationStrategy = fitnessEvaluationStrategy;
        this.domainSpecificConfiguration = domainSpecificConfiguration;
        iterationResults = new HashMap<>();
        individualResults = new HashMap<>();
    }

    public void runOptimizationProcess(){

        if (criterionFunctionStrategy == null)
            throw new RuntimeException("No criterion function for automatic optimization.");

        if (dbmeaState.equals(DBMEA_State.UNINITIALIZED))
            throw new RuntimeException("No population was generated for automatic optimization.");

        evaluatePopulationAndStoreResults(initialPopulation);


        while (true) {

            stepIteration();

            //coherentSegmentMutationOnPopulation();
            //looseSegmentMutationOnPopulation();
            //randomAttributeGroupMutationOnPopulation();
            //geneTransferOnPopulation();
            //localSearch();

            Set<Individual<CHROMOSOME_TYPE>> individuals = individualResults.keySet();
            evaluatePopulationAndStoreResults(new ArrayList<>(individuals));

            if (criterionFunctionStrategy.stopCriterionMeets(this) == true){
                break;
            }
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

    public void generateInitialPopulationSubProcess(Map<IndividualInitializationType, Integer> populationInitializationPlan){

        if (this.evolutionarilyOptimalizable == null)
            throw new RuntimeException("Domain area is not initialized.");

        if (this.chromosomeElementCostFunction == null)
            /// this is necessary only if populationInitializationPlan parameter contains not RANDOM strategies
            /// TODO check the content of populationInitializationPlan parameter
            throw new RuntimeException("Cost function for evaluating chromosome elements was not initialized.");

        initialPopulation = evolutionarilyOptimalizable.initializePopulation(populationInitializationPlan, chromosomeElementCostFunction,
                fitnessFunction, fitnessEvaluationStrategy, domainSpecificConfiguration);

        dbmeaState = DBMEA_State.INITIALIZED;
    }

    public List<Individual<CHROMOSOME_TYPE>> randomAttributeGroupMutationOnPopulation(Integer i_attrib_cnt, Integer n_clones){

        List<Individual<CHROMOSOME_TYPE>> population = randomAttributeGroupMutationOnPopulation(selectLastPopulation(), i_attrib_cnt, n_clones);
        dbmeaState = DBMEA_State.BACTERIAL_MUTATION_FINISHED;
        return population;
    }

    public List<Individual<CHROMOSOME_TYPE>> coherentSegmentMutationOnPopulation(Integer i_segment, Integer n_clones){

        List<Individual<CHROMOSOME_TYPE>> population = coherentSegmentMutationOnPopulation(selectLastPopulation(), i_segment, n_clones);
        dbmeaState = DBMEA_State.BACTERIAL_MUTATION_FINISHED;
        return population;
    }

    public List<Individual<CHROMOSOME_TYPE>> looseSegmentMutationOnPopulation(Integer N_clones){

        List<Individual<CHROMOSOME_TYPE>> population = looseSegmentMutationOnPopulation(selectLastPopulation(), N_clones);
        dbmeaState = DBMEA_State.BACTERIAL_MUTATION_FINISHED;
        return population;
    }

    public List<Individual<CHROMOSOME_TYPE>> geneTransferOnPopulation(int geneTransferCount, int segmentSize){

        List<Individual<CHROMOSOME_TYPE>> population = geneTransferOnPopulation(selectLastPopulation(), geneTransferCount, segmentSize);
        dbmeaState = DBMEA_State.GENE_TRANSFER_FINISHED;
        return population;
    }

    public List<Individual<CHROMOSOME_TYPE>> randomAttributeGroupMutationOnPopulation(List<Individual<CHROMOSOME_TYPE>> population, Integer i_attrib_cnt, Integer n_clones){

        return population.stream()
                .map(individual -> randomAttributeGroupMutationOnIndividual(individual, i_attrib_cnt, n_clones))
                .collect(Collectors.toList());
    }

    public List<Individual<CHROMOSOME_TYPE>> coherentSegmentMutationOnPopulation(List<Individual<CHROMOSOME_TYPE>> population, Integer i_segment, Integer n_clones){

        return population.stream()
                .map(individual -> coherentSegmentMutationOnIndividual(individual, i_segment, n_clones))
                .collect(Collectors.toList());
    }

    public List<Individual<CHROMOSOME_TYPE>> looseSegmentMutationOnPopulation(List<Individual<CHROMOSOME_TYPE>> population, Integer N_clones){

        return population.stream()
                .map(individual -> looseSegmentMutationOnIndividual(individual, N_clones))
                .collect(Collectors.toList());
    }

    public List<Individual<CHROMOSOME_TYPE>> geneTransferOnPopulation(List<Individual<CHROMOSOME_TYPE>> population, int geneTransferCount, int segmentSize){

        Random randomizer = new Random();
        List<Individual<CHROMOSOME_TYPE>> actualPopulation = new ArrayList<>(population);

        int individual_cnt = actualPopulation.size();
        int midPoint = individual_cnt / 2;

        for (int i = 0; i < geneTransferCount; i++) {

            List<Individual<CHROMOSOME_TYPE>> sortedIndividuals = sortIndividualsByFitness(actualPopulation);

            List<Individual<CHROMOSOME_TYPE>> goodIndividuals = sortedIndividuals.subList(0, midPoint);
            List<Individual<CHROMOSOME_TYPE>> badIndividuals = sortedIndividuals.subList(midPoint, individual_cnt);

            int indexOfGoodIndividual = randomizer.nextInt(midPoint);
            int indexOfBadIndividual = randomizer.nextInt(individual_cnt - midPoint);

            Individual<CHROMOSOME_TYPE> goodIndividual = goodIndividuals.get(indexOfGoodIndividual);
            Individual<CHROMOSOME_TYPE> badIndividual = badIndividuals.get(indexOfBadIndividual);

            int chromosomeLength = goodIndividual.getChromosomeSequence().size();
            int offsetInSource = randomizer.nextInt(chromosomeLength - segmentSize + 1);
            int offsetInTarget = randomizer.nextInt(chromosomeLength + 1);

            Individual<CHROMOSOME_TYPE> correctedIndividual = geneTransferOnIndividual(goodIndividual, badIndividual, segmentSize, offsetInSource, offsetInTarget);

            int originalIndex = actualPopulation.indexOf(badIndividual);
            actualPopulation.remove(badIndividual);
            actualPopulation.add(originalIndex, correctedIndividual);
        }

        return actualPopulation;
    }

    List<Individual<CHROMOSOME_TYPE>> sortIndividualsByFitness(List<Individual<CHROMOSOME_TYPE>> population) {

        List<Individual<CHROMOSOME_TYPE>> individuals = new ArrayList<>(population);
        individuals.sort((o1, o2) -> {
            Double o1_fitness = fitnessFunction.calculateFitnessOfChromosomeOfIndividual(o1, chromosomeElementCostFunction, evolutionarilyOptimalizable);
            Double o2_fitness = fitnessFunction.calculateFitnessOfChromosomeOfIndividual(o2, chromosomeElementCostFunction, evolutionarilyOptimalizable);
            return o1_fitness.compareTo(o2_fitness);
        });
        return individuals;
    }

    public Individual<CHROMOSOME_TYPE> geneTransferOnIndividual(Individual<CHROMOSOME_TYPE> source,
                                                                Individual<CHROMOSOME_TYPE> target,
                                                                int segmentSize, int offsetInSource, int offsetInTarget){

        if (segmentSize < 0 || offsetInSource < 0 || offsetInTarget < 0)
            throw new RuntimeException("Error in parameter configuration for gene transfer: segmentSize=" + segmentSize +
                    ", offsetInSource=" + offsetInSource + ", offsetInTarget=" + offsetInTarget);

        List<CHROMOSOME_TYPE> originalSegment = source.getChromosomeSequence().subList(offsetInSource, offsetInSource + segmentSize);
        ArrayList<CHROMOSOME_TYPE> segment = new ArrayList<>(originalSegment);

        List<CHROMOSOME_TYPE> targetSequencePart1 = target.getChromosomeSequence().subList(0, offsetInTarget);
        List<CHROMOSOME_TYPE> targetSequencePart2 = target.getChromosomeSequence().subList(offsetInTarget, target.getChromosomeSequence().size());

        List<CHROMOSOME_TYPE> targetSequencePart1Filtered = targetSequencePart1.stream()
                .filter(segment::contains)
                .collect(Collectors.toList());

        List<CHROMOSOME_TYPE> targetSequencePart2Filtered = targetSequencePart2.stream()
                .filter(segment::contains)
                .collect(Collectors.toList());

        ArrayList<CHROMOSOME_TYPE> targetSequence = new ArrayList<>();
        targetSequence.addAll(targetSequencePart1Filtered);
        targetSequence.addAll(segment);
        targetSequence.addAll(targetSequencePart2Filtered);

        return new Individual<>(targetSequence);
    }

    public Individual<CHROMOSOME_TYPE> randomAttributeGroupMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, int i_column_cnt, int n_clones) {

        /// checking constraints
        Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
        List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
        checkConstraintsForSegmentAndCloneSizes(i_column_cnt, n_clones, countOfPossibleChromosomeElements, possibleSegmentSizes);

        /// getting chromosome length
        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();
        int originalChromosomeLength = originalChromosomeSequence.size();

        /// generating shuffled list of chromosome column index combinations to process
        List<Integer> randomIndexPermutation = IntStream.range(0, originalChromosomeLength)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(randomIndexPermutation);

        return randomAttributeGroupMutationOnIndividual(individual, i_column_cnt, n_clones, randomIndexPermutation, true);
    }

    public Individual<CHROMOSOME_TYPE> randomAttributeGroupMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual,
                                                                                int i_column_cnt, int n_clones,
                                                                                List<Integer> indexPermutation){

        return randomAttributeGroupMutationOnIndividual(individual, i_column_cnt, n_clones, indexPermutation, false);
    }

    Individual<CHROMOSOME_TYPE> randomAttributeGroupMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual,
                                                                         int i_column_cnt, int n_clones,
                                                                         List<Integer> indexPermutation,
                                                                         boolean wereConstraintsChecked) {

        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();
        int originalChromosomeLength = originalChromosomeSequence.size();

        if (wereConstraintsChecked == false) {

            /// checking constraints
            Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
            List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
            checkConstraintsForSegmentAndCloneSizes(i_column_cnt, n_clones, countOfPossibleChromosomeElements, possibleSegmentSizes);
            checkConstraintsForPermutation(originalChromosomeLength, indexPermutation);
        }

        /// segmenting index permutation list and generating random order of segments
        List<List<Integer>> randomAttributeOrder = new ArrayList<>();
        for (int startIndex = 0; startIndex <= originalChromosomeLength - i_column_cnt; startIndex = startIndex + i_column_cnt) {
            int endIndex = startIndex + i_column_cnt;
            randomAttributeOrder.add(indexPermutation.subList(startIndex, endIndex));
        }
        //Collections.shuffle(randomAttributeOrder);

        return randomAttributeGroupMutationOnIndividual(individual, n_clones, randomAttributeOrder, true);
    }

    public Individual<CHROMOSOME_TYPE> randomAttributeGroupMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, int n_clones,
                                                                                List<List<Integer>> randomAttributeOrder){

        return randomAttributeGroupMutationOnIndividual(individual, n_clones, randomAttributeOrder, false);
    }

    Individual<CHROMOSOME_TYPE> randomAttributeGroupMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, int n_clones,
                                                                         List<List<Integer>> randomAttributeOrder,
                                                                         boolean wereConstraintsChecked) {

        if (wereConstraintsChecked == false) {

            /// checking constraints
            Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
            List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
            for (List<Integer> indexPermutation : randomAttributeOrder) {

                checkConstraintsForSegmentAndCloneSizes(indexPermutation.size(), n_clones, countOfPossibleChromosomeElements, possibleSegmentSizes);
            }
        }

        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();

        /// +2 is for original individual and clone of the reversed segment
        int individualInstanceCountToCheck = n_clones + 2;

        /// reference to the actual best individual after actual segment optimization
        Individual<CHROMOSOME_TYPE> actualBest = new Individual<>(new ArrayList<>(originalChromosomeSequence));

        for (List<Integer> indexCombination : randomAttributeOrder) {

            /// container of individuals to be generated, including original, reversed segment and permuted clones
            List<Individual<CHROMOSOME_TYPE>> candidates = new ArrayList<>();

            /// copy of original is added to the candidates
            Individual<CHROMOSOME_TYPE> copyOfOriginal = new Individual<>(actualBest.getChromosomeSequence());
                    // = modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements(actualBest, indexCombination, valuesOfOriginalSequenceInPositionsOfIndexCombination);
            candidates.add(copyOfOriginal);

            /// copy of reversed is added to the candidates if not equals to the original
            List<CHROMOSOME_TYPE> valuesOfOriginalSequenceInPositionsOfIndexCombination = new ArrayList<>(indexCombination.stream()
                    .map(actualBest.getChromosomeSequence()::get)
                    .collect(Collectors.toList()));
            List<CHROMOSOME_TYPE> valuesOfReversedOriginalSequenceInPositionsOfIndexCombinations = new ArrayList<>(valuesOfOriginalSequenceInPositionsOfIndexCombination);
            Collections.reverse(valuesOfReversedOriginalSequenceInPositionsOfIndexCombinations);
            ArrayList<Integer> reversedIndexCombination = new ArrayList<>(indexCombination);
            Collections.reverse(reversedIndexCombination);
            Individual<CHROMOSOME_TYPE> reversed
                    = modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements(actualBest, indexCombination, valuesOfReversedOriginalSequenceInPositionsOfIndexCombinations);
            if (! candidates.contains(reversed)) {
                candidates.add(reversed);
            }

            /// generate and shuffle all permutations for the selected segment
            List<List<CHROMOSOME_TYPE>> shuffledPermutedSegments = DbmeaHelperUtils.generatePerm(valuesOfOriginalSequenceInPositionsOfIndexCombination);
            Collections.shuffle(shuffledPermutedSegments);

            for (List<CHROMOSOME_TYPE> shuffledPermutedSegment : shuffledPermutedSegments) {

                Individual<CHROMOSOME_TYPE> clonedAndPermutedIndividual = modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements(actualBest, indexCombination, shuffledPermutedSegment);

                if (! candidates.contains(clonedAndPermutedIndividual))
                    candidates.add(clonedAndPermutedIndividual);

                if (candidates.size() >= individualInstanceCountToCheck || candidates.size() >= shuffledPermutedSegments.size())
                    break;
            }

            /// evaluate all candidates
            actualBest = getWinnerFromActualBestAndCandidates(actualBest, candidates);
        }
        return actualBest;
    }

    Individual<CHROMOSOME_TYPE> modifyChromosomeOfIndividualInIndexPositionsWithOtherChromosomeElements(Individual<CHROMOSOME_TYPE> individual,
                                                                                                        List<Integer> indices,
                                                                                                        List<CHROMOSOME_TYPE> values) {

        int indices_size = indices.size();
        int values_size = values.size();
        if (indices_size != values_size)
            throw new RuntimeException("The sizes of indices and values does not match: indices=" + indices_size + " vs. values=" + values_size);

        ArrayList<CHROMOSOME_TYPE> newChromosome = new ArrayList<>(individual.getChromosomeSequence());
        for (int i = 0; i < values_size; i++){
            Integer index = indices.get(i);
            newChromosome.set(index, values.get(i));
        }
        return new Individual<>(newChromosome);
    }

    public Individual<CHROMOSOME_TYPE> coherentSegmentMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, Integer i_segment, Integer n_clones) {

        /// checking constraints
        Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
        List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
        checkConstraintsForSegmentAndCloneSizes(i_segment, n_clones, countOfPossibleChromosomeElements, possibleSegmentSizes);

        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();
        int originalChromosomeLength = originalChromosomeSequence.size();

        /// generating shuffled list of chromosome segment borders to process
        List<Tuple2<Integer, Integer>> segmentProcessingOrder = new ArrayList<>();
        for (int startIndex = 0; startIndex <= originalChromosomeLength - i_segment; startIndex = startIndex + i_segment) {

            int endIndex = startIndex + i_segment;
            segmentProcessingOrder.add(new Tuple2<>(startIndex, endIndex));
        }
        Collections.shuffle(segmentProcessingOrder);

        return coherentSegmentMutationOnIndividual(n_clones, individual, segmentProcessingOrder, true);
    }

    Individual<CHROMOSOME_TYPE> coherentSegmentMutationOnIndividual(Integer n_clones, Individual<CHROMOSOME_TYPE> individual,
                                                                    List<Tuple2<Integer, Integer>> segmentProcessingOrder) {

        return coherentSegmentMutationOnIndividual(n_clones, individual, segmentProcessingOrder, false);
    }

    Individual<CHROMOSOME_TYPE> coherentSegmentMutationOnIndividual(Integer n_clones, Individual<CHROMOSOME_TYPE> individual,
                                                                    List<Tuple2<Integer, Integer>> segmentProcessingOrder,
                                                                    boolean wereConstraintsChecked) {

        if (wereConstraintsChecked == false) {
            /// checking constraints
            Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
            List<Integer> possibleSegmentSizes = getPossibleSegmentSizes();
            for (Tuple2<Integer, Integer> segment : segmentProcessingOrder) {
                int size = segment._2 - segment._1;
                checkConstraintsForSegmentAndCloneSizes(size, n_clones, countOfPossibleChromosomeElements, possibleSegmentSizes);
            }
        }

        /// reference to the actual best individual after actual segment optimization
        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();
        Individual<CHROMOSOME_TYPE> actualBest = new Individual<>(new ArrayList<>(originalChromosomeSequence));

        /// +2 is for original individual and clone of the reversed segment
        int individualInstanceCountToCheck = n_clones + 2;

        /// processing all index pairs, generating clones, evaluating them
        for (Tuple2<Integer, Integer> segmentIndexPair : segmentProcessingOrder) {

            Integer startIndex = segmentIndexPair._1;
            Integer endIndex = segmentIndexPair._2;

            /// container of individuals to be generated, including original, reversed segment and permuted clones
            List<Individual<CHROMOSOME_TYPE>> candidates = new ArrayList<>();

            /// get the selected segment by
            List<CHROMOSOME_TYPE> originalSegment = new ArrayList<>(actualBest.getChromosomeSequence().subList(startIndex, endIndex));
            List<CHROMOSOME_TYPE> reversedSegment = new ArrayList<>(actualBest.getChromosomeSequence().subList(startIndex, endIndex));
            Collections.reverse(reversedSegment);

            /// add the individual with reversed segment to the candidates
            Individual<CHROMOSOME_TYPE> reversedSegmentIndividual = geneSequenceChanger(actualBest, startIndex, endIndex, reversedSegment);
            candidates.add(actualBest);
            if (! reversedSegmentIndividual.equals(actualBest))
                candidates.add(reversedSegmentIndividual);
            else
                throw new RuntimeException("Algorithmic or data error. Reversed segment is already contained in candidates list.");

            /// generate and shuffle all permutations for the selected segment
            List<List<CHROMOSOME_TYPE>> shuffledPermutedSegments = DbmeaHelperUtils.generatePerm(originalSegment);
            Collections.shuffle(shuffledPermutedSegments);

            /// while candidates does not contain enough distinct individuals, add to candidates a cloned individual generated from shuffles
            for (List<CHROMOSOME_TYPE> segment : shuffledPermutedSegments) {

                Individual<CHROMOSOME_TYPE> clonedAndPermutedIndividual = geneSequenceChanger(actualBest, startIndex, endIndex, segment);
                if (! candidates.contains(clonedAndPermutedIndividual))
                    candidates.add(clonedAndPermutedIndividual);

                if (candidates.size() >= individualInstanceCountToCheck || candidates.size() >= shuffledPermutedSegments.size())
                    break;
            }

            /// evaluate all candidates
            actualBest = getWinnerFromActualBestAndCandidates(actualBest, candidates);

        }
        return actualBest;
    }

    Individual<CHROMOSOME_TYPE> getWinnerFromActualBestAndCandidates(Individual<CHROMOSOME_TYPE> actualBest, List<Individual<CHROMOSOME_TYPE>> candidates) {

        /// evaluate all candidates
        List<Tuple2<Individual<CHROMOSOME_TYPE>, Double>> fitnessResults = candidates.stream()
                .map(candidate -> new Tuple2<>(candidate, fitnessFunction.calculateFitnessOfChromosomeOfIndividual(candidate, chromosomeElementCostFunction, evolutionarilyOptimalizable)))
                .collect(Collectors.toList());

        Tuple2<Double, Individual<CHROMOSOME_TYPE>> actualBestFromCandidates = null;

        switch (fitnessEvaluationStrategy){

            case MAXIMIZE_FITNESS:
                actualBestFromCandidates = DbmeaHelperUtils.selectFromMultilistByPositionWithHashMap(0, Order.DESCENDING, fitnessResults);
                break;

            case MINIMIZE_FITNESS:
                actualBestFromCandidates = DbmeaHelperUtils.selectFromMultilistByPositionWithHashMap(0, Order.ASCENDING, fitnessResults);
                break;
        }

        if (actualBestFromCandidates != null){

            double actualBestFitness
                    = fitnessFunction.calculateFitnessOfChromosomeOfIndividual(actualBest, chromosomeElementCostFunction, evolutionarilyOptimalizable);
            double permutationFitness
                    = fitnessFunction.calculateFitnessOfChromosomeOfIndividual(actualBestFromCandidates._2, chromosomeElementCostFunction, evolutionarilyOptimalizable);

            if (actualBestFitness < permutationFitness ){

                actualBest = actualBestFromCandidates._2;
            }
        }
        return actualBest;
    }

    Individual<CHROMOSOME_TYPE> geneSequenceChanger(final Individual<CHROMOSOME_TYPE> individual, int startIndex, int toIndex, List<CHROMOSOME_TYPE> newSegment){

        List<CHROMOSOME_TYPE> chromosomeSequenceToChange = new ArrayList<>(individual.getChromosomeSequence());
        chromosomeSequenceToChange.subList(startIndex, toIndex).clear();
        chromosomeSequenceToChange.addAll(startIndex, newSegment);

        return new Individual<>(chromosomeSequenceToChange);
    }

    public Individual<CHROMOSOME_TYPE> looseSegmentMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, List<List<Integer>> indexOrderListsToProcess) {

        /// reference to the actual best individual after actual segment optimization
        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();

        List<Individual<CHROMOSOME_TYPE>> candidates = new ArrayList<>();
        Individual<CHROMOSOME_TYPE> cloneOfOriginal = new Individual<>(new ArrayList<>(originalChromosomeSequence));
        candidates.add(cloneOfOriginal);

        for (List<Integer> indexOrder : indexOrderListsToProcess) {

            Individual<CHROMOSOME_TYPE> permutedIndividual = permuteChromosomeOfIndividualByIndexOrder(individual, indexOrder);
            if (! candidates.contains(permutedIndividual)){
                candidates.add(permutedIndividual);
            }
        }

        /// evaluate all candidates
        return getWinnerFromActualBestAndCandidates(individual, candidates);
    }

    public Individual<CHROMOSOME_TYPE> looseSegmentMutationOnIndividual(Individual<CHROMOSOME_TYPE> individual, Integer n_clones) {

        /// checking constraints
        Integer countOfPossibleChromosomeElements = evolutionarilyOptimalizable.getCountOfPossibleChromosomeElements();
        checkConstraintsCloneCount(countOfPossibleChromosomeElements, n_clones);

        /// reference to the actual best individual after actual segment optimization
        final List<CHROMOSOME_TYPE> originalChromosomeSequence = individual.getChromosomeSequence();

        List<Individual<CHROMOSOME_TYPE>> candidates = new ArrayList<>();
        Individual<CHROMOSOME_TYPE> cloneOfOriginal = new Individual<>(new ArrayList<>(originalChromosomeSequence));

        /// +1 is for original individual and clone of the reversed segment
        int individualInstanceCountToCheck = n_clones + 1;  // one is reserved for original chromosome

        Random randomizer = new Random();
        while(candidates.size() != individualInstanceCountToCheck){

            /// generating random permutation of indices
            List<Integer> permutationItems = IntStream.range(0, countOfPossibleChromosomeElements)
                    .boxed()
                    .collect(Collectors.toList());
            List<Integer> currentPermutation = new ArrayList<>();
            while(currentPermutation.size() != countOfPossibleChromosomeElements){
                final int randomInt = randomizer.nextInt(permutationItems.size());
                Integer item = permutationItems.get(randomInt);
                currentPermutation.add(item);
                permutationItems.remove(item);
            }

            Individual<CHROMOSOME_TYPE> permutedIndividual = permuteChromosomeOfIndividualByIndexOrder(individual, currentPermutation);
            if (! candidates.contains(permutedIndividual)){
                candidates.add(permutedIndividual);
            }
        }

        return getWinnerFromActualBestAndCandidates(cloneOfOriginal, candidates);
    }

    Individual<CHROMOSOME_TYPE> permuteChromosomeOfIndividualByIndexOrder(Individual<CHROMOSOME_TYPE> individual, List<Integer> currentPermutation) {

        List<CHROMOSOME_TYPE> newChromosome = new ArrayList<>();
        for (Integer index : currentPermutation) {
            if (index >= 0 || index < individual.getChromosomeSequence().size()) {
                newChromosome.add(individual.getChromosomeSequence().get(index));
            } else {
                throw new RuntimeException("Invalid element of the chromosome was refered.");
            }
        }
        return new Individual<>(newChromosome);
    }

    @Override
    public void evaluatePopulationAndStoreResults(List<Individual<CHROMOSOME_TYPE>> individualsToEvaluate) {

        if (this.criterionFunctionStrategy == null)
            throw new RuntimeException("Criterion Function was not initialized.");

        if (this.fitnessFunction == null)
            throw new RuntimeException("Fitness Function was not initialized.");

        for (Individual<CHROMOSOME_TYPE> individualToEvaluate : individualsToEvaluate) {

            double fitness = fitnessFunction.calculateFitnessOfChromosomeOfIndividual(individualToEvaluate, chromosomeElementCostFunction, evolutionarilyOptimalizable);

            Tuple3<Integer, Individual<CHROMOSOME_TYPE>, Double> tuple = new Tuple3<>(iteration, individualToEvaluate, fitness);

            DbmeaHelperUtils.addElementToMapWithListContent(iterationResults, new Integer(iteration), tuple);
            DbmeaHelperUtils.addElementToMapWithListContent(individualResults, individualToEvaluate, tuple);
        }

        dbmeaState = DBMEA_State.EVALUATED;
    }

    @Override
    public List<Individual<CHROMOSOME_TYPE>> selectPopulationByIteration(int generation) {

        return iterationResults.get(generation)
                .stream()
                .map(x -> x._2)
                .collect(Collectors.toList());
    }

    @Override
    public List<Individual<CHROMOSOME_TYPE>> selectLastPopulation() {

        return selectPopulationByIteration(iteration);
    }

    @Override
    public double calculateFitnessValueOfIndividual(Individual<CHROMOSOME_TYPE> individual) {

        return fitnessFunction.calculateFitnessOfChromosomeOfIndividual(individual, chromosomeElementCostFunction, evolutionarilyOptimalizable);
    }

    @Override
    public EVOLUTIONARILY_OPTIMIZABLE_TYPE getEvolutionarilyOptimizable() {

        return evolutionarilyOptimalizable;
    }

    @Override
    public int getCurrentIteration() {

        return iteration;
    }

    @Override
    public void stepIteration() {

        iteration = iteration + 1;
    }

    @Override
    public List<Double> getFitnessResultsByIteration(int iteration) {
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

    private void checkConstraintsForSegmentAndCloneSizes(Integer i_segment, Integer n_clones, Integer countOfPossibleChromosomeElements, List<Integer> possibleSegmentSizes) {

        if (! possibleSegmentSizes.contains(i_segment))
            throw new RuntimeException("Segment size is not the divisor of count of possible chromosome elements." +
                    " Count of possible chromosomes = " + countOfPossibleChromosomeElements +
                    ", possible divisors = {" + possibleSegmentSizes.stream().map(x -> x.toString()).collect(Collectors.joining(",")) + "}" +
                    ", I_segment parameter = " + i_segment);

        Integer possiblePermutationCountForSegment = DbmeaHelperUtils.getPossiblePermutationCountForNumber(i_segment);
        if (n_clones > possiblePermutationCountForSegment - 2)
            throw new RuntimeException("The number of possible clones for a chromosome is more than the possible maximum - 2." +
                    " possible maximum - 2 = " + (possiblePermutationCountForSegment - 2) +
                    ", N_clones parameter = " + n_clones);

        if (n_clones < 2)
            throw new RuntimeException("Minimal count of clones is 2, but n_clones = " + n_clones);
    }

    private void checkConstraintsCloneCount(Integer i_segment_size, Integer n_clones) {

        Integer possiblePermutationCountForSegment = DbmeaHelperUtils.getPossiblePermutationCountForNumber(i_segment_size);
        if (n_clones > possiblePermutationCountForSegment - 1)
            throw new RuntimeException("The number of possible clones for a chromosome is more than the possible maximum - 1." +
                    " possible maximum - 1 = " + (possiblePermutationCountForSegment - 1) +
                    ", N_clones parameter = " + n_clones);

        if (n_clones < 1)
            throw new RuntimeException("Minimal count of clones is 1, but n_clones = " + n_clones);
    }

    private void checkConstraintsForPermutation(int originalChromosomeLength, List<Integer> indexPermutation) {

        if (indexPermutation.size() != originalChromosomeLength)
            throw new RuntimeException("Not enough element in permutation");

        Set<Integer> uniqueElements = new HashSet<>(indexPermutation);

        if (uniqueElements.size() != originalChromosomeLength){
            throw new RuntimeException("Not enough unique elements for a valid permutation.");
        }

        if (Collections.min(uniqueElements) < 0){
            throw new RuntimeException("An element in permutation is less than 0.");
        }

        if (Collections.min(uniqueElements) >= originalChromosomeLength){
            throw new RuntimeException("An element in permutation is more than the size of the chromosome.");
        }
    }

}
