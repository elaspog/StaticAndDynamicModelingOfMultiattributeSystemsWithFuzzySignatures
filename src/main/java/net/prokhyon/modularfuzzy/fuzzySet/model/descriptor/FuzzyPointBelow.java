package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("below")
public class FuzzyPointBelow extends FuzzyPointBase {

	@XStreamAlias("x")
	@XStreamAsAttribute
	private double xCoordinate;

	public FuzzyPointBelow(double xCoordinate) {
		super();
		this.xCoordinate = xCoordinate;
	}

	@Override
	public int getCoordinateDimension() {
		return 1;
	}

	public double getxCoordinate() {
		return xCoordinate;
	}

	public void setxCoordinate(double xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	@Override
	public String toString() {
		return "FuzzyPointBelow [xCoordinate=" + xCoordinate + "]";
	}

}
