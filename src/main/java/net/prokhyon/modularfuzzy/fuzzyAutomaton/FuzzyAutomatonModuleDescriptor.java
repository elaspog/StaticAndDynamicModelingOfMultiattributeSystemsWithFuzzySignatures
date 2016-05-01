package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;

public class FuzzyAutomatonModuleDescriptor implements ModuleDescriptor {

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Automaton";
	}

}
