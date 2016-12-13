package net.prokhyon.modularfuzzy.common.modelDescriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class FuzzyDescriptorModelBase extends FuzzyDescriptorBase {

	@XStreamAlias("label")
	@XStreamAsAttribute
	protected String label;

	@XStreamAlias("description")
	protected String description;

	public FuzzyDescriptorModelBase(String label, String description) {
		super();
		this.label = label;
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
