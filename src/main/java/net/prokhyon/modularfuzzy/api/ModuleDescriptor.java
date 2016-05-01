package net.prokhyon.modularfuzzy.api;

import java.util.List;

public interface ModuleDescriptor {

	List<Class<? extends ModuleDescriptor>> getModuleDependencyList();

	String getPublicName();

}
