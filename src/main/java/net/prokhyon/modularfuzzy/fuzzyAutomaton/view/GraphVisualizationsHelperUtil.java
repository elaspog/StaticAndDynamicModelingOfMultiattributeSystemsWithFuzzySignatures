package net.prokhyon.modularfuzzy.fuzzyAutomaton.view;

import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition;

public class GraphVisualizationsHelperUtil {

    public static String generateTransitionsJsonForJavascriptGui(FuzzyAutomaton fuzzyAutomatonToLoad) {

        System.out.println("_t");
        String transitionsNewJson = "";
        for (FuzzyTransition fuzzyTransition : fuzzyAutomatonToLoad.getFuzzyTransitions()) {
            if (! transitionsNewJson.equals("")){
                transitionsNewJson += ", ";
            }

            String doubleList = "";
            for (Double aDouble : fuzzyTransition.getCostVector()) {
                if (! doubleList.equals("")){
                    doubleList += ", ";
                }
                doubleList += "\"" + aDouble.toString() + "\"";
            }

            transitionsNewJson += "{\"name\": \"" + fuzzyTransition.getFuzzyTransitionName()
                    + "\", \"description\" : \"" + fuzzyTransition.getFuzzyTransitionDescription()
                    + "\", \"costVector\" : [" + doubleList + "]" +
                    ", \"from\": \"" + fuzzyTransition.getFromState().getFuzzyStateName()
                    + "\", \"to\": \"" + fuzzyTransition.getToState().getFuzzyStateName()
                    + "\"}";
        }
        return "[" + transitionsNewJson + "]";
    }

    public static String generateStatesJsonForJavascriptGui(FuzzyAutomaton fuzzyAutomatonToLoad) {

        String statesNewJson = "";
        for (FuzzyState fuzzyState : fuzzyAutomatonToLoad.getFuzzyStates()) {
            if (! statesNewJson.equals("")){
                statesNewJson += ", ";
            }
            statesNewJson += "{\"name\": \"" + fuzzyState.getFuzzyStateName()
                    + "\", \"description\": \"" + fuzzyState.getFuzzyStateDescription()
                    + "\", \"type\": \"" + fuzzyState.getFuzzyStateType().toString().toLowerCase()
                    + "\", \"set\": \"" + (fuzzyState.getFuzzySet() != null ? fuzzyState.getFuzzySet().getFuzzySetName() : "") + "\"}";
        }
        return "[" + statesNewJson + "]";
    }

}
