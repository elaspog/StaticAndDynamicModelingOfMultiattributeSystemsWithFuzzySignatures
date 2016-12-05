package net.prokhyon.modularfuzzy.fuzzySet.model;

import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;

import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public static net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem fxmodelToDescriptor(net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem model){

        return model.convert2DescriptorModel();
    }

    public static net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem descriptorToFXmodel(net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem descriptor){

        final String fuzzySystemUUID = descriptor.getUUID();
        final String fuzzySystemName = descriptor.getTypeid();
        final String fuzzySystemDescription =  descriptor.getDescription();
        final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum fuzzySystemType =  descriptor.getType();
        final List<net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetBase> sets = descriptor.getSets();

        List<net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet> fuzzySets = new ArrayList<>();
        for (net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetBase element : sets) {

            final String fuzySetName = element.getId();
            //TODO element.getLabel();
            final String fuzzySetDescription = element.getDescription();
            final net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTypeEnum fuzzySetType = element.getType();
            final List<net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.IFuzzyPoint> fuzzySetPoints = element.getPoints();

            List<net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetPoint> fuzzypoints = new ArrayList<>();
            for (net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.IFuzzyPoint point : fuzzySetPoints) {

                net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetPoint fsp = null;

                if (point instanceof net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointAbove) {
                    final float x = (float) ((net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointAbove) point).getxCoordinate();
                    fsp = new net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetPoint(x, 1);

                } else if (point instanceof net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBelow) {
                    final float x = (float) ((net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBelow) point).getxCoordinate();
                    fsp = new net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetPoint(x, 0);

                } else if (point instanceof net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom) {
                    final float x = (float) ((net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom) point).getxCoordinate();
                    final float y = (float) ((net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom) point).getyCoordinate();
                    fsp = new net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetPoint(x, y);
                }

                fuzzypoints.add(fsp);
            }

            net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet fuzzySet;
            fuzzySet = new net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet(fuzySetName, fuzzySetDescription, fuzzySetType, fuzzypoints);
            fuzzySets.add(fuzzySet);
        }

        return new net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySetSystem(fuzzySystemName, fuzzySystemDescription, fuzzySystemType, fuzzySets, fuzzySystemUUID);
    }

}
