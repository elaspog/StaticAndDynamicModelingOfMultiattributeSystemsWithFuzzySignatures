package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import javafx.beans.property.*;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;

import java.util.ArrayList;
import java.util.List;

public class FuzzySignature extends WorkspaceFxRootElementBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature, net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzySignature>{

    /*
     * Properties
     */

    private final StringProperty uuid;
    private final StringProperty fuzzySignatureName;
    private final StringProperty fuzzySignatureDescription;
    private final ObjectProperty<Integer> costVectorDimensionObj;
    private final IntegerProperty costVectorDimensionInt;
    private FuzzyNode rootNodeOfTheTree;

    /*
     * Constructors
     */

    public FuzzySignature(String uuid, String signatureName, FuzzyNode rootNodeOfTheTree, String fuzzySignatureDescription, Integer costVectorDimension){

        super();
        this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
        this.fuzzySignatureName = new SimpleStringProperty(signatureName);
        this.fuzzySignatureDescription = new SimpleStringProperty(fuzzySignatureDescription);
        this.costVectorDimensionObj = new SimpleObjectProperty(costVectorDimension);
        this.costVectorDimensionInt = IntegerProperty.integerProperty(costVectorDimensionObj);
        this.rootNodeOfTheTree = rootNodeOfTheTree.deepCopy();
    }

    public FuzzySignature(FuzzySignature otherFuzzySignature){
        this(otherFuzzySignature.getUuid(), otherFuzzySignature.getFuzzySignatureName(),
                otherFuzzySignature.getRootNodeOfTheTree(), otherFuzzySignature.getFuzzySignatureDescription(),
                otherFuzzySignature.getCostVectorDimensionObj());
    }

    /*
     * Implemented interfaces
     */

    @Override
    public String getListElementIdentifier() {

        return fuzzySignatureName.get() + " : (" + getChildrenRecursivelyDFS(rootNodeOfTheTree).size() + ")";
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature convert2DescriptorModel() {

        String uuid = getUuid();
        String fuzzySignatureName = getFuzzySignatureName();
        String fuzzySignatureDescription = getFuzzySignatureDescription();
        FuzzyNode rootNodeOfTheTree = getRootNodeOfTheTree();

        net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode fuzzyNode = rootNodeOfTheTree.convert2DescriptorModel();

        net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature fuzzySignature
                = new net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzySignature(uuid, fuzzySignatureName, fuzzySignatureDescription, fuzzyNode, this.getCostVectorDimensionObj());

        return fuzzySignature;
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

    @Override
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

    public Integer getCostVectorDimensionObj() {
        return costVectorDimensionObj.get();
    }

    public ObjectProperty<Integer> costVectorDimensionObjProperty() {
        return costVectorDimensionObj;
    }

    public void setCostVectorDimensionObj(Integer costVectorDimensionObj) {
        this.costVectorDimensionObj.set(costVectorDimensionObj);
    }

    public List<FuzzyNode> getAllNodesOfTree(){

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
