package net.prokhyon.modularfuzzy.common.modelDescriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class DescriptorRootBase extends DescriptorBase {

	@XStreamAlias("uuid")
	@XStreamAsAttribute
	protected String uuid;

	@XStreamAlias("typename")
	@XStreamAsAttribute
	protected String typeName;

	@XStreamAlias("description")
	protected String description;

	public DescriptorRootBase(String uuid, String typeName, String description) {
		super();
		this.uuid = uuid;
		this.typeName = typeName;
		this.description = description;
	}

	public String getUUID(){
		return uuid;
	}

	public void setUUID(String uuid){
		this.uuid = uuid;
	}

	public String getTypename() {
		return typeName;
	}

	public void setTypename(String typename) {
		this.typeName = typename;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
