package net.prokhyon.modularfuzzy.common.modules;

public class DefaultModelLoaderInfo {

	private String viewName;
	private PersistableModelInfo persistableModelInfo;

	public DefaultModelLoaderInfo(String viewName, PersistableModelInfo persistableModelInfo) {
		this.viewName = viewName;
		this.persistableModelInfo = persistableModelInfo;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public PersistableModelInfo getPersistableModelInfo() {
		return persistableModelInfo;
	}

	public void setPersistableModelInfo(PersistableModelInfo persistableModelInfo) {
		this.persistableModelInfo = persistableModelInfo;
	}

}
