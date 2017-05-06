package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxElementBase;

public interface ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {


    interface Internal <DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
            extends ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL, FX_MODEL> {

        DESCRIPTOR_MODEL convert2DescriptorModel();
    }

    interface External <DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
            extends ConvertibleFxModel2Descriptor<DESCRIPTOR_MODEL, FX_MODEL> {

        DESCRIPTOR_MODEL convert2DescriptorModel(FX_MODEL model);
    }

}
