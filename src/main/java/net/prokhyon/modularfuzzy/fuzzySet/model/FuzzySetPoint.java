package net.prokhyon.modularfuzzy.fuzzySet.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

public class FuzzySetPoint {

	private final FloatProperty xPoint;
	private final FloatProperty yPoint;

	public FuzzySetPoint(float xPoint, float yPoint) {
		super();
		this.xPoint = new SimpleFloatProperty(xPoint);
		this.yPoint = new SimpleFloatProperty(yPoint);
	}

	public float getXPoint() {
		return xPoint.get();
	}

	public void setXPoint(float xPoint) {
		this.xPoint.set(xPoint);
	}

	public FloatProperty xPointProperty() {
		return xPoint;
	}

	public float getYPoint() {
		return yPoint.get();
	}

	public void setYPoint(float yPoint) {
		this.yPoint.set(yPoint);
	}

	public FloatProperty yPointProperty() {
		return yPoint;
	}

	@Override
	public String toString() {
		return "FuzzySetPoint [xPoint=" + xPoint + ", yPoint=" + yPoint + "]";
	}

}
