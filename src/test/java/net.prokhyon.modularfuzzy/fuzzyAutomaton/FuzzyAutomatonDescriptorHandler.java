package net.prokhyon.modularfuzzy.fuzzyAutomaton;

import java.util.ArrayList;
import java.util.List;

import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.modelDescriptor.IDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyAutomaton;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyState;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyStateTypeEnum;
import net.prokhyon.modularfuzzy.fuzzyAutomaton.model.descriptor.FuzzyTransition;

public class FuzzyAutomatonDescriptorHandler extends DescriptorHandler implements IDescriptorHandler {

	String xml;

	public FuzzyAutomatonDescriptorHandler() {
		super();
	}

	public FuzzyAutomaton createSample() {

		List<FuzzyState> states = new ArrayList<FuzzyState>();
		List<FuzzyTransition> transitions = new ArrayList<FuzzyTransition>();

		states.add(new FuzzyState( "label1", null, FuzzyStateTypeEnum.INITIAL, "gyenge"));
		states.add(new FuzzyState( "label2", null, FuzzyStateTypeEnum.NORMAL, "kozepes"));
		states.add(new FuzzyState( "label3", "description3", FuzzyStateTypeEnum.TERMINAL, "nagyon_jo"));

		transitions.add(new FuzzyTransition( "lab1", "descr1", "id1", "id2", null));
		transitions.add(new FuzzyTransition( "lab2", null, "id2", "id3", null));
		transitions.add(new FuzzyTransition( "lab3", null, "id1", "id3", null));

		return new FuzzyAutomaton("uuid", "automaton_id", "automata leiras", "3set", states, transitions, 0);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {

		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		FuzzyAutomaton fuzzyAutomaton = (FuzzyAutomaton) xstream.fromXML(xml);
		System.out.println(fuzzyAutomaton);
	}

}
