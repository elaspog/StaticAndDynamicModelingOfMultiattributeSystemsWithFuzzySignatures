package net.prokhyon.modularfuzzy.common.descriptor;

public interface ConvertableDescriptor2FxModel<DESCRIPTOR_MODEL, FX_MODEL> {

    FX_MODEL convert2FxModel(DESCRIPTOR_MODEL model);
}
