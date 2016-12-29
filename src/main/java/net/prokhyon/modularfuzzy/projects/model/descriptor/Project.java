package net.prokhyon.modularfuzzy.projects.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem;
import fuzzySignature.FuzzySignature_Old;
import net.prokhyon.modularfuzzy.pathValues.model.descriptor.FuzzyModelValues;

@XStreamAlias("BuildingConditionExaminerProject")
public class Project {

	@XStreamImplicit
	private List<FuzzySetSystem> sets;

	@XStreamImplicit
	private List<FuzzyAutomaton> automatons;

	@XStreamImplicit
	private List<FuzzySignature_Old> signatures;

	@XStreamImplicit
	private List<FuzzyModelValues> models;

	public Project(List<FuzzySetSystem> sets, List<FuzzyAutomaton> automatons, List<FuzzySignature_Old> signatures,
			List<FuzzyModelValues> models) {
		super();
		this.sets = sets;
		this.automatons = automatons;
		this.signatures = signatures;
		this.models = models;
	}

	public List<FuzzySetSystem> getSets() {
		return sets;
	}

	public void setSets(List<FuzzySetSystem> sets) {
		this.sets = sets;
	}

	public List<FuzzyAutomaton> getAutomatons() {
		return automatons;
	}

	public void setAutomatons(List<FuzzyAutomaton> automatons) {
		this.automatons = automatons;
	}

	public List<FuzzySignature_Old> getSignatures() {
		return signatures;
	}

	public void setSignatures(List<FuzzySignature_Old> signatures) {
		this.signatures = signatures;
	}

	public List<FuzzyModelValues> getModels() {
		return models;
	}

	public void setModels(List<FuzzyModelValues> models) {
		this.models = models;
	}

	@Override
	public String toString() {
		return "Project [sets=" + sets + ", automatons=" + automatons + ", signatures=" + signatures + ", models="
				+ models + "]";
	}

}
