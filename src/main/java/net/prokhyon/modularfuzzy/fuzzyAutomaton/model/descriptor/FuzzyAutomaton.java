package net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

@XStreamAlias("FuzzyAutomaton")
public class FuzzyAutomaton extends FuzzyDescriptorRootBase {

	@XStreamAlias("ReferencedFuzzySetSystemUUID")
	@XStreamAsAttribute
	private String referencedFuzzySetSystemUUID;

	@XStreamAlias("CostVectorDimension")
	@XStreamAsAttribute
	private Integer costVectorDimension;

	@XStreamImplicit
	private List<FuzzyState> states;

	@XStreamImplicit
	private List<FuzzyTransition> transitions;

	public FuzzyAutomaton(String uuid, String typeName, String typeDescription, String referencedFuzzySetSystemUUID, List<FuzzyState> states,
			List<FuzzyTransition> transitions, Integer costVectorDimension) {
		super(uuid, typeName, typeDescription);
		this.referencedFuzzySetSystemUUID = referencedFuzzySetSystemUUID;
		this.states = states;
		this.transitions = transitions;
		this.costVectorDimension = costVectorDimension;
	}

	public Integer getCostVectorDimension() {
		return costVectorDimension;
	}

	public List<FuzzyState> getStates() {
		return states;
	}

	public List<FuzzyTransition> getTransitions() {
		return transitions;
	}

	public String getReferencedFuzzySetSystemUUID() {
		return referencedFuzzySetSystemUUID;
	}

	@Override
	public String toString() {
		//return "FuzzyAutomaton [fuzzyTypeId=" + fuzzyTypeId + ", states=" + states + ", transitions=" + transitions + "]";
		return "FuzzyAutomaton [states=" + states + ", transitions=" + transitions + "]";
	}

}
