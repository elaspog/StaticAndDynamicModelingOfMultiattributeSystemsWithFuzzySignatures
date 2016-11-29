package net.prokhyon.modularfuzzy.pathValues.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("fuzzymodelvalues")
public class FuzzyModelValues {

	@XStreamImplicit
	private List<PathInfo> pathList;

	public FuzzyModelValues(List<PathInfo> pathList) {
		super();
		this.pathList = pathList;
	}

	public List<PathInfo> getPathList() {
		return pathList;
	}

	public void setPathList(List<PathInfo> pathList) {
		this.pathList = pathList;
	}

	@Override
	public String toString() {
		return "FuzzyModelValues [pathList=" + pathList + "]";
	}

}
