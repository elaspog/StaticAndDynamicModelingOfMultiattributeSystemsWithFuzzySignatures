package net.prokhyon.modularfuzzy.fuzzySet.model.fx;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.common.fxModel.FuzzyFxBase;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointAbove;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBelow;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBase;

public class FuzzySetPoint extends FuzzyFxBase
		implements ConvertibleFxModel2Descriptor<net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBase, FuzzySetPoint> {

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

	@Override
	public FuzzyPointBase convert2DescriptorModel() {

		final float xPoint = getXPoint();
		final float yPoint = getYPoint();

		if (yPoint == 1.0){
			return new FuzzyPointAbove(xPoint);
		} else if (yPoint == 0.0) {
			return new FuzzyPointBelow(xPoint);
		} else {
			return new FuzzyPointCustom(xPoint, yPoint);
		}
	}
}
