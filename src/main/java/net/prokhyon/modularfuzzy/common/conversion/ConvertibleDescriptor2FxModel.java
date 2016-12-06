package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

public interface ConvertibleDescriptor2FxModel<DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {


    interface Internal <DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
            extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {

        FX_MODEL convert2FxModel();
    }

    interface External <DESCRIPTOR_MODEL extends FuzzyDescriptorBase, FX_MODEL extends FuzzyFxBase>
            extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {

        FX_MODEL convert2FxModel(DESCRIPTOR_MODEL model);
    }

}
