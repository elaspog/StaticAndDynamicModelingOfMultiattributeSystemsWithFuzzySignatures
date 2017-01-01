package net.prokhyon.modularfuzzy.pathValues.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.common.modelDescriptor.FuzzyDescriptorRootBase;

@XStreamAlias("fuzzymodelvalues")
public class FuzzyModelValues extends FuzzyDescriptorRootBase {

	@XStreamImplicit
	private List<PathInfo> pathList;

	public FuzzyModelValues(String uuid, String fuzzyModelName, String fuzzyModelDescription, List<PathInfo> pathList) {
		super(uuid, fuzzyModelName, fuzzyModelDescription);
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
