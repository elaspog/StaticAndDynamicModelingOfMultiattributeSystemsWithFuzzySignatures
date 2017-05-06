package net.prokhyon.modularfuzzy.common.conversion;

import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorBase;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxElementBase;

public interface ConvertibleDescriptor2FxModel<DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
        extends IConversionBase<DESCRIPTOR_MODEL, FX_MODEL> {


    interface Internal <DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
            extends ConvertibleDescriptor2FxModel<DESCRIPTOR_MODEL, FX_MODEL> {

        FX_MODEL convert2FxModel();
    }

    interface External <DESCRIPTOR_MODEL extends DescriptorBase, FX_MODEL extends WorkspaceFxElementBase>
            extends ConvertibleDescriptor2FxModel<DESCRIPTOR_MODEL, FX_MODEL> {

        FX_MODEL convert2FxModel(DESCRIPTOR_MODEL model);
    }

}
