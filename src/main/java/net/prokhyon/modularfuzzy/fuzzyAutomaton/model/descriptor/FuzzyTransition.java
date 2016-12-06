package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorModelBase;

@XStreamAlias("transition")
public class FuzzyTransition extends FuzzyDescriptorModelBase {

	@XStreamAlias("startstateid")
	@XStreamAsAttribute
	private String startStateId;

	@XStreamAlias("endstateid")
	@XStreamAsAttribute
	private String endStateId;

	public FuzzyTransition(String id, String label, String description, String startStateId, String endStateId) {
		super(id, label, description);
		this.startStateId = startStateId;
		this.endStateId = endStateId;
	}

	public String getStartStateId() {
		return startStateId;
	}

	public void setStartStateId(String startStateId) {
		this.startStateId = startStateId;
	}

	public String getEndStateId() {
		return endStateId;
	}

	public void setEndStateId(String endStateId) {
		this.endStateId = endStateId;
	}

	@Override
	public String toString() {
		return "FuzzyTransition [startStateId=" + startStateId + ", endStateId=" + endStateId + "]";
	}

}
