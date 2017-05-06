package net.prokhyon.modularfuzzy.fuzzySet.model.fx;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import net.prokhyon.modularfuzzy.common.CommonUtils;
import net.prokhyon.modularfuzzy.common.modelFx.WorkspaceFxRootElementBase;
import net.prokhyon.modularfuzzy.common.conversion.ConvertibleFxModel2Descriptor;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.*;

public class FuzzySetSystem extends WorkspaceFxRootElementBase
		implements ConvertibleFxModel2Descriptor.Internal<net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem, FuzzySetSystem> {

	private final StringProperty uuid;
	private final StringProperty fuzzySystemName;
	private final StringProperty fuzzySystemDescription;
	private final ObjectProperty<FuzzySetSystemTypeEnum> fuzzySystemType;
	private final ListProperty<FuzzySet> fuzzySets;

	public FuzzySetSystem(String uuid, String fuzzySystemName, String fuzzySystemDescription, FuzzySetSystemTypeEnum fuzzySystemType,
						  List<FuzzySet> fuzzySets) {
		super();
		this.uuid = CommonUtils.initializeUUIDPropertyFromString(uuid);
		this.fuzzySystemName = new SimpleStringProperty(fuzzySystemName);
		this.fuzzySystemDescription = new SimpleStringProperty(fuzzySystemDescription);
		this.fuzzySystemType = new SimpleObjectProperty<>(fuzzySystemType);
		this.fuzzySets = new SimpleListProperty<>(FXCollections.observableArrayList(fuzzySets != null ? fuzzySets : new ArrayList<>()));
	}

	public FuzzySetSystem(FuzzySetSystem otherFuzzySetSystem){
		this(otherFuzzySetSystem.uuid.get(), otherFuzzySetSystem.fuzzySystemName.get(),
				otherFuzzySetSystem.fuzzySystemDescription.get(), otherFuzzySetSystem.fuzzySystemType.get(),
				otherFuzzySetSystem.fuzzySets.get());
	}

	public FuzzySetSystem deepCopy() {
		return new FuzzySetSystem( this.uuid.get(), this.fuzzySystemName.get(), this.fuzzySystemDescription.get(), this.fuzzySystemType.get(), deepCopyFuzzySets(this.fuzzySets.get()));
	}

	private List<FuzzySet> deepCopyFuzzySets(List<FuzzySet> fuzzySets){

		if (fuzzySets != null) {
			List<FuzzySet> copiedFuzzySets = new ArrayList<>();
			for (FuzzySet fs : fuzzySets) {
				copiedFuzzySets.add(fs.deepCopy());
			}
			return copiedFuzzySets;
		}
		return fuzzySets;
	}

	public String getFuzzySystemName() {
		return fuzzySystemName.get();
	}

	public void setFuzzySystemName(String fuzzySystemName) {
		this.fuzzySystemName.set(fuzzySystemName);
	}

	public StringProperty fuzzySystemNameProperty() {
		return fuzzySystemName;
	}

	public String getFuzzySystemDescription() {
		return fuzzySystemDescription.get();
	}

	public void setFuzzySystemDescription(String fuzzySystemDescription) {
		this.fuzzySystemDescription.set(fuzzySystemDescription);
	}

	public StringProperty fuzzySystemDescriptionProperty() {
		return fuzzySystemDescription;
	}

	public FuzzySetSystemTypeEnum getFuzzySystemType() {
		return fuzzySystemType.get();
	}

	public void setFuzzySystemType(FuzzySetSystemTypeEnum fuzzySystemType) {
		this.fuzzySystemType.set(fuzzySystemType);
	}

	public ObjectProperty<FuzzySetSystemTypeEnum> fuzzySystemTypeProperty() {
		return fuzzySystemType;
	}

	public List<FuzzySet> getFuzzySets() {
		return fuzzySets.get();
	}

	public void setFuzzySets(List<FuzzySet> fuzzySets) {
		this.fuzzySets.set(FXCollections.observableArrayList(fuzzySets));
	}

	public ListProperty<FuzzySet> fuzzySetsProperty() {
		return fuzzySets;
	}

	@Override
	public String getUuid() {
		return uuid.get();
	}

	public StringProperty uuidProperty() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid.set(uuid);
	}

	@Override
	public net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem convert2DescriptorModel() {

		final List<FuzzySet> fxFuzzySets = this.getFuzzySets();
		List<FuzzySetBase> descriptorFuzzySets = new ArrayList<>();
		for (FuzzySet fxFuzzySet : fxFuzzySets){
			final FuzzySetBase fuzzySetBase = fxFuzzySet.convert2DescriptorModel();
			descriptorFuzzySets.add(fuzzySetBase);
		}
		return new net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem(this.getUuid(),
				this.getFuzzySystemName(), this.getFuzzySystemDescription(), this.getFuzzySystemType(),
				descriptorFuzzySets);
	}

	@Override
	public String getListElementIdentifier() {
		return fuzzySystemName.get() + " (" + fuzzySets.size() + ")" + " [" + fuzzySystemType.get() + "]";
	}
}
