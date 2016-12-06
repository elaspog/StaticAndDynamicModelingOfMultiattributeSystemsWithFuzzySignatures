package net.prokhyon.modularfuzzy.common.modules;

import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

public class WorkspaceInfo {

	private String viewName;
	private Class<? extends WorkspaceElement> modelType;
	private FxModulesViewInfo loaderInformation;
	private PersistableModelInfo persistableModelInfo;

	public WorkspaceInfo(String viewName, Class<? extends WorkspaceElement> modelType, FxModulesViewInfo loaderInformation, PersistableModelInfo persistableModelInfo) {
		this.viewName = viewName;
		this.modelType = modelType;
		this.loaderInformation = loaderInformation;
		this.persistableModelInfo = persistableModelInfo;
	}

	public WorkspaceInfo(String viewName, Class<? extends WorkspaceElement> modelType,
                         FxModulesViewInfo loaderInformation) {

		this(viewName, modelType, loaderInformation, null);
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Class<? extends WorkspaceElement> getModelType() {
		return modelType;
	}

	public void setModelType(Class<? extends WorkspaceElement> modelType) {
		this.modelType = modelType;
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
