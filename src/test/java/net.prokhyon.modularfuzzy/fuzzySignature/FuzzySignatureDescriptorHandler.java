package net.prokhyon.modularfuzzy.fuzzySignature;

import fuzzySignature.*;
import net.prokhyon.modularfuzzy.common.modelDescriptor.DescriptorHandler;
import net.prokhyon.modularfuzzy.common.modelDescriptor.IDescriptorHandler;

import java.util.ArrayList;
import java.util.List;


public class FuzzySignatureDescriptorHandler extends DescriptorHandler implements IDescriptorHandler {

	String xml;

	public FuzzySignatureDescriptorHandler() {
		super();
	}

	public FuzzySignature_Old createSample() {

		List<FuzzyNode_Old> nodes = new ArrayList<FuzzyNode_Old>();

		nodes.add(new FuzzyNode_Old( "rootNode", "some descr", new ArrayList<ChildNodeInfo>() {
			{
				add(new ChildNodeInfo("n11"));
				add(new ChildNodeInfo("n12"));
			}
		}, new ArrayList<SubTreeInfo>() {
			{
				add(new SubTreeInfo("tree_01"));
			}
		}, "OWA", "3set"));
		nodes.add(new FuzzyNode_Old( null, null, new ArrayList<ChildNodeInfo>() {
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
		nodes.add(new FuzzyNode_Old( null, "nincd leszarmazott", null, null, "OWA2", "5set"));
		nodes.add(new FuzzyNode_Old( "level1", null, null, null, "SMA1", "5set"));
		nodes.add(new FuzzyNode_Old( "level1", null, null, null, "SMA3", null));

		return new FuzzySignature_Old("uuid", "3set", "szignatura leiras", FuzzyTreeTypeEnum.PARTIAL_TYPE, "rootNodeId", nodes);
	}

	@Override
	public void writeXMLFile(String outputFilePath) {

		xml = xstream.toXML(createSample());
		System.out.println(formatXml(xml));
	}

	@Override
	public void readXMLFile(String inputFilePath) {

		FuzzySignature_Old fuzzySignature = (FuzzySignature_Old) xstream.fromXML(xml);
		System.out.println(fuzzySignature);
	}

}
