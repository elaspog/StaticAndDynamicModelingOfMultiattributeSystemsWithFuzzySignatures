package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx;

import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

public class FuzzyTransition extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton, net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton> {

    @Override
    public net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton convert2DescriptorModel() {
        return null;
    }

}
