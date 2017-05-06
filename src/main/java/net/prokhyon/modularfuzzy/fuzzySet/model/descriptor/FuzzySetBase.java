package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorModelBase;

public abstract class FuzzySetBase extends DescriptorModelBase {

	@XStreamAsAttribute
	private FuzzySetTypeEnum type;

	@XStreamImplicit
	private List<FuzzyPointBase> points;

	public FuzzySetBase(String label, String description, FuzzySetTypeEnum type, List<FuzzyPointBase> points) {
		super(label, description);
		this.type = type;
		this.points = points;
	}

	public FuzzySetTypeEnum getType() {
		return type;
	}

	public List<FuzzyPointBase> getPoints() {
		return points;
	}

	@Override
	public String toString() {
		return "FuzzySetBase [type=" + type + ", points=" + points + ", id=" + id
				+ ", description=" + description + "]";
	}

}
