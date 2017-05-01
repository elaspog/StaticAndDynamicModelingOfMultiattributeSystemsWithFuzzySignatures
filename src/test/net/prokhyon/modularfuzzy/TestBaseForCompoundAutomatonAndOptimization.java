package net.prokhyon.modularfuzzy;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;
import net.prokhyon.modularfuzzy.optimalization.ChromosomeElement;
import net.prokhyon.modularfuzzy.optimalization.EvolutionarilyOptimizable;
import net.prokhyon.modularfuzzy.optimalization.Individual;
import net.prokhyon.modularfuzzy.optimalization.fitness.ChromosomeElementCostFunction;

import java.util.*;
import java.util.stream.Collectors;

public class TestBaseForCompoundAutomatonAndOptimization {

    protected static FuzzyAutomaton testFuzzyAutomaton_2node;
    protected static FuzzyAutomaton testFuzzyAutomaton_3node;
    protected static FuzzyAutomaton testFuzzyAutomaton_4node;

    protected static FuzzyState i;
    protected static FuzzyState j;
    protected static FuzzyState x;
    protected static FuzzyState y;
    protected static FuzzyState z;
    protected static FuzzyState a;
    protected static FuzzyState b;
    protected static FuzzyState c;
    protected static FuzzyState d;

    protected static FuzzyState o1s00;
    protected static FuzzyState o1s11;
    protected static FuzzyState o1s12;
    protected static FuzzyState o1s13;
    protected static FuzzyState o1s21;
    protected static FuzzyState o1s22;
    protected static FuzzyState o1s23;
    protected static FuzzyState o1s40;

    protected static FuzzyState o2s00;
    protected static FuzzyState o2s11;
    protected static FuzzyState o2s12;
    protected static FuzzyState o2s13;
    protected static FuzzyState o2s21;
    protected static FuzzyState o2s22;
    protected static FuzzyState o2s23;
    protected static FuzzyState o2s40;
    protected static FuzzyState o2s14;
    protected static FuzzyState o2s15;
    protected static FuzzyState o2s24;
    protected static FuzzyState o2s25;

    public static Double calculateCost(List<List<Double>> costData) {

        final List<Double> components_0 = costData.stream().filter(v -> v != null).map(v -> v.get(0)).collect(Collectors.toList());
        final List<Double> components_1 = costData.stream().filter(v -> v != null).map(v -> v.get(1)).collect(Collectors.toList());

        final double max = Collections.max(components_0).doubleValue();
        final double sum = components_1.stream().mapToDouble(Double::doubleValue).sum();

        return max * sum;
    }

