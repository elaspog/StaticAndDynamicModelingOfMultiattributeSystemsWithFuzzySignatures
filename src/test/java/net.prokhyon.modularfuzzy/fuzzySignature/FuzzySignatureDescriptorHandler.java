package net.prokhyon.modularfuzzy.fuzzySignature;

import net.prokhyon.modularfuzzy.common.descriptor.DescriptorHandlerBase;
import net.prokhyon.modularfuzzy.common.descriptor.IDescriptorHandler;
import net.prokhyon.modularfuzzy.fuzzySignature.model.descriptor.*;

import java.util.ArrayList;
import java.util.List;


public class FuzzySignatureDescriptorHandler extends DescriptorHandlerBase implements IDescriptorHandler {

	String xml;

	public FuzzySignatureDescriptorHandler() {
		super();
	}

	public FuzzySignature createSample() {

		List<FuzzyNode> nodes = new ArrayList<FuzzyNode>();

		nodes.add(new FuzzyNode("n1", "rootNode", "some descr", new ArrayList<ChildNodeInfo>() {
			{
				add(new ChildNodeInfo("n11"));
				add(new ChildNodeInfo("n12"));
			}
		}, new ArrayList<SubTreeInfo>() {
			{
				add(new SubTreeInfo("tree_01"));
			}
		}, "OWA", "3set"));
		nodes.add(new FuzzyNode("n11", null, null, new ArrayList<ChildNodeInfo>() {
			{
				add(new ChildNodeInfo("n111"));
				add(new ChildNodeInfo("n112"));
			}
		}, new ArrayList<SubTreeInfo>() {
			{
				add(new SubTreeInfo("tree_02"));
				add(new SubTreeInfo("tree_03"));
			}
		}, "OWA", "5set"));
		nodes.add(new FuzzyNode("n12", null, "nincd leszarmazott", null, null, "OWA2", "5set"));
		nodes.add(new FuzzyNode("n111", "level1", null, null, null, "SMA1", "5set"));
		nodes.add(new FuzzyNode("n112", "level1", null, null, null, "SMA3", null));

		return new FuzzySignature("3set", "szignatura leiras", FuzzyTreeTypeEnum.PARTIAL_TYPE, "rootNodeId", nodes);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {

		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		FuzzySignature fuzzySignature = (FuzzySignature) xstream.fromXML(xml);
		System.out.println(fuzzySignature);
	}

}
