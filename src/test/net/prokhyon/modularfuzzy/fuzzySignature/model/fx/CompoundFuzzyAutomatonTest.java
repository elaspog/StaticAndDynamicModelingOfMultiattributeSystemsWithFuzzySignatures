package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;
import org.junit.*;
import java.util.List;
import java.util.stream.Collectors;

public class CompoundFuzzyAutomatonTest extends AutomatonTestBase {

    @BeforeClass
    public static void runOnceBeforeClass() {

        testFuzzyAutomaton_2node = initializeTestFuzzyAutomaton_2_node();
        testFuzzyAutomaton_3node = initializeTestFuzzyAutomaton_3_node();
        testFuzzyAutomaton_4node = initializeTestFuzzyAutomaton_4_node();
    }

    @Test
    public void testCompoundAutomatonCreation() throws Exception {

        CompoundFuzzyAutomaton compoundFuzzyAutomaton = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);

        final List<FuzzyAutomaton> fuzzyAutomatonTuple = compoundFuzzyAutomaton.getFuzzyAutomatonTuple();
        final List<CompoundFuzzyState> compoundFuzzyStates = compoundFuzzyAutomaton.getCompoundFuzzyStates();
        final List<CompoundFuzzyTransition> compoundFuzzyTransitions = compoundFuzzyAutomaton.getCompoundFuzzyTransitions();

        /// one automaton is stored in compound automaton
        Assert.assertEquals(1, fuzzyAutomatonTuple.size());

        // the first automaton in compound automaton was the one which was passes as constructor parameter
        Assert.assertEquals(testFuzzyAutomaton_3node, fuzzyAutomatonTuple.get(0));

        // for all original states and transitions the compound state and compound transition were created
        Assert.assertEquals(testFuzzyAutomaton_3node.getFuzzyStates().size(), compoundFuzzyStates.size());
        Assert.assertEquals(testFuzzyAutomaton_3node.getFuzzyTransitions().size(), compoundFuzzyTransitions.size());

        for (CompoundFuzzyState compoundFuzzyState : compoundFuzzyStates) {

            /// each compound state contains only one of the original states
            final List<FuzzyState> fuzzyStateTuple = compoundFuzzyState.getFuzzyStateTuple();
            Assert.assertEquals(1, fuzzyStateTuple.size());

            /// each compound state is one of the original states
            final FuzzyState fuzzyState = fuzzyStateTuple.get(0);
            Assert.assertTrue(testFuzzyAutomaton_3node.getFuzzyStates().contains(fuzzyState));

            final List<CompoundFuzzyTransition> incomingEdges = compoundFuzzyState.getIncomingEdges();
            final List<CompoundFuzzyTransition> outgoingEdges = compoundFuzzyState.getOutgoingEdges();

            for (CompoundFuzzyTransition incomingEdge : incomingEdges) {

                /// each incoming edge of compound state is a compound transition of the automaton
                Assert.assertTrue(compoundFuzzyTransitions.contains(incomingEdge));
            }

            for (CompoundFuzzyTransition outgoingEdge : outgoingEdges) {

                /// each outgoing edge of compound state is a compound transition of the automaton
                Assert.assertTrue(compoundFuzzyTransitions.contains(outgoingEdge));
            }
        }

        final List<FuzzyState> originalFromStates = testFuzzyAutomaton_3node.getFuzzyTransitions().stream().map(x -> x.getFromState()).collect(Collectors.toList());
        final List<FuzzyState> originalToStates = testFuzzyAutomaton_3node.getFuzzyTransitions().stream().map(x -> x.getToState()).collect(Collectors.toList());

        final List<FuzzyState> firstFromStatesOfCompounds = compoundFuzzyTransitions.stream().map(x -> x.getFromState().getFuzzyStateTuple().get(0)).collect(Collectors.toList());
        final List<FuzzyState> firstToStatesOfCompounds = compoundFuzzyTransitions.stream().map(x -> x.getToState().getFuzzyStateTuple().get(0)).collect(Collectors.toList());

