package net.prokhyon.modularfuzzy.api;

import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;

public interface LoadableDataController {

	<T extends WorkspaceFxRootElementBase> void loadWithData(T modelToLoad);
}