    public static Double calculateCostSimple(List<List<Double>> costData) {

        return costData.stream()
                .flatMap(x -> x.stream())
                .filter(Objects::nonNull)
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    public static Double getCost(List<Double> costData) {

        List<List<Double>> backLead = new ArrayList<>();
        backLead.add(costData);
        return calculateCostSimple(backLead);
    }

    public static <T extends ChromosomeElement> Double fitnessFunction(Individual<T> individual,
                                                                       ChromosomeElementCostFunction<List<List<Double>>> chromosomeElementCostFunction) {


        //chromosomeElementCostFunction.calculateCost()
        return 0.0;
    }

    protected static FuzzyAutomaton initializeTestFuzzyAutomaton_2_node(){

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 2;

        i = new FuzzyState("i", "", null, FuzzyStateTypeEnum.INITIAL);
        j = new FuzzyState("j", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(i);
        fuzzyStates.add(j);

        Double[] costVec1 = {0.1, 0.2};

        fuzzyTransitions.add(new FuzzyTransition("r1", "", Arrays.asList(costVec1), i,j));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_2_node", "fsn2", "fsd2", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_2_node", "fan2", "fad2", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
    }

    protected static FuzzyAutomaton initializeTestFuzzyAutomaton_3_node(){

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 2;

        x = new FuzzyState("x", "", null, FuzzyStateTypeEnum.INITIAL);
        y = new FuzzyState("y", "", null, FuzzyStateTypeEnum.NORMAL);
        z = new FuzzyState("z", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(x);
        fuzzyStates.add(y);
        fuzzyStates.add(z);

        Double[] costVec1 = {0.1, 0.2};
        Double[] costVec2 = {0.2, 0.2};
        Double[] costVec3 = {0.4, 0.1};

        fuzzyTransitions.add(new FuzzyTransition("t1", "", Arrays.asList(costVec1), x,y));
        fuzzyTransitions.add(new FuzzyTransition("t2", "", Arrays.asList(costVec2), y,z));
        fuzzyTransitions.add(new FuzzyTransition("t3", "", Arrays.asList(costVec3), x,z));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_3_node", "fsn3", "fsn3", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_3_node", "fan3", "fad3", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
    }

    protected static FuzzyAutomaton initializeTestFuzzyAutomaton_4_node(){

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 2;

        a = new FuzzyState("a", "", null, FuzzyStateTypeEnum.INITIAL);
        b = new FuzzyState("b", "", null, FuzzyStateTypeEnum.INITIAL);
        c = new FuzzyState("c", "", null, FuzzyStateTypeEnum.NORMAL);
        d = new FuzzyState("d", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(a);
        fuzzyStates.add(b);
        fuzzyStates.add(c);
        fuzzyStates.add(d);

        Double[] costVec1 = {0.1, 0.2};
        Double[] costVec2 = {0.2, 0.2};
        Double[] costVec3 = {0.4, 0.1};
        Double[] costVec4 = {0.2, 0.3};

        fuzzyTransitions.add(new FuzzyTransition("s1", "", Arrays.asList(costVec1), a,b));
        fuzzyTransitions.add(new FuzzyTransition("s2", "", Arrays.asList(costVec2), a,c));
        fuzzyTransitions.add(new FuzzyTransition("s3", "", Arrays.asList(costVec3), b,d));
        fuzzyTransitions.add(new FuzzyTransition("s4", "", Arrays.asList(costVec4), c,d));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_4_node", "fsn4", "fsd4", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_4_node", "fan4", "fad4", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
    }

    protected static FuzzyAutomaton initializeTestFuzzyAutomatonForOptimization1() {

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 1;

        o1s00 = new FuzzyState("s00", "", null, FuzzyStateTypeEnum.INITIAL);
        o1s11 = new FuzzyState("s11", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s12 = new FuzzyState("s12", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s13 = new FuzzyState("s13", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s21 = new FuzzyState("s21", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s22 = new FuzzyState("s22", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s23 = new FuzzyState("s23", "", null, FuzzyStateTypeEnum.NORMAL);
        o1s40 = new FuzzyState("s40", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(o1s00);
        fuzzyStates.add(o1s11);
        fuzzyStates.add(o1s12);
        fuzzyStates.add(o1s13);
        fuzzyStates.add(o1s21);
        fuzzyStates.add(o1s22);
        fuzzyStates.add(o1s23);
        fuzzyStates.add(o1s40);

        Double[] costVec_00_11 = {1.0};
        Double[] costVec_00_12 = {2.0};
        Double[] costVec_00_13 = {3.0};
        Double[] costVec_11_21 = {1.0};
        Double[] costVec_11_22 = {2.0};
        Double[] costVec_11_23 = {3.0};
        Double[] costVec_12_21 = {1.0};
        Double[] costVec_12_22 = {2.0};
        Double[] costVec_12_23 = {3.0};
        Double[] costVec_13_21 = {1.0};
        Double[] costVec_13_22 = {2.0};
        Double[] costVec_13_23 = {3.0};
        Double[] costVec_21_40 = {1.0};
        Double[] costVec_22_40 = {2.0};
        Double[] costVec_23_40 = {3.0};

        fuzzyTransitions.add(new FuzzyTransition("t_00_11", "", Arrays.asList(costVec_00_11), o1s00, o1s11));
        fuzzyTransitions.add(new FuzzyTransition("t_00_12", "", Arrays.asList(costVec_00_12), o1s00, o1s12));
        fuzzyTransitions.add(new FuzzyTransition("t_00_13", "", Arrays.asList(costVec_00_13), o1s00, o1s13));
        fuzzyTransitions.add(new FuzzyTransition("t_11_21", "", Arrays.asList(costVec_11_21), o1s11, o1s21));
        fuzzyTransitions.add(new FuzzyTransition("t_11_22", "", Arrays.asList(costVec_11_22), o1s11, o1s22));
        fuzzyTransitions.add(new FuzzyTransition("t_11_23", "", Arrays.asList(costVec_11_23), o1s11, o1s23));
        fuzzyTransitions.add(new FuzzyTransition("t_12_21", "", Arrays.asList(costVec_12_21), o1s12, o1s21));
        fuzzyTransitions.add(new FuzzyTransition("t_12_22", "", Arrays.asList(costVec_12_22), o1s12, o1s22));
        fuzzyTransitions.add(new FuzzyTransition("t_12_23", "", Arrays.asList(costVec_12_23), o1s12, o1s23));
        fuzzyTransitions.add(new FuzzyTransition("t_13_21", "", Arrays.asList(costVec_13_21), o1s13, o1s21));
        fuzzyTransitions.add(new FuzzyTransition("t_13_22", "", Arrays.asList(costVec_13_22), o1s13, o1s22));
        fuzzyTransitions.add(new FuzzyTransition("t_13_23", "", Arrays.asList(costVec_13_23), o1s13, o1s23));
        fuzzyTransitions.add(new FuzzyTransition("t_21_40", "", Arrays.asList(costVec_21_40), o1s21, o1s40));
        fuzzyTransitions.add(new FuzzyTransition("t_22_40", "", Arrays.asList(costVec_22_40), o1s22, o1s40));
        fuzzyTransitions.add(new FuzzyTransition("t_23_40", "", Arrays.asList(costVec_23_40), o1s23, o1s40));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("", "test_fuzzyset_name_optimization", "test_fuzzyset_descritption_optimization", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("", "test_automaton_name_optimization", "test_automaton_description_optimization", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);

    }

    protected static FuzzyAutomaton initializeTestFuzzyAutomatonForOptimization2() {

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 1;

        o2s00 = new FuzzyState("s00", "", null, FuzzyStateTypeEnum.INITIAL);
        o2s11 = new FuzzyState("s11", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s12 = new FuzzyState("s12", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s13 = new FuzzyState("s13", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s14 = new FuzzyState("s14", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s15 = new FuzzyState("s15", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s21 = new FuzzyState("s21", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s22 = new FuzzyState("s22", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s23 = new FuzzyState("s23", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s24 = new FuzzyState("s24", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s25 = new FuzzyState("s25", "", null, FuzzyStateTypeEnum.NORMAL);
        o2s40 = new FuzzyState("s40", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(o2s00);
        fuzzyStates.add(o2s11);
        fuzzyStates.add(o2s12);
        fuzzyStates.add(o2s13);
        fuzzyStates.add(o2s14);
        fuzzyStates.add(o2s15);
        fuzzyStates.add(o2s21);
        fuzzyStates.add(o2s22);
        fuzzyStates.add(o2s23);
        fuzzyStates.add(o2s24);
        fuzzyStates.add(o2s25);
        fuzzyStates.add(o2s40);

        Double[] costVec_00_11 = {10.0};
        Double[] costVec_00_12 = {20.0};
        Double[] costVec_00_13 = {30.0};
        Double[] costVec_00_14 = {26.0};
        Double[] costVec_00_15 = {25.0};
        Double[] costVec_11_21 = {10.0};
        Double[] costVec_11_22 = {20.0};
        Double[] costVec_11_23 = {30.0};
        Double[] costVec_12_21 = {10.0};
        Double[] costVec_12_22 = {20.0};
        Double[] costVec_12_23 = {30.0};
        Double[] costVec_13_21 = {10.0};
        Double[] costVec_13_22 = {20.0};
        Double[] costVec_13_23 = {30.0};
        Double[] costVec_14_24 = {14.0};
        Double[] costVec_15_25 = {15.0};
        Double[] costVec_21_40 = {10.0};
        Double[] costVec_22_40 = {20.0};
        Double[] costVec_23_40 = {30.0};
        Double[] costVec_24_15 = {05.0};
        Double[] costVec_24_40 = {05.0};
        Double[] costVec_25_14 = {35.0};
        Double[] costVec_25_40 = {34.0};

        fuzzyTransitions.add(new FuzzyTransition("t_00_11", "", Arrays.asList(costVec_00_11),o2s00, o2s11));
        fuzzyTransitions.add(new FuzzyTransition("t_00_12", "", Arrays.asList(costVec_00_12),o2s00, o2s12));
        fuzzyTransitions.add(new FuzzyTransition("t_00_13", "", Arrays.asList(costVec_00_13),o2s00, o2s13));
        fuzzyTransitions.add(new FuzzyTransition("t_11_21", "", Arrays.asList(costVec_11_21),o2s11, o2s21));
        fuzzyTransitions.add(new FuzzyTransition("t_11_22", "", Arrays.asList(costVec_11_22),o2s11, o2s22));
        fuzzyTransitions.add(new FuzzyTransition("t_11_23", "", Arrays.asList(costVec_11_23),o2s11, o2s23));
        fuzzyTransitions.add(new FuzzyTransition("t_12_21", "", Arrays.asList(costVec_12_21),o2s12, o2s21));
        fuzzyTransitions.add(new FuzzyTransition("t_12_22", "", Arrays.asList(costVec_12_22),o2s12, o2s22));
        fuzzyTransitions.add(new FuzzyTransition("t_12_23", "", Arrays.asList(costVec_12_23),o2s12, o2s23));
        fuzzyTransitions.add(new FuzzyTransition("t_13_21", "", Arrays.asList(costVec_13_21),o2s13, o2s21));
        fuzzyTransitions.add(new FuzzyTransition("t_13_22", "", Arrays.asList(costVec_13_22),o2s13, o2s22));
        fuzzyTransitions.add(new FuzzyTransition("t_13_23", "", Arrays.asList(costVec_13_23),o2s13, o2s23));
        fuzzyTransitions.add(new FuzzyTransition("t_21_40", "", Arrays.asList(costVec_21_40),o2s21, o2s40));
        fuzzyTransitions.add(new FuzzyTransition("t_22_40", "", Arrays.asList(costVec_22_40),o2s22, o2s40));
        fuzzyTransitions.add(new FuzzyTransition("t_23_40", "", Arrays.asList(costVec_23_40),o2s23, o2s40));

        fuzzyTransitions.add(new FuzzyTransition("t_00_14", "", Arrays.asList(costVec_00_14),o2s00, o2s14));
        fuzzyTransitions.add(new FuzzyTransition("t_00_15", "", Arrays.asList(costVec_00_15),o2s00, o2s15));
        fuzzyTransitions.add(new FuzzyTransition("t_14_24", "", Arrays.asList(costVec_14_24),o2s14, o2s24));
        fuzzyTransitions.add(new FuzzyTransition("t_15_25", "", Arrays.asList(costVec_15_25),o2s15, o2s25));
        fuzzyTransitions.add(new FuzzyTransition("t_24_15", "", Arrays.asList(costVec_24_15),o2s24, o2s15));
        fuzzyTransitions.add(new FuzzyTransition("t_24_40", "", Arrays.asList(costVec_24_40),o2s24, o2s40));
        fuzzyTransitions.add(new FuzzyTransition("t_25_14", "", Arrays.asList(costVec_25_14),o2s25, o2s14));
        fuzzyTransitions.add(new FuzzyTransition("t_25_40", "", Arrays.asList(costVec_25_40),o2s25, o2s40));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("", "test_fuzzyset_name_optimization", "test_fuzzyset_descritption_optimization", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("", "test_automaton_name_optimization", "test_automaton_description_optimization", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);

    }

}
