package net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorRootBase;

@XStreamAlias("FuzzySignature")
public class FuzzySignature extends DescriptorRootBase {

    @XStreamAlias("FuzzyNode")
    private FuzzyNode rootNode;

    @XStreamAlias("CostVectorDimension")
    @XStreamAsAttribute
    private Integer costVectorDimension;

    public FuzzySignature(String uuid, String fuzzySignatureName, String fuzzySignatureDescription, FuzzyNode fuzzyRootNode, Integer costVectorDimension) {
        super(uuid, fuzzySignatureName, fuzzySignatureDescription);
        this.rootNode = fuzzyRootNode;
        this.costVectorDimension = costVectorDimension;
    }

    public FuzzyNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(FuzzyNode rootNode) {
        this.rootNode = rootNode;
    }

    public Integer getCostVectorDimension() {
        return costVectorDimension;
    }

    public void setCostVectorDimension(Integer costVectorDimension) {
        this.costVectorDimension = costVectorDimension;
    }

    @Override
    public String toString() {
        return "FuzzySignature{" +
                "rootNode=" + rootNode +
                '}';
    }
}
