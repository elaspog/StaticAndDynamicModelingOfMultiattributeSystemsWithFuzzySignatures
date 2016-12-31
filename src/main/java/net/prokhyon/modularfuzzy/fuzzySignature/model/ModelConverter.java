package net.prokhyon.modularfuzzy.fuzzySignature.model;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.common.CommonServices;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleDescriptor2FxModel;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.AggregationType;
import net.prokhyon.modularfuzzy.shell.services.ServiceFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModelConverter
        implements ConvertibleDescriptor2FxModel.External<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature, net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature>  {

    static CommonServices commonServices;
    static FuzzyAutomatonModuleDescriptor fuzzyAutomatonModuleDescriptor;

    @Override
    public net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature convert2FxModel(net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature fuzzySignature) {

        if (commonServices == null || fuzzyAutomatonModuleDescriptor == null) {

            commonServices = new ServiceFactory().getCommonServices();

            final Map<Class<? extends ModuleDescriptor>, ModuleDescriptor> pseudoModules = commonServices.getPseudoModules();
            for (Map.Entry<Class<? extends ModuleDescriptor>, ModuleDescriptor> classModuleDescriptorEntry : pseudoModules.entrySet()) {
                final Class<? extends ModuleDescriptor> key = classModuleDescriptorEntry.getKey();
                final ModuleDescriptor value = classModuleDescriptorEntry.getValue();
                if (key == FuzzyAutomatonModuleDescriptor.class) {
                    final FuzzyAutomatonModuleDescriptor famdv = (FuzzyAutomatonModuleDescriptor) value;
                    if (famdv.getViewName().equals("Automatons")) {
                        fuzzyAutomatonModuleDescriptor = famdv;
                    }
                }
            }
        }

        final String uuid = fuzzySignature.getUUID();
        final String typeName = fuzzySignature.getTypeName();
        final String description = fuzzySignature.getDescription();
        final Integer costVectorDimension = fuzzySignature.getCostVectorDimension();
        final net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode rootNodeDescriptor = fuzzySignature.getRootNode();

        return new net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature(uuid, typeName, convertToFx(rootNodeDescriptor, null), description, costVectorDimension);
    }

    private net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode convertToFx(
            net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode nodeDescriptor,
            net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode parent) {

        String id = nodeDescriptor.getId();
        String description = nodeDescriptor.getDescription();
        AggregationType aggregationType = nodeDescriptor.getAggregationType();
        String fuzzyAutomatonUUID = nodeDescriptor.getFuzzyAutomatonUUID();

        FuzzyAutomaton resolvedAutomaton = commonServices.resolveModelByUUID(fuzzyAutomatonUUID);

        net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode fuzzyNode
                = new net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode(id, parent, null, description, aggregationType, resolvedAutomaton);
        List<net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode> generatedChildren = generateChildren(nodeDescriptor.getChildNodes(), fuzzyNode);
        fuzzyNode.setChildNodes(generatedChildren);

        return fuzzyNode;
    }

    private List<net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode> generateChildren(
            List<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode> childNodes,
            net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode parent) {

        List<net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode> nodes = new ArrayList<>();
        if (childNodes != null)
            for (net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode childNode : childNodes)
                nodes.add(convertToFx(childNode, parent));
        return nodes;
    }

}
