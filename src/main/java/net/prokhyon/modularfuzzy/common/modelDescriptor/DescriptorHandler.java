package net.prokhyon.modularfuzzy.common.modelDescriptor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Writer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class DescriptorHandler {

	protected XStream xstream;

	public DescriptorHandler() {
		super();
	}

	protected static String formatXml(String xml) {

		try {
			Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
			StreamResult res = new StreamResult(new ByteArrayOutputStream());

			serializer.transform(xmlSource, res);

			return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());

		} catch (Exception e) {
			return xml;
		}
	}

	public <T extends FuzzyDescriptorRootBase> String getXml(T model) {
		xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);
		String xml = xstream.toXML(model);
		return formatXml(xml);
	}

	public <T extends FuzzyDescriptorRootBase> String getJson(T model) {
		xstream = new XStream(new JsonHierarchicalStreamDriver() {

			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		xstream.autodetectAnnotations(true);
		return xstream.toXML(model);
	}
}
