package net.prokhyon.modularfuzzy.shell.services;

import java.util.List;

import net.prokhyon.modularfuzzy.common.modules.FxModulesViewInfo;

public interface ShellServices {

	void initializeModules();

	List<FxModulesViewInfo> getRegisteredViews();

}
