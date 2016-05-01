package net.prokhyon.modularfuzzy.pathValues;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySignature.FuzzySignatureModuleDescriptor;

public class PathValuesModuleDescriptor implements ModuleDescriptor {

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		dependencies.add(FuzzyAutomatonModuleDescriptor.class);
		dependencies.add(FuzzySignatureModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Path Values";
	}
}
