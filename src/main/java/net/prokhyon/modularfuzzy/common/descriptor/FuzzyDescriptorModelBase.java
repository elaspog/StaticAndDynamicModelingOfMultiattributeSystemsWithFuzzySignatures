package net.prokhyon.modularfuzzy.common.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class FuzzyDescriptorModelBase extends FuzzyDescriptorBase {

	@XStreamAlias("id")
	@XStreamAsAttribute
	protected String id;

	@XStreamAlias("label")
	@XStreamAsAttribute
	protected String label;

	@XStreamAlias("description")
	protected String description;

	public FuzzyDescriptorModelBase(String id, String label, String description) {
		super();
		this.id = id;
		this.label = label;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
