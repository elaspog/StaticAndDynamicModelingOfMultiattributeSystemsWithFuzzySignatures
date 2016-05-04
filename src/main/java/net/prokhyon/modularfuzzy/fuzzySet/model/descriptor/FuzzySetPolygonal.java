package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("fuzzyset")
public class FuzzySetPolygonal extends FuzzySetBase {

	public FuzzySetPolygonal(String id, String label, String description, List<IFuzzyPoint> points) {
		super(id, label, description, FuzzySetTypeEnum.POLYGONAL, points);
	}

}