        /// the count of original from/to states is equal to the first element of generated state compounds
        Assert.assertEquals(originalFromStates.size(), firstFromStatesOfCompounds.size());
        Assert.assertEquals(originalToStates.size(), firstToStatesOfCompounds.size());

        for (FuzzyTransition fuzzyTransition : testFuzzyAutomaton_3node.getFuzzyTransitions()) {

            /// each from/to state is stored in compound
            Assert.assertTrue(firstFromStatesOfCompounds.contains(fuzzyTransition.getFromState()));
            Assert.assertTrue(firstToStatesOfCompounds.contains(fuzzyTransition.getToState()));
        }

        final List<FuzzyState> firstFuzzyStatesOfCompoundFuzzyStates
                = compoundFuzzyStates.stream().map(x -> x.getFuzzyStateTuple().get(0)).collect(Collectors.toList());

        for (CompoundFuzzyTransition compoundFuzzyTransition : compoundFuzzyTransitions) {

            final CompoundFuzzyState fromCompoundState = compoundFuzzyTransition.getFromState();
            final CompoundFuzzyState toCompoundState = compoundFuzzyTransition.getToState();

            /// each from/to compound state of the transitions refers to a state of the compound automaton
            Assert.assertTrue(compoundFuzzyStates.contains(fromCompoundState));
            Assert.assertTrue(compoundFuzzyStates.contains(toCompoundState));

            /// each fuzzy state of from/to compound state of the transition refers to an original fuzzy state
            Assert.assertTrue(firstFuzzyStatesOfCompoundFuzzyStates.contains(fromCompoundState.getFuzzyStateTuple().get(0)));
            Assert.assertTrue(firstFuzzyStatesOfCompoundFuzzyStates.contains(toCompoundState.getFuzzyStateTuple().get(0)));

            /// each state is checkable by equalsInPosition method
            Assert.assertTrue(fromCompoundState.equalsInPosition(0, fromCompoundState.getFuzzyStateTuple().get(0)));
            Assert.assertTrue(toCompoundState.equalsInPosition(0, toCompoundState.getFuzzyStateTuple().get(0)));

            final List<CompoundFuzzyTransition> incomingEdgesOfFromCompoundState = fromCompoundState.getIncomingEdges();
            final List<CompoundFuzzyTransition> outgoingEdgesOfFromCompoundState = fromCompoundState.getOutgoingEdges();
            final List<CompoundFuzzyTransition> incomingEdgesOfToCompoundState = toCompoundState.getIncomingEdges();
            final List<CompoundFuzzyTransition> outgoingEdgesOfToCompoundState = toCompoundState.getOutgoingEdges();

            /// checking endpoint compound states of the transition
            Assert.assertTrue(outgoingEdgesOfFromCompoundState.contains(compoundFuzzyTransition));
            Assert.assertTrue(incomingEdgesOfToCompoundState.contains(compoundFuzzyTransition));

            /// checking some other places where the transition is prohibited
            Assert.assertFalse(incomingEdgesOfFromCompoundState.contains(compoundFuzzyTransition));
            Assert.assertFalse(outgoingEdgesOfToCompoundState.contains(compoundFuzzyTransition));
        }

    }

    @Test
    public void testCompoundAutomationProductOperatorOneTime() throws Exception {

        CompoundFuzzyAutomaton compoundFuzzyAutomaton = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);

        Assert.assertEquals(testFuzzyAutomaton_3node.getFuzzyStates().size(), compoundFuzzyAutomaton.getCompoundFuzzyStates().size());
        Assert.assertEquals(testFuzzyAutomaton_3node.getFuzzyTransitions().size(), compoundFuzzyAutomaton.getCompoundFuzzyTransitions().size());

        compoundFuzzyAutomaton.extendExistingCompoundWith(testFuzzyAutomaton_2node);

        final int expectedNodeCount = testFuzzyAutomaton_3node.getFuzzyStates().size() * testFuzzyAutomaton_2node.getFuzzyStates().size();
        final int expectedTransitionCount = 12;

        Assert.assertEquals(expectedNodeCount, compoundFuzzyAutomaton.getCompoundFuzzyStates().size());
        Assert.assertEquals(expectedTransitionCount, compoundFuzzyAutomaton.getCompoundFuzzyTransitions().size());
    }

    @Test
    public void testSymmetricityOfProductOperatorOnTwoAutomatons() throws Exception {

        CompoundFuzzyAutomaton compoundFuzzyAutomaton1 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton1.extendExistingCompoundWith(testFuzzyAutomaton_2node);

        CompoundFuzzyAutomaton compoundFuzzyAutomaton2 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton2.extendExistingCompoundWith(testFuzzyAutomaton_3node);

        final int expectedNodeCount = 6;

        Assert.assertEquals(expectedNodeCount, compoundFuzzyAutomaton1.getCompoundFuzzyStates().size());
        Assert.assertEquals(expectedNodeCount, compoundFuzzyAutomaton2.getCompoundFuzzyStates().size());

        final int expectedTransitionCount = 12;

        Assert.assertEquals(expectedTransitionCount, compoundFuzzyAutomaton1.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(expectedTransitionCount, compoundFuzzyAutomaton2.getCompoundFuzzyTransitions().size());
    }

    @Test
    public void testSymmetricityOfProductOperatorAfterThreeMultiplication() throws Exception {

        CompoundFuzzyAutomaton compoundFuzzyAutomaton234 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton243 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton324 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton342 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton423 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_4node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton432 = new CompoundFuzzyAutomaton(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton234.extendExistingCompoundWith(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton234.extendExistingCompoundWith(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton243.extendExistingCompoundWith(testFuzzyAutomaton_4node);
        compoundFuzzyAutomaton243.extendExistingCompoundWith(testFuzzyAutomaton_3node);

        compoundFuzzyAutomaton324.extendExistingCompoundWith(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton324.extendExistingCompoundWith(testFuzzyAutomaton_4node);

        compoundFuzzyAutomaton342.extendExistingCompoundWith(testFuzzyAutomaton_4node);
        compoundFuzzyAutomaton342.extendExistingCompoundWith(testFuzzyAutomaton_2node);

        compoundFuzzyAutomaton423.extendExistingCompoundWith(testFuzzyAutomaton_2node);
        compoundFuzzyAutomaton423.extendExistingCompoundWith(testFuzzyAutomaton_3node);

        compoundFuzzyAutomaton432.extendExistingCompoundWith(testFuzzyAutomaton_3node);
        compoundFuzzyAutomaton432.extendExistingCompoundWith(testFuzzyAutomaton_2node);

        Assert.assertEquals(24, compoundFuzzyAutomaton234.getCompoundFuzzyStates().size());
        Assert.assertEquals(24, compoundFuzzyAutomaton243.getCompoundFuzzyStates().size());
        Assert.assertEquals(24, compoundFuzzyAutomaton324.getCompoundFuzzyStates().size());
        Assert.assertEquals(24, compoundFuzzyAutomaton342.getCompoundFuzzyStates().size());
        Assert.assertEquals(24, compoundFuzzyAutomaton423.getCompoundFuzzyStates().size());
        Assert.assertEquals(24, compoundFuzzyAutomaton432.getCompoundFuzzyStates().size());

        Assert.assertEquals(120, compoundFuzzyAutomaton234.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(120, compoundFuzzyAutomaton243.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(120, compoundFuzzyAutomaton324.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(120, compoundFuzzyAutomaton342.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(120, compoundFuzzyAutomaton423.getCompoundFuzzyTransitions().size());
        Assert.assertEquals(120, compoundFuzzyAutomaton432.getCompoundFuzzyTransitions().size());
    }

}