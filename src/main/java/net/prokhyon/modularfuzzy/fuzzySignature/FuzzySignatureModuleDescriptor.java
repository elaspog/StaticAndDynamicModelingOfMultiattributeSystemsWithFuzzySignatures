package net.prokhyon.modularfuzzy.fuzzySignature;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.api.ModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.FuzzyAutomatonModuleDescriptor;
import net.prokhyon.modularfuzzy.fuzzySet.FuzzySetModuleDescriptor;

public class FuzzySignatureModuleDescriptor implements ModuleDescriptor {

	@Override
	public List<Class<? extends ModuleDescriptor>> getModuleDependencyList() {

		List<Class<? extends ModuleDescriptor>> dependencies = new ArrayList<Class<? extends ModuleDescriptor>>();
		dependencies.add(FuzzySetModuleDescriptor.class);
		dependencies.add(FuzzyAutomatonModuleDescriptor.class);
		return dependencies;
	}

	@Override
	public String getPublicName() {
		return "Fuzzy Signature";
	}

}
