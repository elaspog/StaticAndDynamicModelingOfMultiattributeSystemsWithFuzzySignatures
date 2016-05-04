package net.prokhyon.modularfuzzy.fuzzySet.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTypeEnum;

public class FuzzySet {

	private final StringProperty fuzySetName;
	private final StringProperty fuzzySetDescription;
	private final ObjectProperty<FuzzySetTypeEnum> fuzzySetType;
	private final ListProperty<FuzzySetPoint> fuzzySetPoints;

	public static Callback<FuzzySet, Observable[]> extractor() {
		return new Callback<FuzzySet, Observable[]>() {
			@Override
			public Observable[] call(FuzzySet param) {
				return new Observable[] { param.fuzySetNameProperty(), param.fuzzySetDescriptionProperty(),
						param.fuzzySetTypeProperty(), param.fuzzySetPointsProperty() };
			}
		};
	}
	
	public FuzzySet(String fuzySetName, String fuzzySetDescription, FuzzySetTypeEnum fuzzySetType,
			List<FuzzySetPoint> fuzzySetPoints) {
		super();
		this.fuzySetName = new SimpleStringProperty(fuzySetName);
		this.fuzzySetDescription = new SimpleStringProperty(fuzzySetDescription);
		this.fuzzySetType = new SimpleObjectProperty<FuzzySetTypeEnum>(fuzzySetType);
		if (fuzzySetPoints == null)
			fuzzySetPoints = new ArrayList<FuzzySetPoint>();
		this.fuzzySetPoints = new SimpleListProperty<FuzzySetPoint>(FXCollections.observableArrayList(fuzzySetPoints));
	}

	public String getFuzySetName() {
		return fuzySetName.get();
	}

	public void setFuzySetName(String fuzySetName) {
		this.fuzySetName.set(fuzySetName);
	}

	public StringProperty fuzySetNameProperty() {
		return fuzySetName;
	}

	public String getFuzzySetDescription() {
		return fuzzySetDescription.get();
	}

	public void setFuzzySetDescription(String fuzzySetDescription) {
		this.fuzzySetDescription.set(fuzzySetDescription);
	}

	public StringProperty fuzzySetDescriptionProperty() {
		return fuzzySetDescription;
	}

	public FuzzySetTypeEnum getFuzzySetType() {
		return fuzzySetType.get();
	}

	public void setFuzzySetType(FuzzySetTypeEnum fuzzySetType) {
		this.fuzzySetType.set(fuzzySetType);
	}

	public ObjectProperty<FuzzySetTypeEnum> fuzzySetTypeProperty() {
		return fuzzySetType;
	}

	public List<FuzzySetPoint> getFuzzySetPoints() {
		return fuzzySetPoints.get();
	}

	public void setFuzzySetPoints(List<FuzzySetPoint> fuzzySetPoints) {
		this.fuzzySetPoints.set(FXCollections.observableArrayList(fuzzySetPoints));
	}

	public ListProperty<FuzzySetPoint> fuzzySetPointsProperty() {
		return fuzzySetPoints;
	}

	@Override
	public String toString() {
		return fuzySetName.get() + " : " + fuzzySetType.get();
	}

}
