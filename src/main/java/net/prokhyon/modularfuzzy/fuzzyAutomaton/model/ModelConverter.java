package net.prokhyon.modularfuzzy.fuzzyAutomaton.model;

import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;

public class ModelConverter
        implements ConvertibleDescriptor2FxModel.External<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    @Override
    public FuzzyAutomaton convert2FxModel(net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton fuzzyAutomaton) {
        return null;
    }

}
