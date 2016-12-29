package net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

@XStreamAlias("FuzzySignature")
public class FuzzySignature extends FuzzyDescriptorRootBase {

    @XStreamAlias("FuzzyNode")
    private FuzzyNode rootNode;

    public FuzzySignature(String uuid, String fuzzySignatureName, String fuzzySignatureDescription, FuzzyNode fuzzyRootNode) {
        super(uuid, fuzzySignatureName, fuzzySignatureDescription);
        this.rootNode = fuzzyRootNode;
    }

    public FuzzyNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(FuzzyNode rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public String toString() {
        return "FuzzySignature{" +
                "rootNode=" + rootNode +
                '}';
    }
}
