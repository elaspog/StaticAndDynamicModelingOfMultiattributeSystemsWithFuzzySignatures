package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorModelBase;

@XStreamAlias("FuzzyState")
public class FuzzyState extends DescriptorModelBase {

	@XStreamAlias("StateType")
	@XStreamAsAttribute
	private FuzzyStateTypeEnum type;

	@XStreamAlias("FuzzySetName")
	@XStreamAsAttribute
	private String fuzzySetName;

	public FuzzyState(String label, String description, FuzzyStateTypeEnum type, String fuzzySetName) {
		super(label, description);
		this.type = type;
		this.fuzzySetName = fuzzySetName;
	}

	public FuzzyStateTypeEnum getType() {
		return type;
	}

	public String getFuzzySetName() {
		return fuzzySetName;
	}

	@Override
	public String toString() {
		return "FuzzyState [type=" + type + ", fuzzySetName=" + fuzzySetName + "]";
	}

}
