package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

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

public class AutomatonTestBase {

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

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_2_node", "test_fuzzyset_name_2_node", "test_fuzzyset_descritption_2_node", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_2_node", "test_automaton_name_2_node", "test_automaton_description_2_node", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
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

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_3_node", "test_fuzzyset_name_3_node", "test_fuzzyset_descritption_3_node", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_3_node", "test_automaton_name_3_node", "test_automaton_description_3_node", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
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

        FuzzySetSystem fuzzySetSystem = new FuzzySetSystem("test_setsystem_UUID_4_node", "test_fuzzyset_name_4_node", "test_fuzzyset_descritption_4_node", FuzzySetSystemTypeEnum.CUSTOM, fuzzySets);
        return new FuzzyAutomaton("test_automaton_UUID_4_node", "test_automaton_name_4_node", "test_automaton_description_4_node", fuzzyStates, fuzzyTransitions, fuzzySetSystem, costVectorDimension);
    }

}
