package net.prokhyon.modularfuzzy.common;

import javafx.scene.layout.Pane;

public class FxModulesViewInfo {

	String viewName;
	String viewRelativePath;
	Class<?> relativeResourceClass;
	Class<? extends Pane> paneType;

	public FxModulesViewInfo(String viewName, String viewRelativePath, Class<?> relativeResourceClass,
                             Class<? extends Pane> paneType) {
		super();
		this.viewName = viewName;
		this.viewRelativePath = viewRelativePath;
		this.relativeResourceClass = relativeResourceClass;
		this.paneType = paneType;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewRelativePath() {
		return viewRelativePath;
	}

	public void setViewRelativePath(String viewRelativePath) {
		this.viewRelativePath = viewRelativePath;
	}

	public Class<?> getRelativeResourceClass() {
		return relativeResourceClass;
	}

	public void setRelativeResourceClass(Class<?> relativeResourceClass) {
		this.relativeResourceClass = relativeResourceClass;
	}

	public Class<? extends Pane> getPaneType() {
		return paneType;
	}

	public void setPaneType(Class<? extends Pane> paneType) {
		this.paneType = paneType;
	}

}
