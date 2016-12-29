package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FuzzySet")
public class FuzzySetPolygonal extends FuzzySetBase {

	public FuzzySetPolygonal(String label, String description, List<FuzzyPointBase> points) {
		super(label, description, FuzzySetTypeEnum.POLYGONAL, points);
	}

}
