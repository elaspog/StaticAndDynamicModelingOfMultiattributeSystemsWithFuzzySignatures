package net.prokhyon.modularfuzzy.pathValues.sample;

import net.prokhyon.modularfuzzy.common.descriptor.DescriptorHandlerBase;
import net.prokhyon.modularfuzzy.common.descriptor.IDescriptorHandler;
import net.prokhyon.modularfuzzy.pathValues.model.descriptor.FuzzyModelValues;
import net.prokhyon.modularfuzzy.pathValues.model.descriptor.PathInfo;

import java.util.ArrayList;
import java.util.List;


public class FuzzyModelValuesDescriptorHandler extends DescriptorHandlerBase implements IDescriptorHandler {

	String xml;

	public FuzzyModelValuesDescriptorHandler() {
		super();
	}

	public FuzzyModelValues createSample() {

		List<PathInfo> pathInfoList = new ArrayList<PathInfo>();

		pathInfoList.add(new PathInfo("F1.cs1.cs2.T1.cs1", "minoseges"));
		pathInfoList.add(new PathInfo("F1.cs1.cs2.T1.cs2", "korhadt"));
		pathInfoList.add(new PathInfo("F1.cs3.T2.cs1", "kozepes"));

		return new FuzzyModelValues(pathInfoList);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {
		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		FuzzyModelValues fuzzyModelValues = (FuzzyModelValues) xstream.fromXML(xml);
		System.out.println(fuzzyModelValues);
	}

}
