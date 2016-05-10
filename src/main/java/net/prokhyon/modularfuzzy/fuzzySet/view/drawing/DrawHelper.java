package net.prokhyon.modularfuzzy.fuzzySet.view.drawing;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import net.prokhyon.modularfuzzy.fuzzySet.model.FuzzySet;
import net.prokhyon.modularfuzzy.fuzzySet.model.FuzzySetPoint;
import net.prokhyon.modularfuzzy.fuzzySet.model.FuzzySetSystem;

public class DrawHelper {

	private static Pane pane;

	private static ObjectProperty<FuzzySetSystem> fuzzySystem;

	public static void initialize(Pane fuzzySetSystemPane, ObjectProperty<FuzzySetSystem> fuzzySetSystem) {

		pane = fuzzySetSystemPane;
		fuzzySystem = fuzzySetSystem;
	}

	public static void clearPane() {

		pane.getChildren().clear();
	}

	public static void drawFuzzySystem() {

		clearPane();

		double padding = 0.05;
		double height = pane.getHeight();
		double width = pane.getWidth();
		double startingPosX = padding * width;
		double startingPosY = padding * height;
		double widthX = width - 2 * padding * width;
		double heightY = height - 2 * padding * height;

		Rectangle baseSetFrame = getTransformedBaseSetForDrawing(startingPosX, startingPosY, widthX, heightY);
		pane.getChildren().addAll(baseSetFrame);

		FuzzySetSystem fuzzySetSystem = fuzzySystem.get();
		if (fuzzySetSystem != null) {

			List<FuzzySetDrawWrapper> scaleFuzzySetsToDrawCoordinates = getTransformedFuzzySetsToDrawing(startingPosX,
					startingPosY, widthX, heightY, fuzzySetSystem);

			for (FuzzySetDrawWrapper fsdw : scaleFuzzySetsToDrawCoordinates) {
				Polygon polygon = fsdw.getPolygon();
				pane.getChildren().addAll(polygon);
			}
		}

	}

	private static Rectangle getTransformedBaseSetForDrawing(double startingPosX, double startingPosY, double widthX,
			double heightY) {

		Rectangle baseSetFrame = new Rectangle(startingPosX, startingPosY, widthX, heightY);
		baseSetFrame.setStroke(Color.LIGHTGRAY);
		baseSetFrame.setFill(null);
		baseSetFrame.setStrokeWidth(1);
		return baseSetFrame;
	}

	private static List<FuzzySetDrawWrapper> getTransformedFuzzySetsToDrawing(double startingPosX, double startingPosY,
			double widthX, double heightY, FuzzySetSystem fuzzySetSystem) {

		List<FuzzySetDrawWrapper> transforedSets = new ArrayList<FuzzySetDrawWrapper>();
		for (FuzzySet fs : fuzzySetSystem.getFuzzySets()) {

			List<FuzzySetPointDrawCoordinate> transformedPoints = new ArrayList<FuzzySetPointDrawCoordinate>();
			for (FuzzySetPoint fsp : fs.getFuzzySetPoints()) {

				Double xPoint = (startingPosX + (fsp.getXPoint() * widthX));
				Double yPoint = (startingPosY + ((1 - fsp.getYPoint()) * heightY));
				transformedPoints.add(new FuzzySetPointDrawCoordinate(xPoint, yPoint));
			}
			transforedSets.add(new FuzzySetDrawWrapper(fs, transformedPoints));
		}
		return transforedSets;
	}
}
