package net.prokhyon.modularfuzzy.api;

import java.util.List;

public interface ModuleDescriptor {

	String getPublicName();

	List<Class<? extends ModuleDescriptor>> getModuleDependencyList();

	void registerShellInstantiatedDependencies(List<ModuleDescriptor> dependencies);

	void initializeModule();

	ModuleMainController getMainController();

}
