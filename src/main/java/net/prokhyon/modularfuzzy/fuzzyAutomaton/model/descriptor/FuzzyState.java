package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorModelBase;

@XStreamAlias("state")
public class FuzzyState extends FuzzyDescriptorModelBase {

	@XStreamAlias("type")
	@XStreamAsAttribute
	private FuzzyStateTypeEnum type;

	@XStreamAlias("fuzzyvalue")
	@XStreamAsAttribute
	private String fuzzyValue;

	public FuzzyState(String id, String label, String description, FuzzyStateTypeEnum type, String fuzzyValue) {
		super(id, label, description);
		this.type = type;
		this.fuzzyValue = fuzzyValue;
	}

	public FuzzyStateTypeEnum getType() {
		return type;
	}

	public void setType(FuzzyStateTypeEnum type) {
		this.type = type;
	}

	public String getFuzzyvalue() {
		return fuzzyValue;
	}

	public void setFuzzyvalue(String fuzzyvalue) {
		this.fuzzyValue = fuzzyvalue;
	}

	@Override
	public String toString() {
		return "FuzzyState [type=" + type + ", fuzzyValue=" + fuzzyValue + "]";
	}

}
