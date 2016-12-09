package net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

@XStreamAlias("fuzzysignature")
public class FuzzySignature extends FuzzyDescriptorRootBase {

	@XStreamAlias("reuseability")
	@XStreamAsAttribute
	private FuzzyTreeTypeEnum treeType;

	@XStreamAlias("rootnodeid")
	@XStreamAsAttribute
	private String rootNodeId;

	@XStreamImplicit
	private List<FuzzyNode> nodeList;

	public FuzzySignature(String uuid, String typeId, String typeDescription, FuzzyTreeTypeEnum treeType, String rootNodeId,
			List<FuzzyNode> nodeList) {
		super(uuid, typeId, typeDescription);
		this.treeType = treeType;
		this.rootNodeId = rootNodeId;
		this.nodeList = nodeList;
	}

	public FuzzyTreeTypeEnum getTreeType() {
		return treeType;
	}

	public void setTreeType(FuzzyTreeTypeEnum treeType) {
		this.treeType = treeType;
	}

	public String getRootNodeId() {
		return rootNodeId;
	}

	public void setRootNodeId(String rootNodeId) {
		this.rootNodeId = rootNodeId;
	}

	public List<FuzzyNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<FuzzyNode> nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public String toString() {
		return "FuzzySignature [treeType=" + treeType + ", rootNodeId=" + rootNodeId + ", nodeList=" + nodeList
				+ ", typeId=" + typeId + "]";
	}

}
