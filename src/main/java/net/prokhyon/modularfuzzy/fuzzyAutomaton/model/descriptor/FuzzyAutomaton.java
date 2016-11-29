package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.descriptor.FuzzyDescriptorRootBase;

@XStreamAlias("fuzzyautomaton")
public class FuzzyAutomaton extends FuzzyDescriptorRootBase {

	@XStreamAlias("fuzzytypeid")
	@XStreamAsAttribute
	private String fuzzyTypeId;

	@XStreamImplicit
	private List<FuzzyState> states;

	@XStreamImplicit
	private List<FuzzyTransition> transitions;

	public FuzzyAutomaton(String typeId, String typeDescription, String fuzzyTypeId, List<FuzzyState> states,
			List<FuzzyTransition> transitions) {
		super(typeId, typeDescription);
		this.fuzzyTypeId = fuzzyTypeId;
		this.states = states;
		this.transitions = transitions;
	}

	public String getFuzzytypeid() {
		return fuzzyTypeId;
	}

	public void setFuzzytypeid(String fuzzytypeid) {
		this.fuzzyTypeId = fuzzytypeid;
	}

	public List<FuzzyState> getStates() {
		return states;
	}

	public void setStates(List<FuzzyState> states) {
		this.states = states;
	}

	public List<FuzzyTransition> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<FuzzyTransition> transitions) {
		this.transitions = transitions;
	}

	@Override
	public String toString() {
		return "FuzzyAutomaton [fuzzyTypeId=" + fuzzyTypeId + ", states=" + states + ", transitions=" + transitions
				+ "]";
	}

}
