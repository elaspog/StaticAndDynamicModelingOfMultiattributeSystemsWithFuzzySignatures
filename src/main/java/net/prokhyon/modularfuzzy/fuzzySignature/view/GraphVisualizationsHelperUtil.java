package net.prokhyon.modularfuzzy.fuzzySignature.view;

import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode;
import net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature;

import java.util.List;

public class GraphVisualizationsHelperUtil {

    public static String generateSignatureJsonForJavascriptGui(FuzzySignature fuzzySignatureToLoad){

        final FuzzyNode rootNodeOfTheTree = fuzzySignatureToLoad.getRootNodeOfTheTree();
        return getJsonContentOfChildren(rootNodeOfTheTree);
    }

    private static String getJsonContentOfChildren(FuzzyNode fuzzyNode){

        final String fuzzyNodeName = fuzzyNode.getFuzzyNodeName();
        final List<FuzzyNode> childNodes = fuzzyNode.getChildNodes();

        String nodeStructure = "{\"name\": \"" + fuzzyNodeName + "\"";
        String childrenArray = "";
        if(childNodes != null && childNodes.size() > 0){
            for (FuzzyNode childNode : childNodes) {
                if (! childrenArray.equals("")){
                    childrenArray += ", ";
                }
                childrenArray += getJsonContentOfChildren(childNode);
            }
            childrenArray = ",\"children\":[" + childrenArray + "]";
        }
        nodeStructure += childrenArray + "}";
        return nodeStructure;
    }

}
