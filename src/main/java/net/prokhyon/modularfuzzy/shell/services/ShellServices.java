package net.prokhyon.modularfuzzy.shell.services;

import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.stage.Stage;
import net.prokhyon.modularfuzzy.common.FxModulesViewInfo;
import net.prokhyon.modularfuzzy.common.WorkspaceElement;
import net.prokhyon.modularfuzzy.common.WorkspaceInfo;

public interface ShellServices {

	void initializeModules();

	List<FxModulesViewInfo> getRegisteredViews();

	Map<WorkspaceInfo, ObservableList<? extends WorkspaceElement>> getRegisteredStores();

	void saveModelByModule(ObservableList<? extends WorkspaceElement> modelList, WorkspaceInfo modelInformation);

	Stage getShellStage();

}
