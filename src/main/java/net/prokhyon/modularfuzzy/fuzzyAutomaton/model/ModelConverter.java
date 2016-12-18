package net.prokhyon.modularfuzzy.fuzzyAutomaton.model;

import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter
        implements ConvertibleDescriptor2FxModel.External<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    @Override
    public FuzzyAutomaton convert2FxModel(net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton fuzzyAutomaton) {

        final String description = fuzzyAutomaton.getDescription();
        final String typeName = fuzzyAutomaton.getTypeName();
        final String uuid = fuzzyAutomaton.getUUID();
        final Integer costVectorDimension = fuzzyAutomaton.getCostVectorDimension();
        final List<FuzzyState> states = fuzzyAutomaton.getStates();
        final List<FuzzyTransition> transitions = fuzzyAutomaton.getTransitions();

        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState> fxStates = new ArrayList<>();
        for (FuzzyState state : states) {
            final String stateLabel = state.getLabel();
            final String stateDescription = state.getDescription();
            // TODO fuzzySet
            final FuzzyStateTypeEnum stateType = state.getType();
            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState fxState
                    = new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState(stateLabel, stateDescription, null, stateType);
            fxStates.add(fxState);
        }

        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition> fxTransitions = new ArrayList<>();
        for (FuzzyTransition transition : transitions) {
            final String transitionLabel = transition.getLabel();
            final String transitionDescription = transition.getDescription();
            final String transitionFromState = transition.getFromState();
            final String transitionToState = transition.getToState();
            // TODO costVector
            // TODO transitionFromState
            // TODO transitionToState
            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition fxTransition
                    = new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition(transitionLabel, transitionDescription, null, null, null);
            fxTransitions.add(fxTransition);
        }

        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton(uuid, typeName, description, fxStates, fxTransitions, null, costVectorDimension);
    }

}
