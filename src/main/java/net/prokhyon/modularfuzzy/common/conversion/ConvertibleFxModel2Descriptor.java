package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

public interface ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {


    interface Internal <DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
            extends ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL, FX_MODEL> {

        DESCRIPTOR_MODEL convert2DescriptorModel();
    }

    interface External <DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
            extends ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL, FX_MODEL> {

        DESCRIPTOR_MODEL convert2DescriptorModel(FX_MODEL model);
    }

}
