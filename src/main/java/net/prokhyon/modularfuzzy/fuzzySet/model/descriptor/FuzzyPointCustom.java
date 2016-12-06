package net.prokhyon.modularfuzzy.fuzzySet.model.descriptor;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("custom")
public class FuzzyPointCustom extends FuzzyPointBase {

	@XStreamAlias("x")
	@XStreamAsAttribute
	private double xCoordinate;

	@XStreamAlias("y")
	@XStreamAsAttribute
	private double yCoordinate;

	public FuzzyPointCustom(double xCoordinate, double yCoordinate) {
		super();
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	@Override
	public int getCoordinateDimension() {
		return 2;
	}

	public double getxCoordinate() {
		return xCoordinate;
	}

	public double getyCoordinate() {
		return yCoordinate;
	}

	@Override
	public String toString() {
		return "FuzzyPointMiddle [xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}

}
