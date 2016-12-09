package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

@XStreamAlias("fuzzysetsystem")
public class FuzzySetSystem extends FuzzyDescriptorRootBase {

	@XStreamAlias("type")
	@XStreamAsAttribute
	private FuzzySetSystemTypeEnum type;

	@XStreamImplicit
	@XStreamAlias("fuzzysets")
	private List<FuzzySetBase> sets;

	public FuzzySetSystem(String uuid, String typeId, String typeDescription, FuzzySetSystemTypeEnum type, List<FuzzySetBase> sets) {
		super(uuid, typeId, typeDescription);
		this.type = type;
		this.sets = sets;
	}

	public FuzzySetSystemTypeEnum getType() {
		return type;
	}

	public void setType(FuzzySetSystemTypeEnum type) {
		this.type = type;
	}

	public List<FuzzySetBase> getSets() {
		return sets;
	}

	public void setSets(List<FuzzySetBase> sets) {
		this.sets = sets;
	}

	@Override
	public String toString() {
		return "FuzzySetSystem [type=" + type + ", sets=" + sets + ", typeId=" + typeId + "]";
	}

}
