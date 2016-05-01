package net.prokhyon.modularfuzzy.fuzzySet;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;

public class FuzzySetModuleDescriptor implements ModuleDescriptor {

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Set";
	}

}
