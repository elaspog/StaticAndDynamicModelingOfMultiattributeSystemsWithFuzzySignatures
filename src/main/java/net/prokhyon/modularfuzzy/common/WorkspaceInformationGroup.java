package net.prokhyon.modularfuzzy.common;

public class WorkspaceInformationGroup {

	private String viewName;
	private Class<? extends WorkspaceElement> modelType;
	private FxModulesViewInformationGroup loaderInformation;
	private PersistableModelTuple persistableModelTuple;

	public WorkspaceInformationGroup(String viewName, Class<? extends WorkspaceElement> modelType, FxModulesViewInformationGroup loaderInformation, PersistableModelTuple persistableModelTuple) {
		this.viewName = viewName;
		this.modelType = modelType;
		this.loaderInformation = loaderInformation;
		this.persistableModelTuple = persistableModelTuple;
	}

	public WorkspaceInformationGroup(String viewName, Class<? extends WorkspaceElement> modelType,
									 FxModulesViewInformationGroup loaderInformation) {

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

	public FxModulesViewInformationGroup getLoaderInformation() {
		return loaderInformation;
	}

	public void setLoaderInformation(FxModulesViewInformationGroup loaderInformation) {
		this.loaderInformation = loaderInformation;
	}

	public PersistableModelTuple getPersistableModelTuple() {
		return persistableModelTuple;
	}

	public void setPersistableModelTuple(PersistableModelTuple persistableModelTuple) {
		this.persistableModelTuple = persistableModelTuple;
	}

}
