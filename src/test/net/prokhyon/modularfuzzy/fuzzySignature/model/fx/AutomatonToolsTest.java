package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.TestBaseForCompoundAutomatonAndOptimization;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AutomatonToolsTest extends TestBaseForCompoundAutomatonAndOptimization {

    static FuzzyState fs1 = new FuzzyState("fs1", "fs1descr", null, FuzzyStateTypeEnum.INITIAL);
    static FuzzyState fs2 = new FuzzyState("fs2", "fs2descr", null, FuzzyStateTypeEnum.NORMAL);
    static FuzzyState fs3 = new FuzzyState("fs3", "fs3descr", null, FuzzyStateTypeEnum.NORMAL);
    static FuzzyState fs4 = new FuzzyState("fs4", "fs4descr", null, FuzzyStateTypeEnum.TERMINAL);
    static FuzzyState fs5 = new FuzzyState("fs5", "fs5descr", null, FuzzyStateTypeEnum.NORMAL);

    static CompoundFuzzyState cfs1 = new CompoundFuzzyState();     /// cfs1 is a non matching compound - the count of states is less than expected
    static CompoundFuzzyState cfs2 = new CompoundFuzzyState();     /// cfs2 is a non matching compound - but only by algorithmic constraints
    static CompoundFuzzyState cfs3 = new CompoundFuzzyState();     /// cfs3 is a matching compound state for (<fs1, fs2, fs3>, <fs4>)
    static CompoundFuzzyState cfs4 = new CompoundFuzzyState();     /// cfs4 is a non matching compound - empty compound
    static CompoundFuzzyState cfs5 = new CompoundFuzzyState();     /// cfs5 is a non matching compound - the count of states is more than expected
    static CompoundFuzzyState cfs6 = new CompoundFuzzyState();     /// cfs6 is a non matching compound - wrong order
    static CompoundFuzzyState cfs7 = new CompoundFuzzyState();     /// cfs7 is a matching compound state for (<fs1, fs2, fs3>, <fs4>)
    static CompoundFuzzyState cfs8 = new CompoundFuzzyState();     /// cfs7 is a non matching compound - the count of states is less than expected

    @BeforeClass
    public static void runOnceBeforeClass() {

        cfs1.getFuzzyStateTuple().add(fs1);
        cfs1.getFuzzyStateTuple().add(fs2);
        cfs1.getFuzzyStateTuple().add(fs3);

        cfs2.getFuzzyStateTuple().add(fs3);
        cfs2.getFuzzyStateTuple().add(fs2);
        cfs2.getFuzzyStateTuple().add(fs1);
        cfs2.getFuzzyStateTuple().add(fs4);

        cfs3.getFuzzyStateTuple().add(fs1);
        cfs3.getFuzzyStateTuple().add(fs2);
        cfs3.getFuzzyStateTuple().add(fs3);
        cfs3.getFuzzyStateTuple().add(fs4);

        cfs5.getFuzzyStateTuple().add(fs1);
        cfs5.getFuzzyStateTuple().add(fs2);
        cfs5.getFuzzyStateTuple().add(fs3);
        cfs5.getFuzzyStateTuple().add(fs4);
        cfs5.getFuzzyStateTuple().add(fs5);

        cfs6.getFuzzyStateTuple().add(fs3);
        cfs6.getFuzzyStateTuple().add(fs5);
        cfs6.getFuzzyStateTuple().add(fs2);
        cfs6.getFuzzyStateTuple().add(fs4);

        cfs7.getFuzzyStateTuple().add(fs1);
        cfs7.getFuzzyStateTuple().add(fs2);
        cfs7.getFuzzyStateTuple().add(fs3);
        cfs7.getFuzzyStateTuple().add(fs4);

        cfs8.getFuzzyStateTuple().add(fs1);
        cfs8.getFuzzyStateTuple().add(fs2);
        cfs8.getFuzzyStateTuple().add(fs3);
        cfs8.getFuzzyStateTuple().add(fs5);

        testFuzzyAutomaton_2node = initializeTestFuzzyAutomaton_2_node();
        testFuzzyAutomaton_3node = initializeTestFuzzyAutomaton_3_node();
        testFuzzyAutomaton_4node = initializeTestFuzzyAutomaton_4_node();
    }

    @Test
    public void testGetOutgoingEdgesForAStateOfAnAutomaton() throws Exception {

        Assert.assertEquals(1, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(i, testFuzzyAutomaton_2node).size());
        Assert.assertEquals(0, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(j, testFuzzyAutomaton_2node).size());

        Assert.assertEquals(2, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(x, testFuzzyAutomaton_3node).size());
        Assert.assertEquals(1, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(y, testFuzzyAutomaton_3node).size());
        Assert.assertEquals(0, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(z, testFuzzyAutomaton_3node).size());

        Assert.assertEquals(2, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(a, testFuzzyAutomaton_4node).size());
        Assert.assertEquals(1, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(b, testFuzzyAutomaton_4node).size());
        Assert.assertEquals(1, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(c, testFuzzyAutomaton_4node).size());
        Assert.assertEquals(0, AutomatonTools.getOutgoingEdgesForAStateOfAnAutomaton(d, testFuzzyAutomaton_4node).size());
    }


    @Test
    public void testGetInitStatesOfFuzzyAutomaton() throws Exception {

        final List<FuzzyState> initStatesOfFuzzyAutomaton_2node = AutomatonTools.getInitStatesOfFuzzyAutomaton(testFuzzyAutomaton_2node);
        final List<FuzzyState> initStatesOfFuzzyAutomaton_3node = AutomatonTools.getInitStatesOfFuzzyAutomaton(testFuzzyAutomaton_3node);
        final List<FuzzyState> initStatesOfFuzzyAutomaton_4node = AutomatonTools.getInitStatesOfFuzzyAutomaton(testFuzzyAutomaton_4node);

        CompoundFuzzyAutomaton compoundFuzzyAutomaton_2node = new CompoundFuzzyAutomaton(testFuzzyAutomaton_2node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton_3node = new CompoundFuzzyAutomaton(testFuzzyAutomaton_3node);
        CompoundFuzzyAutomaton compoundFuzzyAutomaton_4node = new CompoundFuzzyAutomaton(testFuzzyAutomaton_4node);

        Assert.assertEquals(1, initStatesOfFuzzyAutomaton_2node.size());
        Assert.assertEquals(1, initStatesOfFuzzyAutomaton_3node.size());
        Assert.assertEquals(2, initStatesOfFuzzyAutomaton_4node.size());

        Assert.assertEquals(1, AutomatonTools.getInitializationStatesOfCompoundAutomaton(compoundFuzzyAutomaton_2node).size());
        Assert.assertEquals(1, AutomatonTools.getInitializationStatesOfCompoundAutomaton(compoundFuzzyAutomaton_3node).size());
        Assert.assertEquals(2, AutomatonTools.getInitializationStatesOfCompoundAutomaton(compoundFuzzyAutomaton_4node).size());
    }

    @Test
    public void test1_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs2);

        final CompoundFuzzyState searchResult = AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);

        /// the size of the input used for searching is N-1 if the result is N sized
        Assert.assertEquals(3, cfs1.getFuzzyStateTuple().size());
        for (CompoundFuzzyState compoundFuzzyState : store) {
            Assert.assertEquals(4, compoundFuzzyState.getFuzzyStateTuple().size());
        }
        Assert.assertEquals(4, searchResult.getFuzzyStateTuple().size());

        /// a new state is created by fuzzy states of cfs1 and fs4 state
        Assert.assertNotNull(searchResult);

        /// the reference of the returned compound state is not equal to the reference of the cfs1
        Assert.assertNotEquals(cfs1, searchResult);

        final int max = Math.max(cfs3.getFuzzyStateTuple().size(), cfs1.getFuzzyStateTuple().size());
        for (int i = 0; i < max - 1; i++){

            /// the first N-1 state of the returned compound state equals to all elements of the cfs1
            Assert.assertEquals(searchResult.getFuzzyStateTuple().get(i), cfs1.getFuzzyStateTuple().get(i));
            Assert.assertEquals(cfs3.getFuzzyStateTuple().get(i), cfs1.getFuzzyStateTuple().get(i));
        }

        /// the first Nth state of the returned compound state equals to all elements of the fs4
        Assert.assertEquals(searchResult.getFuzzyStateTuple().get(max-1), fs4);
        Assert.assertEquals(cfs3.getFuzzyStateTuple().get(max-1), fs4);

    }

    @Test
    public void test2_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs3);
        store.add(cfs2);

        final CompoundFuzzyState searchResult = AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);

        /// the size of the input used for searching is N-1 if the result is N sized
        Assert.assertEquals(3, cfs1.getFuzzyStateTuple().size());
        for (CompoundFuzzyState compoundFuzzyState : store) {
            Assert.assertEquals(4, compoundFuzzyState.getFuzzyStateTuple().size());
        }
        Assert.assertEquals(4, searchResult.getFuzzyStateTuple().size());

        /// the cfs3 is found by fuzzy states of cfs1 and fs4 state
        Assert.assertNotNull(searchResult);

        /// the returned compound state equals to cfs3
        Assert.assertEquals(cfs3, searchResult);

        /// the reference of the returned compound state is not equal to the reference of the cfs1
        Assert.assertNotEquals(cfs1, searchResult);

        final int max = Math.max(cfs3.getFuzzyStateTuple().size(), cfs1.getFuzzyStateTuple().size());
        for (int i = 0; i < max - 1; i++){

            /// the first N-1 state of the returned compound state equals to all elements of the cfs1
            Assert.assertEquals(searchResult.getFuzzyStateTuple().get(i), cfs1.getFuzzyStateTuple().get(i));
            Assert.assertEquals(cfs3.getFuzzyStateTuple().get(i), cfs1.getFuzzyStateTuple().get(i));
        }

        /// the first Nth state of the returned compound state equals to all elements of the fs4
        Assert.assertEquals(searchResult.getFuzzyStateTuple().get(max-1), fs4);
        Assert.assertEquals(cfs3.getFuzzyStateTuple().get(max-1), fs4);

    }

    @Test (expected=RuntimeException.class)
    public void testAlgorithmError1_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs3);
        store.add(cfs7);

        /// generates RuntimeException
        AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);
    }

    @Test (expected=RuntimeException.class)
    public void testAlgorithmError2_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs3);
        store.add(cfs4);    // empty compound state

        /// generates RuntimeException
        AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);
    }

    @Test (expected=RuntimeException.class)
    public void testAlgorithmError3_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs8);    // last element of compound state does not match
        store.add(cfs5);

        /// generates RuntimeException
        AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);
    }

    @Test (expected=RuntimeException.class)
    public void testAlgorithmError4_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs3);
        store.add(cfs5);    // the count of states is more than expected

        /// generates RuntimeException
        AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs1, fs4);
    }

    @Test (expected=RuntimeException.class)
    public void testAlgorithmError5_searchForOrCreateNewUnfinishedIncreasedSizeNode() throws Exception {

        List<CompoundFuzzyState> store = new ArrayList<>();
        store.add(cfs6);
        store.add(cfs3);
        store.add(cfs2);

        /// generates RuntimeException
        AutomatonTools.searchForOrCreateNewUnfinishedIncreasedSizeNode(store, cfs5, fs4);
    }

}