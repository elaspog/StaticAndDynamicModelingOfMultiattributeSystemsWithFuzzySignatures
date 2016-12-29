package fuzzySignature;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("subtree")
public class SubTreeInfo {

	@XStreamAlias("typeid")
	@XStreamAsAttribute
	private String typeId;

	public String getId() {
		return typeId;
	}

	public void setId(String typeId) {
		this.typeId = typeId;
	}

	public SubTreeInfo(String typeId) {
		super();
		this.typeId = typeId;
	}

	@Override
	public String toString() {
		return "SubTreeInfo [typeId=" + typeId + "]";
	}
}
