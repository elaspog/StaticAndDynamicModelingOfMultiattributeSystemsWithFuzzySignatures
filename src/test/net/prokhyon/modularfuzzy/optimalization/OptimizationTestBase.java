package net.prokhyon.modularfuzzy.optimalization;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptimizationTestBase {

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

    protected static FuzzyAutomaton initializeTestFuzzyAutomatonForOptimization() {

        List<FuzzySet> fuzzySets = new ArrayList<>();   // will remain empty for the test
        List<FuzzyState> fuzzyStates = new ArrayList<>();
        List<FuzzyTransition> fuzzyTransitions = new ArrayList<>();
        Integer costVectorDimension = 1;

        FuzzyState s00 = new FuzzyState("s00", "", null, FuzzyStateTypeEnum.INITIAL);
        FuzzyState s11 = new FuzzyState("s11", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s21 = new FuzzyState("s21", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s31 = new FuzzyState("s31", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s12 = new FuzzyState("s21", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s22 = new FuzzyState("s22", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s32 = new FuzzyState("s32", "", null, FuzzyStateTypeEnum.NORMAL);
        FuzzyState s40 = new FuzzyState("s40", "", null, FuzzyStateTypeEnum.TERMINAL);

        fuzzyStates.add(s00);
        fuzzyStates.add(s11);
        fuzzyStates.add(s21);
        fuzzyStates.add(s31);
        fuzzyStates.add(s12);
        fuzzyStates.add(s22);
        fuzzyStates.add(s32);
        fuzzyStates.add(s40);

        Double[] costVec_00_11 = {1.0};
        Double[] costVec_00_21 = {2.0};
        Double[] costVec_00_31 = {3.0};
        Double[] costVec_11_12 = {1.0};
        Double[] costVec_11_22 = {2.0};
        Double[] costVec_11_32 = {3.0};
        Double[] costVec_21_12 = {1.0};
        Double[] costVec_21_22 = {2.0};
        Double[] costVec_21_32 = {3.0};
        Double[] costVec_31_12 = {1.0};
        Double[] costVec_31_22 = {2.0};
        Double[] costVec_31_32 = {3.0};
        Double[] costVec_12_40 = {1.0};
        Double[] costVec_22_40 = {2.0};
        Double[] costVec_32_40 = {3.0};

        fuzzyTransitions.add(new FuzzyTransition("t_00_11", "", Arrays.asList(costVec_00_11), s00, s11));
        fuzzyTransitions.add(new FuzzyTransition("t_00_21", "", Arrays.asList(costVec_00_21), s00, s21));
        fuzzyTransitions.add(new FuzzyTransition("t_00_31", "", Arrays.asList(costVec_00_31), s00, s31));
        fuzzyTransitions.add(new FuzzyTransition("t_11_12", "", Arrays.asList(costVec_11_12), s11, s12));
        fuzzyTransitions.add(new FuzzyTransition("t_11_22", "", Arrays.asList(costVec_11_22), s11, s22));
        fuzzyTransitions.add(new FuzzyTransition("t_11_32", "", Arrays.asList(costVec_11_32), s11, s32));
        fuzzyTransitions.add(new FuzzyTransition("t_21_12", "", Arrays.asList(costVec_21_12), s21, s12));
        fuzzyTransitions.add(new FuzzyTransition("t_21_22", "", Arrays.asList(costVec_21_22), s21, s22));
        fuzzyTransitions.add(new FuzzyTransition("t_21_32", "", Arrays.asList(costVec_21_32), s21, s32));
        fuzzyTransitions.add(new FuzzyTransition("t_31_12", "", Arrays.asList(costVec_31_12), s31, s12));
        fuzzyTransitions.add(new FuzzyTransition("t_31_22", "", Arrays.asList(costVec_31_22), s31, s22));
        fuzzyTransitions.add(new FuzzyTransition("t_31_32", "", Arrays.asList(costVec_31_32), s31, s32));
        fuzzyTransitions.add(new FuzzyTransition("t_12_40", "", Arrays.asList(costVec_12_40), s12, s40));
        fuzzyTransitions.add(new FuzzyTransition("t_22_40", "", Arrays.asList(costVec_22_40), s22, s40));
        fuzzyTransitions.add(new FuzzyTransition("t_32_40", "", Arrays.asList(costVec_32_40), s32, s40));

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("", "test_fuzzyset_name_optimization", "test_fuzzyset_descritption_optimization", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("", "test_automaton_name_optimization", "test_automaton_description_optimization", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);

    }


}
