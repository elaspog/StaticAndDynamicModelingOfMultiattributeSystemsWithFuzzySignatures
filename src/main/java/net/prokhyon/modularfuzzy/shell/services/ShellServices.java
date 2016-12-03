package net.prokhyon.modularfuzzy.shell.services;

import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import net.prokhyon.modularfuzzy.common.FxModulesViewInformationGroup;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.WorkspaceInformationGroup;

public interface ShellServices {

	void initializeModules();

	List<FxModulesViewInformationGroup> getRegisteredViews();

	Map<WorkspaceInformationGroup, ObservableList<? extends WorkspaceElement>> getRegisteredStores();

	void saveModelByModule(ObservableList<? extends WorkspaceElement> modelList, WorkspaceInformationGroup modelInformation);

}
