package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.fx.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.AggregationType;

import java.util.ArrayList;
import java.util.List;

public class FuzzyNode extends FuzzyFxBase
        implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode, net.prokhyon.modularfuzzy.fuzzySignature.model.fx.FuzzyNode>{

    private final StringProperty fuzzyNodeName;
    private final StringProperty fuzzyNodeDescription;

    private FuzzyNode parentNode;
    private List<FuzzyNode> childNodes;
    private final ObjectProperty<AggregationType> aggregationType;
    private final ObjectProperty<FuzzyAutomaton> fuzzyAutomaton;

    public FuzzyNode(String name, FuzzyNode parentNode, List<FuzzyNode> childNodes, String description, AggregationType aggregationType, FuzzyAutomaton fuzzyAutomaton) {

        super();
        this.fuzzyNodeName = new SimpleStringProperty(name);
        this.fuzzyNodeDescription = new SimpleStringProperty(description);
        this.aggregationType = new SimpleObjectProperty<>(aggregationType);
        this.fuzzyAutomaton = new  SimpleObjectProperty<>(fuzzyAutomaton);
        this.parentNode = parentNode;

        this.childNodes = new ArrayList<>();
        if (childNodes != null) {
            for (FuzzyNode childNode : childNodes) {
                this.childNodes.add(childNode.deepCopy());
            }
        }
    }

    public FuzzyNode(FuzzyNode otherFuzzyNode){

        this(otherFuzzyNode.getFuzzyNodeName(), otherFuzzyNode.getParentNode(), otherFuzzyNode.getChildNodes(),
                otherFuzzyNode.getFuzzyNodeDescription(), otherFuzzyNode.getAggregationType(),
                otherFuzzyNode.getFuzzyAutomaton());
    }

    public FuzzyNode deepCopy() {
        return new FuzzyNode(this);
    }

    public FuzzyNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(FuzzyNode parentNode) {
        this.parentNode = parentNode;
    }

    public List<FuzzyNode> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<FuzzyNode> childNodes) {
        this.childNodes = childNodes;
    }

    public String getFuzzyNodeName() {
        return fuzzyNodeName.get();
    }

    public StringProperty fuzzyNodeNameProperty() {
        return fuzzyNodeName;
    }

    public void setFuzzyNodeName(String fuzzyNodeName) {
        this.fuzzyNodeName.set(fuzzyNodeName);
    }

    public String getFuzzyNodeDescription() {
        return fuzzyNodeDescription.get();
    }

    public StringProperty fuzzyNodeDescriptionProperty() {
        return fuzzyNodeDescription;
    }

    public void setFuzzyNodeDescription(String fuzzyNodeDescription) {
        this.fuzzyNodeDescription.set(fuzzyNodeDescription);
    }

    public AggregationType getAggregationType() {
        return aggregationType.get();
    }

    public ObjectProperty<AggregationType> aggregationTypeProperty() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType.set(aggregationType);
    }

    public FuzzyAutomaton getFuzzyAutomaton() {
        return fuzzyAutomaton.get();
    }

    public ObjectProperty<FuzzyAutomaton> fuzzyAutomatonProperty() {
        return fuzzyAutomaton;
    }

    public void setFuzzyAutomaton(FuzzyAutomaton fuzzyAutomaton) {
        this.fuzzyAutomaton.set(fuzzyAutomaton);
    }

    @Override
    public net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode convert2DescriptorModel() {

        AggregationType aggregationType = getAggregationType();
        List<FuzzyNode> childNodes = getChildNodes();
        FuzzyAutomaton fuzzyAutomaton = getFuzzyAutomaton();
        String fuzzyNodeDescription = getFuzzyNodeDescription();
        String fuzzyNodeName = getFuzzyNodeName();
        //getParentNode();
        String automatonUuid = null;

        // Inner node
        List<net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode> convertedChildNodes = new ArrayList<>();
        for (FuzzyNode childNode : childNodes) {
            convertedChildNodes.add(childNode.convert2DescriptorModel());
        }

        // Leaf node
        if (childNodes.size() == 0)
            automatonUuid = fuzzyAutomaton.getUuid();

        return new net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.FuzzyNode(fuzzyNodeName, fuzzyNodeDescription, convertedChildNodes, aggregationType, automatonUuid);
    }

}
