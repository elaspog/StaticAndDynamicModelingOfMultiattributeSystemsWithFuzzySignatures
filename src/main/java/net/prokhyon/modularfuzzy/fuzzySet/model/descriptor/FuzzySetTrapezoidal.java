package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("fuzzyset")
public class FuzzySetTrapezoidal extends FuzzySetBase {

	public FuzzySetTrapezoidal(String label, String description, List<FuzzyPointBase> points) {
		super(label, description, FuzzySetTypeEnum.TRAPEZOID, points);
	}

}
