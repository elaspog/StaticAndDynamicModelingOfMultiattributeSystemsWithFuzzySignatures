package net.prokhyon.modularfuzzy.fuzzySignature.view;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GraphVisualizationsHelperUtil {

    public static String generateSignatureJsonForJavascriptGui(FuzzySignature fuzzySignatureToLoad){

        final FuzzyNode rootNodeOfTheTree = fuzzySignatureToLoad.getRootNodeOfTheTree();
        return getJsonContentOfChildren(rootNodeOfTheTree);
    }

    private static String getJsonContentOfChildren(FuzzyNode fuzzyNode){

        final String fuzzyNodeName = fuzzyNode.getFuzzyNodeName();
        final List<FuzzyNode> childNodes = fuzzyNode.getChildNodes();

        String nodeStructure = "{\"name\": \"" + fuzzyNodeName + "\"";
        String childrenArray = "";
        if(childNodes != null && childNodes.size() > 0){
            for (FuzzyNode childNode : childNodes) {
                if (! childrenArray.equals("")){
                    childrenArray += ", ";
                }
                childrenArray += getJsonContentOfChildren(childNode);
            }
            childrenArray = ",\"children\":[" + childrenArray + "]";
        }
        nodeStructure += childrenArray + "}";
        return nodeStructure;
    }


    public static String generateTransitionsJsonForJavascriptGui(CompoundFuzzyAutomaton fuzzyAutomatonToLoad) {

        int i = 0;
        String transitionsNewJson = "";
        for (CompoundFuzzyTransition fuzzyTransition : fuzzyAutomatonToLoad.getCompoundFuzzyTransitions()) {

            final FuzzyAutomaton fuzzyAutomaton = fuzzyAutomatonToLoad.getFuzzyAutomatonTuple().get(i);
            final int costVectorDimensionInt = fuzzyAutomaton.getCostVectorDimensionInt();

            if (! transitionsNewJson.equals("")){
                transitionsNewJson += ", ";
            }

            final String costVector = "[" + (fuzzyTransition == null ? "_" :
                    fuzzyTransition.getTransitions().stream()
                            .map(ft -> "[" + (ft == null ?
                                    IntStream.range(0, costVectorDimensionInt)
                                            .mapToObj(j -> ((Integer) j).toString())
                                            .map((j)->"\"\"").collect(Collectors.joining(", "))
                                    :
                                    ft.getCostVector().stream()
                                            .map(cv -> "\"" + (cv == null ? "?" : cv.toString()) + "\"")
                                            .collect(Collectors.joining(", "))) + "]")
                            .collect(Collectors.joining(", "))) + "]";

            transitionsNewJson += "{\"name\": \"" + fuzzyTransition.getAggregatedTransitionName()
                    // TODO: + "\", \"description\" : \"" + "null" //fuzzyTransition.getAggregatedTransitionDescription()
                    + "\", \"costVector\" : " + costVector +
                    ", \"from\": \"" + fuzzyTransition.getFromState().getAggregatedStateName()
                    + "\", \"to\": \"" + fuzzyTransition.getToState().getAggregatedStateName()
                    + "\"}";
        }
        return "[" + transitionsNewJson + "]";
    }

    public static String generateStatesJsonForJavascriptGui(CompoundFuzzyAutomaton fuzzyAutomatonToLoad) {

        String statesNewJson = "";
        for (CompoundFuzzyState fuzzyState : fuzzyAutomatonToLoad.getCompoundFuzzyStates()) {
            if (! statesNewJson.equals("")){
                statesNewJson += ", ";
            }
            statesNewJson += "{\"name\": \"" + fuzzyState.getAggregatedStateName()
                    // TODO: + "\", \"description\": \"" + "null" //fuzzyState.getFuzzyStateDescription()
                    + "\", \"type\": \"" + fuzzyState.getAggregatedStateType().toString().toLowerCase()
                    // TODO: + "\", \"set\": \"" + "dummyset" //(fuzzyState.getFuzzySet() != null ? fuzzyState.getFuzzySet().getFuzzySetName() : "")
                    + "\"}";
        }
        return "[" + statesNewJson + "]";
    }

}
