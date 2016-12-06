package net.prokhyon.modularfuzzy.api;

import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceElement;

public interface LoadableDataController {

	<T extends WorkspaceElement> void loadWithData(T modelToLoad);
}
