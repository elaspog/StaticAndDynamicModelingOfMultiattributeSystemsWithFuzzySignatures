package net.prokhyon.modularfuzzy.common.modelDescriptor;

import java.io.*;
import java.util.List;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;
import net.prokhyon.modularfuzzy.common.errors.ModuleImplementationException;
import net.prokhyon.modularfuzzy.common.errors.NotParsableDescriptorException;
import org.xml.sax.InputSource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DescriptorHandler {

	protected XStream xstream;

	public DescriptorHandler() {
		super();
	}

	protected static String formatXml(String xml) {

		try {
			Transformer serializer = SAXTransformerFactory.newInstance().newTransformer();

			serializer.setOutputProperty(OutputKeys.INDENT, "yes");
			serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			Source xmlSource = new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
			StreamResult res = new StreamResult(new ByteArrayOutputStream());

			serializer.transform(xmlSource, res);

			return new String(((ByteArrayOutputStream) res.getOutputStream()).toByteArray());

		} catch (Exception e) {
			return xml;
		}
	}

	public <T extends FuzzyDescriptorRootBase> String generateXmlStringFromModel(T model) {
		xstream = new XStream(new StaxDriver());
		xstream.autodetectAnnotations(true);
		String xml = xstream.toXML(model);
		return formatXml(xml);
	}

	public <T extends FuzzyDescriptorRootBase> String generateJsonStringFromModel(T model) {
		xstream = new XStream(new JsonHierarchicalStreamDriver() {

			public HierarchicalStreamWriter createWriter(Writer writer) {
				return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
			}
		});
		xstream.autodetectAnnotations(true);
		return xstream.toXML(model);
	}

	public void saveToXmlFile(String xmlFilePath, String xmlFileData) {

		try {
			Transformer tr = TransformerFactory.newInstance().newTransformer();
			tr.setOutputProperty(OutputKeys.INDENT, "yes");
			tr.setOutputProperty(OutputKeys.METHOD, "xml");
			tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			StreamResult result = new StreamResult(new FileOutputStream(xmlFilePath));
			tr.transform(new StreamSource(new StringReader(xmlFileData)), result);
			result.getOutputStream().close();

		} catch (TransformerException te) {
			System.out.println(te.getMessage());
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	public void saveToTextFile(String filePath, String txtFileData) {

		try {
			FileWriter file = new FileWriter(filePath);
			file.write(txtFileData);
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public <T extends FuzzyDescriptorRootBase>
	T readFromXmlFile(File file,
					  Class<? extends FuzzyDescriptorRootBase> descriptorRootModel,
					  List<Class<? extends FuzzyDescriptorBase>> descriptorModels)
			throws NotParsableDescriptorException {

		try {
			xstream = new XStream(new StaxDriver());
			for (Class<? extends FuzzyDescriptorBase> dm : descriptorModels){
				xstream.processAnnotations(dm);
			}
			xstream.autodetectAnnotations(true);
			final Object o = xstream.fromXML(file);

			if (((T) o).getClass() == descriptorRootModel.cast(o).getClass())
				return (T) o;

			throw new ModuleImplementationException();

		} catch (Exception e){
			throw new NotParsableDescriptorException("Error has occurred while importing XML file: " + file.getAbsolutePath());
		}
	}

	public <T extends FuzzyDescriptorRootBase>
	T readFromJsonFile(File file,
					   Class<? extends FuzzyDescriptorRootBase> descriptorRootModel,
					   List<Class<? extends FuzzyDescriptorBase>> descriptorModels)
			throws NotParsableDescriptorException {

		try {
			//xstream = new XStream(new JettisonMappedXmlDriver());
			xstream = new XStream(new JsonHierarchicalStreamDriver() {

				public HierarchicalStreamWriter createWriter(Writer writer) {
					return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
				}
			});
			for (Class<? extends FuzzyDescriptorBase> dm : descriptorModels){
				xstream.processAnnotations(dm);
			}
			xstream.autodetectAnnotations(true);
			final Object o = xstream.fromXML(file);

			if (((T) o).getClass() == descriptorRootModel.cast(o).getClass())
				return (T) o;

			throw new ModuleImplementationException();

		} catch (Exception e){
			throw new NotParsableDescriptorException("Error has occurred while importing JSON file: " + file.getAbsolutePath());
		}
	}

	public <T extends FuzzyDescriptorRootBase> T readFromTextFile(File file, Class clazz)
			throws NotParsableDescriptorException {

		throw new NotImplementedException();
	}

}
