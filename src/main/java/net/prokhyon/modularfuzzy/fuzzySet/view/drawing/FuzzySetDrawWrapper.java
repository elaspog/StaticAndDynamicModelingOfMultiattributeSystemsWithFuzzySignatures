package net.prokhyon.modularfuzzy.fuzzySet.view.drawing;

import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import net.prokhyon.modularfuzzy.fuzzySet.model.fx.FuzzySet;

public class FuzzySetDrawWrapper {

	private FuzzySet fuzzySet;
	private List<FuzzySetPointDrawCoordinate> transformedCoordinates;
	private Polygon polygon;

	public FuzzySetDrawWrapper(FuzzySet fuzzySet, List<FuzzySetPointDrawCoordinate> transformedCoordinates) {
		super();
		this.fuzzySet = fuzzySet;
		this.transformedCoordinates = transformedCoordinates;
		createDrawObject();
	}

	public void createDrawObject() {

		polygon = new Polygon();
		for (FuzzySetPointDrawCoordinate fspdc : transformedCoordinates) {
			polygon.getPoints().addAll(fspdc.getxPoint(), fspdc.getyPoint());
		}
		polygon.setFill(Color.SKYBLUE);
		polygon.setOpacity(0.5);
		polygon.setStrokeWidth(1);
		polygon.setStroke(Color.BLACK);
	}

	public FuzzySet getFuzzySet() {
		return fuzzySet;
	}

	public void setFuzzySet(FuzzySet fuzzySet) {
		this.fuzzySet = fuzzySet;
	}

	public List<FuzzySetPointDrawCoordinate> getTransformedCoordinates() {
		return transformedCoordinates;
	}

	public void setTransformedCoordinates(List<FuzzySetPointDrawCoordinate> transformedCoordinates) {
		this.transformedCoordinates = transformedCoordinates;
	}

	public Polygon getPolygon() {
		return polygon;
	}
}
