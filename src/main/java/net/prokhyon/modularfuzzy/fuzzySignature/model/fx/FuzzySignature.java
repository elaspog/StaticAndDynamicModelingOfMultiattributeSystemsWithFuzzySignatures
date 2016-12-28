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
    private final StringProperty fuzzySignatureDescription;
    private FuzzyNode rootNodeOfTheTree;

    /*
     * Constructors
     */

    public FuzzySignature(String uuid, String signatureName, FuzzyNode rootNodeOfTheTree, String fuzzySignatureDescription){

        super();
        this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
        this.fuzzySignatureName = new SimpleStringProperty(signatureName);
        this.fuzzySignatureDescription = new SimpleStringProperty(fuzzySignatureDescription);
        this.rootNodeOfTheTree = rootNodeOfTheTree.deepCopy();
    }

    public FuzzySignature(FuzzySignature otherFuzzySignature){
        this(otherFuzzySignature.getUUID(), otherFuzzySignature.getFuzzySignatureName(),
                otherFuzzySignature.getRootNodeOfTheTree(), otherFuzzySignature.getFuzzySignatureDescription());
    }

    /*
     * Implemented interfaces
     */

    @Override
    public String getUUID() {
        return uuid.get();
    }

    @Override
    public String getListElementIdentifier() {

        return fuzzySignatureName.get() + " : (" + getChildrenRecursivelyDFS(rootNodeOfTheTree).size() + ")";
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature convert2DescriptorModel() {
        return null;
    }

    /*
     * Methods
     */

    public FuzzySignature deepCopy() {
        return new FuzzySignature(this);
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

    public String getFuzzySignatureDescription() {
        return fuzzySignatureDescription.get();
    }

    public StringProperty fuzzySignatureDescriptionProperty() {
        return fuzzySignatureDescription;
    }

    public void setFuzzySignatureDescription(String fuzzySignatureDescription) {
        this.fuzzySignatureDescription.set(fuzzySignatureDescription);
    }

    List<FuzzyNode> getAllNodesOfTree(){

        return getChildrenRecursivelyDFS(rootNodeOfTheTree);
    }

    private List<FuzzyNode> getChildrenRecursivelyDFS(FuzzyNode rootNodeOfTheTree) {

        List<FuzzyNode> retList = new ArrayList<>();
        retList.add(rootNodeOfTheTree);
        for (FuzzyNode childNode : rootNodeOfTheTree.getChildNodes()) {
            retList.addAll(getChildrenRecursivelyDFS(childNode));
        }
        return retList;
    }

}
