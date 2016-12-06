package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("fuzzyset")
public class FuzzySetTrapezoidal extends FuzzySetBase {

	public FuzzySetTrapezoidal(String id, String label, String description, List<FuzzyPointBase> points) {
		super(id, label, description, FuzzySetTypeEnum.TRAPEZOID, points);
	}

}
