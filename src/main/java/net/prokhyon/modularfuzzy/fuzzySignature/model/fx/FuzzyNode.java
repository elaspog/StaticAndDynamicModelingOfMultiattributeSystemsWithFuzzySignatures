package net.prokhyon.modularfuzzy.fuzzySignature.model.fx;

import net.prokhyon.modularfuzzy.common.modelFx.FuzzyFxBase;

public class FuzzyNode extends FuzzyFxBase {

    private String name;

    private FuzzyNode fuzzyNode;

    public FuzzyNode(String name, FuzzyNode fuzzyNode) {
        this.name = name;
        this.fuzzyNode = fuzzyNode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FuzzyNode getFuzzyNode() {
        return fuzzyNode;
    }

    public void setFuzzyNode(FuzzyNode fuzzyNode) {
        this.fuzzyNode = fuzzyNode;
    }

}
