package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

import java.util.ArrayList;
import java.util.List;

public class FuzzyNode extends FuzzyFxBase {

    private String name;

    private String description;

    private FuzzyNode parentNode;

    private List<FuzzyNode> childNodes;

    public FuzzyNode(String name) {
        this(name, null, null);
    }

    public FuzzyNode(String name, List<FuzzyNode> childNodes) {
        this(name, null, childNodes);
    }

    public FuzzyNode(String name, FuzzyNode parentNode) {
        this(name, parentNode, null);
    }

    public FuzzyNode(String name, FuzzyNode parentNode, List<FuzzyNode> childNodes) {
        this.name = name;
        this.parentNode = parentNode;
        if (childNodes != null)
            this.childNodes = childNodes;
        else
            this.childNodes = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
