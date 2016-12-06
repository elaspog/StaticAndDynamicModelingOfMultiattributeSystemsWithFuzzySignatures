package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.fxModel.FuzzyFxBase;

public interface ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {

    DESCRIPTOR_MODEL convert2DescriptorModel();
}
