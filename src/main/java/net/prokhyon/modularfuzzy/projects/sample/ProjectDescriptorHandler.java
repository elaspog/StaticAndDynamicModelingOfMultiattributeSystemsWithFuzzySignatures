package net.prokhyon.modularfuzzy.projects.sample;

import net.prokhyon.modularfuzzy.common.descriptor.DescriptorHandlerBase;
import net.prokhyon.modularfuzzy.common.descriptor.IDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.sample.FuzzyAutomatonDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzySet.sample.FuzzySetSystemDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzySignature.sample.FuzzySignatureDescriptorHandler;
import net.prokhyon.modularfuzzy.pathValues.sample.FuzzyModelValuesDescriptorHandler;
import net.prokhyon.modularfuzzy.projects.model.descriptor.Project;

import java.util.ArrayList;


public class ProjectDescriptorHandler extends DescriptorHandlerBase implements IDescriptorHandler {

	String xml;
	private FuzzySetSystemDescriptorHandler fuzzySetSystemDescriptorHandler;
	private FuzzyAutomatonDescriptorHandler fuzzyAutomatonDescriptorHandler;
	private FuzzySignatureDescriptorHandler fuzzySignatureDescriptorHandler;
	private FuzzyModelValuesDescriptorHandler fuzzyModelValuesDescriptorHandler;

	public ProjectDescriptorHandler() {
		super();
		fuzzySetSystemDescriptorHandler = new FuzzySetSystemDescriptorHandler();
		fuzzyAutomatonDescriptorHandler = new FuzzyAutomatonDescriptorHandler();
		fuzzySignatureDescriptorHandler = new FuzzySignatureDescriptorHandler();
		fuzzyModelValuesDescriptorHandler = new FuzzyModelValuesDescriptorHandler();
	}

	private Project createSample() {

		ArrayList setSystems = new ArrayList() {
			{
				add(fuzzySetSystemDescriptorHandler.createSample());
			}
		};

		ArrayList signatures = new ArrayList() {
			{
				add(fuzzySignatureDescriptorHandler.createSample());
			}
		};

		ArrayList automatons = new ArrayList() {
			{
				add(fuzzyAutomatonDescriptorHandler.createSample());
			}
		};

		ArrayList modelValues = new ArrayList() {
			{
				add(fuzzyModelValuesDescriptorHandler.createSample());
			}
		};

		return new Project(setSystems, automatons, signatures, modelValues);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {

		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		Project project = (Project) xstream.fromXML(xml);
		System.out.println(project);
	}
}
