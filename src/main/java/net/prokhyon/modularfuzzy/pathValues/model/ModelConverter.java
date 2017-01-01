package net.prokhyon.modularfuzzy.pathValues.model;

import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.pathValues.model.fx.FuzzyModelValues;

public class ModelConverter
        implements ConvertibleDescriptor2FxModel.External<net.prokhyon.modularfuzzy.pathValues.model.descriptor.FuzzyModelValues, net.prokhyon.modularfuzzy.pathValues.model.fx.FuzzyModelValues> {

    @Override
    public FuzzyModelValues convert2FxModel(net.prokhyon.modularfuzzy.pathValues.model.descriptor.FuzzyModelValues fuzzyModelValues) {
        return null;
    }

}
