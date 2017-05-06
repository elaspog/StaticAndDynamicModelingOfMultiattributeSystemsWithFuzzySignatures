package net.prokhyon.modularfuzzy.common.modelDescriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class DescriptorModelBase extends DescriptorBase {

	@XStreamAlias("id")
	@XStreamAsAttribute
	protected String id;

	@XStreamAlias("description")
	protected String description;

	public DescriptorModelBase(String id, String description) {
		super();
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
