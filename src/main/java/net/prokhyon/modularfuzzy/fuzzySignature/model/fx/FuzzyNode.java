package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

import java.util.ArrayList;
import java.util.List;

public class FuzzyNode extends FuzzyFxBase {

    private final StringProperty fuzzyNodeName;
    private final StringProperty fuzzyNodeDescription;

    private String description;

    private FuzzyNode parentNode;

    private List<FuzzyNode> childNodes;

    public FuzzyNode(String name) {
        this(name, null, null, null);
    }

    public FuzzyNode(String name, List<FuzzyNode> childNodes) {
        this(name, null, childNodes, null);
    }

    public FuzzyNode(String name, FuzzyNode parentNode) {
        this(name, parentNode, null, null);
    }

    public FuzzyNode(String name, FuzzyNode parentNode, List<FuzzyNode> childNodes, String description) {

        this.fuzzyNodeName = new SimpleStringProperty(name);
        this.fuzzyNodeDescription = new SimpleStringProperty(description);
        this.parentNode = parentNode;
        if (childNodes != null)
            this.childNodes = childNodes;
        else
            this.childNodes = new ArrayList<>();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
