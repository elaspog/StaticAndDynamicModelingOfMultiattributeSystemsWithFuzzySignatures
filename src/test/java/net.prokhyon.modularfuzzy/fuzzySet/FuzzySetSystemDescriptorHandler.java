package net.prokhyon.modularfuzzy.fuzzySet;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.common.descriptor.DescriptorHandlerBase;
import net.prokhyon.modularfuzzy.common.descriptor.IDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointAbove;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointBelow;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzyPointCustom;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetBase;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetPolygonal;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystem;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetSystemTypeEnum;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTrapezoidal;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.FuzzySetTriangular;
import net.prokhyon.modularfuzzy.fuzzySet.model.descriptor.IFuzzyPoint;

public class FuzzySetSystemDescriptorHandler extends DescriptorHandlerBase implements IDescriptorHandler {

	String xml;

	public FuzzySetSystemDescriptorHandler() {
		super();
	}

	public FuzzySetSystem createSample() {
		List<FuzzySetBase> list1 = new ArrayList<FuzzySetBase>();

		List<IFuzzyPoint> list21 = new ArrayList<IFuzzyPoint>();
		List<IFuzzyPoint> list22 = new ArrayList<IFuzzyPoint>();
		List<IFuzzyPoint> list23 = new ArrayList<IFuzzyPoint>();
		List<IFuzzyPoint> list24 = new ArrayList<IFuzzyPoint>();

		list21.add(new FuzzyPointBelow(0.1));
		list21.add(new FuzzyPointAbove(0.2));
		list21.add(new FuzzyPointBelow(0.3));

		list22.add(new FuzzyPointBelow(0.2));
		list22.add(new FuzzyPointAbove(0.3));
		list22.add(new FuzzyPointAbove(0.4));
		list22.add(new FuzzyPointBelow(0.5));

		list23.add(new FuzzyPointBelow(0.9));
		list23.add(new FuzzyPointAbove(1.0));

		list24.add(new FuzzyPointBelow(0.35));
		list24.add(new FuzzyPointCustom(0.4, 0.5));
		list24.add(new FuzzyPointAbove(0.5));
		list24.add(new FuzzyPointAbove(0.6));
		list24.add(new FuzzyPointBelow(0.7));

		list1.add(new FuzzySetTriangular("haromszog", "rossz", "rossz", list21));
		list1.add(new FuzzySetTrapezoidal("negyszog", "elfogadhato", null, list22));
		list1.add(new FuzzySetPolygonal("toredek1", "kozepes", null, list23));
		list1.add(new FuzzySetPolygonal("toredek2", "kivallo", "szuperjo ezt nem szabad elcseszni", list24));

		return new FuzzySetSystem("5set", "haromhalmazos leiras", FuzzySetSystemTypeEnum.RUSPINNI_PARTITION, list1);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {

		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		FuzzySetSystem fuzzySetSystem = (FuzzySetSystem) xstream.fromXML(xml);
		System.out.println(fuzzySetSystem);
	}

}
