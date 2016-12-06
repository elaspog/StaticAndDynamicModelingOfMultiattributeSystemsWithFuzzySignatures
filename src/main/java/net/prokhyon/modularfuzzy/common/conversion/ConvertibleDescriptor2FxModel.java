package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.fxModel.FuzzyFxBase;

public interface ConvertibleDescriptor2FxModel<DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {

    FX_MODEL convert2FxModel();
}
