package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

import java.util.ArrayList;
import java.util.List;

public class FuzzySignature extends WorkspaceElement
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature, net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature>{

    /*
     * Properties
     */

    private final StringProperty uuid;
    private final StringProperty fuzzySignatureName;
    private FuzzyNode rootNodeOfTheTree;
    private List<FuzzyNode> allNodesOfTheTree;

    /*
     * Constructors
     */

    public FuzzySignature(String uuid, String signatureName, FuzzyNode rootNodeOfTheTree){

        this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
        this.fuzzySignatureName = new SimpleStringProperty(signatureName);
        this.rootNodeOfTheTree = rootNodeOfTheTree;
        this.allNodesOfTheTree = new ArrayList<>();
        allNodesOfTheTree.add(rootNodeOfTheTree);
    }

    public FuzzySignature(FuzzySignature otherFuzzySignature){
        this(otherFuzzySignature.getUUID(), otherFuzzySignature.getFuzzySignatureName(), otherFuzzySignature.getRootNodeOfTheTree());
    }

    /*
     * Implemented interfaces
     */

    @Override
    public String getUUID() {
        return null;
    }

    @Override
    public String getListElementIdentifier() {
        return "_";
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature convert2DescriptorModel() {
        return null;
    }

    /*
     * Getters and Setters
     */

    public String getUuid() {
        return uuid.get();
    }

    public StringProperty uuidProperty() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid.set(uuid);
    }

    public String getFuzzySignatureName() {
        return fuzzySignatureName.get();
    }

    public StringProperty fuzzySignatureNameProperty() {
        return fuzzySignatureName;
    }

    public void setFuzzySignatureName(String fuzzySignatureName) {
        this.fuzzySignatureName.set(fuzzySignatureName);
    }

    public FuzzyNode getRootNodeOfTheTree() {
        return rootNodeOfTheTree;
    }

    public void setRootNodeOfTheTree(FuzzyNode rootNodeOfTheTree) {
        this.rootNodeOfTheTree = rootNodeOfTheTree;
    }

    public List<FuzzyNode> getAllNodesOfTheTree() {
        return allNodesOfTheTree;
    }

    public void setAllNodesOfTheTree(List<FuzzyNode> allNodesOfTheTree) {
        this.allNodesOfTheTree = allNodesOfTheTree;
    }
}
