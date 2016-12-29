package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("AbovePoint")
public class FuzzyPointAbove extends FuzzyPointBase {

	@XStreamAlias("X")
	@XStreamAsAttribute
	private double xCoordinate;

	public FuzzyPointAbove(double d) {
		super();
		this.xCoordinate = d;
	}

	@Override
	public int getCoordinateDimension() {
		return 1;
	}

	public double getxCoordinate() {
		return xCoordinate;
	}

	@Override
	public String toString() {
		return "FuzzyPointAbove [xCoordinate=" + xCoordinate + "]";
	}

}
