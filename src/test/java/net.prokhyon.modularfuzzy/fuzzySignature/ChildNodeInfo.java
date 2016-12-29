package fuzzySignature;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("childnode")
public class ChildNodeInfo {

	@XStreamAlias("id")
	@XStreamAsAttribute
	private String id;

	public ChildNodeInfo(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ChildInfo [id=" + id + "]";
	}

}
