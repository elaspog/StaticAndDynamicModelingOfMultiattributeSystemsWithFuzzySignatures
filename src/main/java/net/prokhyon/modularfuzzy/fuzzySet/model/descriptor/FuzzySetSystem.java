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

	public FuzzySetSystem(String uuid, String typeName, String typeDescription, FuzzySetSystemTypeEnum type, List<FuzzySetBase> sets) {
		super(uuid, typeName, typeDescription);
		this.type = type;
		this.sets = sets;
	}

	public FuzzySetSystemTypeEnum getType() {
		return type;
	}

	public List<FuzzySetBase> getSets() {
		return sets;
	}

	@Override
	public String toString() {
		return "FuzzySetSystem [type=" + type + ", sets=" + sets + ", typeName=" + typeName + "]";
	}

}
