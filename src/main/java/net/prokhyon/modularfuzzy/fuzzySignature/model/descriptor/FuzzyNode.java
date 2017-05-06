package net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorModelBase;

import java.util.List;

@XStreamAlias("FuzzyNode")
public class FuzzyNode extends DescriptorModelBase {

    @XStreamImplicit
    private List<FuzzyNode> childNodes;

    @XStreamAlias("AggregationOperator")
    @XStreamAsAttribute
    private AggregationType aggregationType;

    @XStreamAlias("ReferencedFuzzyAutomatonUUID")
    @XStreamAsAttribute
    private String fuzzyAutomatonUUID;

    public FuzzyNode(String label, String description, List<FuzzyNode> childNodes, AggregationType aggregationType, String fuzzyAutomatonUUID) {
        super(label, description);
        this.childNodes = childNodes;
        this.aggregationType = aggregationType;
        this.fuzzyAutomatonUUID = fuzzyAutomatonUUID;
    }

    public List<FuzzyNode> getChildNodes() {
        return childNodes;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public String getFuzzyAutomatonUUID() {
        return fuzzyAutomatonUUID;
    }

    public void setChildNodes(List<FuzzyNode> childNodes) {
        this.childNodes = childNodes;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public void setFuzzyAutomatonUUID(String fuzzyAutomatonUUID) {
        this.fuzzyAutomatonUUID = fuzzyAutomatonUUID;
    }

    @Override
    public String toString() {
        return "FuzzyNode{" +
                "childNodes=" + childNodes +
                ", aggregationType=" + aggregationType +
                ", fuzzyAutomatonUUID='" + fuzzyAutomatonUUID + '\'' +
                '}';
    }

}
