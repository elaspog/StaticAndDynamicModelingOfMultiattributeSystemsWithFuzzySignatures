package net.prokhyon.modularfuzzy.common.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

public abstract class FuzzyDescriptorRootBase extends FuzzyDescriptorBase {

	@XStreamAlias("uuid")
	@XStreamAsAttribute
	protected String uuid;

	@XStreamAlias("typeid")
	@XStreamAsAttribute
	protected String typeId;

	@XStreamAlias("description")
	protected String description;

	public FuzzyDescriptorRootBase(String typeId, String description) {
		super();
		this.typeId = typeId;
		this.description = description;
	}

	public String getUUID(){
		return uuid;
	}

	public void setUUID(String uuid){
		this.uuid = uuid;
	}

	public String getTypeid() {
		return typeId;
	}

	public void setTypeid(String typeid) {
		this.typeId = typeid;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
