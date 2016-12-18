package net.prokhyon.modularfuzzy.fuzzyAutomaton.model;

import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter
        implements ConvertibleDescriptor2FxModel.External<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    final CommonServices commonServices = new ServiceFactory().getCommonServices();

    @Override
    public FuzzyAutomaton convert2FxModel(net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton fuzzyAutomaton) {

        final String description = fuzzyAutomaton.getDescription();
        final String typeName = fuzzyAutomaton.getTypeName();
        final String uuid = fuzzyAutomaton.getUUID();
        final String referencedFuzzySetSystemUUID = fuzzyAutomaton.getReferencedFuzzySetSystemUUID();
        final Integer costVectorDimension = fuzzyAutomaton.getCostVectorDimension();
        final List<FuzzyState> states = fuzzyAutomaton.getStates();
        final List<FuzzyTransition> transitions = fuzzyAutomaton.getTransitions();

        final FuzzySetSystem fuzzySetSystem = commonServices.resolveModelByUUID(referencedFuzzySetSystemUUID);

        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState> fxStates = new ArrayList<>();
        for (FuzzyState state : states) {
            final String stateLabel = state.getLabel();
            final String stateDescription = state.getDescription();
            final FuzzyStateTypeEnum stateType = state.getType();

            final String fuzzySetNameLabel = state.getFuzzySetName();

            FuzzySet fs = null;
            for (FuzzySet fuzzySet : fuzzySetSystem.getFuzzySets()) {
                if (fuzzySet.getFuzzySetName().equals(fuzzySetNameLabel)) {
                    fs = fuzzySet;
                    break;
                }
            }

            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState fxState
                    = new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState(stateLabel, stateDescription, fs, stateType);
            fxStates.add(fxState);
        }

        List<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition> fxTransitions = new ArrayList<>();
        for (FuzzyTransition transition : transitions) {
            final String transitionLabel = transition.getLabel();
            final String transitionDescription = transition.getDescription();
            final String transitionFromStateLabel = transition.getFromState();
            final String transitionToStateLabel = transition.getToState();

            // TODO costVector

            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState transitionFromState = null;
            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState transitionToState = null;

            for (net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyState fxState : fxStates){

                if (fxState.getFuzzyStateName().equals(transitionFromStateLabel))
                    transitionFromState = fxState;

                if (fxState.getFuzzyStateName().equals(transitionToStateLabel))
                    transitionToState = fxState;

                if (transitionFromState != null && transitionToState != null)
                    break;
            }

            net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition fxTransition
                    = new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyTransition(transitionLabel, transitionDescription, null, transitionFromState, transitionToState);
            fxTransitions.add(fxTransition);
        }

        return new net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton(uuid, typeName, description, fxStates, fxTransitions, fuzzySetSystem, costVectorDimension);
    }

}
