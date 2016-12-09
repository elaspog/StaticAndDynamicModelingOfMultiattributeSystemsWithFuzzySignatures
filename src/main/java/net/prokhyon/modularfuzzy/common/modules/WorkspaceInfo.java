package net.prokhyon.modularfuzzy.common.modules;

public class WorkspaceInfo {

	private String viewName;
	private FxModulesViewInfo loaderInformation;
	private PersistableModelInfo persistableModelInfo;

	public WorkspaceInfo(String viewName, FxModulesViewInfo loaderInformation, PersistableModelInfo persistableModelInfo) {
		this.viewName = viewName;
		this.loaderInformation = loaderInformation;
		this.persistableModelInfo = persistableModelInfo;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public FxModulesViewInfo getLoaderInformation() {
		return loaderInformation;
	}

	public void setLoaderInformation(FxModulesViewInfo loaderInformation) {
		this.loaderInformation = loaderInformation;
	}

	public PersistableModelInfo getPersistableModelInfo() {
		return persistableModelInfo;
	}

	public void setPersistableModelInfo(PersistableModelInfo persistableModelInfo) {
		this.persistableModelInfo = persistableModelInfo;
	}

}
