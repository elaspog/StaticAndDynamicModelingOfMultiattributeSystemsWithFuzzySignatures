package net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorModelBase;

@XStreamAlias("node")
public class FuzzyNode extends FuzzyDescriptorModelBase {

	@XStreamImplicit
	private List<ChildNodeInfo> children;

	@XStreamImplicit
	private List<SubTreeInfo> subTrees;

	@XStreamAlias("aggregatoroperator")
	@XStreamAsAttribute
	private String aggregatorOperator;

	@XStreamAlias("fuzzysettypeid")
	@XStreamAsAttribute
	private String fuzzySetTypeId;

	public FuzzyNode(String id, String label, String description, List<ChildNodeInfo> children,
			List<SubTreeInfo> subTrees, String aggregatorOperator, String fuzzySetTypeId) {
		super(id, label, description);
		this.children = children;
		this.subTrees = subTrees;
		this.aggregatorOperator = aggregatorOperator;
		this.fuzzySetTypeId = fuzzySetTypeId;
	}

	public List<ChildNodeInfo> getChildren() {
		return children;
	}

	public void setChildren(List<ChildNodeInfo> children) {
		this.children = children;
	}

	public List<SubTreeInfo> getSubTrees() {
		return subTrees;
	}

	public void setSubTrees(List<SubTreeInfo> subTrees) {
		this.subTrees = subTrees;
	}

	public String getAggregatorOperator() {
		return aggregatorOperator;
	}

	public void setAggregatorOperator(String aggregatorOperator) {
		this.aggregatorOperator = aggregatorOperator;
	}

	public String getFuzzySetTypeId() {
		return fuzzySetTypeId;
	}

	public void setFuzzySetTypeId(String fuzzySetTypeId) {
		this.fuzzySetTypeId = fuzzySetTypeId;
	}

	@Override
	public String toString() {
		return "FuzzyNode [children=" + children + ", aggregatorOperator=" + aggregatorOperator + ", fuzzySetTypeId="
				+ fuzzySetTypeId + ", id=" + id + ", label=" + label + ", description=" + description + "]";
	}

}
