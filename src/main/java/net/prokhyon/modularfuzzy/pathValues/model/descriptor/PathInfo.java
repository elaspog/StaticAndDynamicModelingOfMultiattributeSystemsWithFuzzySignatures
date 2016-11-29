package net.prokhyon.modularfuzzy.pathValues.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("fuzzyvalue")
public class PathInfo {

	@XStreamAlias("path")
	@XStreamAsAttribute
	private String path;

	@XStreamAlias("value")
	@XStreamAsAttribute
	private String value;

	public PathInfo(String path, String value) {
		super();
		this.path = path;
		this.value = value;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "PathInfo [path=" + path + ", value=" + value + "]";
	}

}
