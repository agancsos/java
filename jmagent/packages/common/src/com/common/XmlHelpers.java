package com.common;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import  org.w3c.tidy.Tidy;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


public class XmlHelpers {
	public static NodeList selectNodes(Document doc, String xpath) throws Exception {
		XPath xPath = XPathFactory.newInstance().newXPath();
		return (NodeList) xPath.compile(xpath).evaluate(doc, XPathConstants.NODESET);
	}

	public static String tidyup(String raw) throws Exception {
		Tidy t = new Tidy();
		t.setXHTML(true);
		t.setQuiet(true);
		t.setXmlOut(true);
		t.setShowErrors(0);
		t.setShowWarnings(false);
		t.setForceOutput(true);
		ByteArrayInputStream in = new ByteArrayInputStream(raw.getBytes("UTF-8"));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		t.parse(in, out);
		raw = out.toString("UTF-8");
		return raw;
	}

	public static Document htmlToXml(String raw) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		factory.setFeature("http://xml.org/sax/features/namespaces", false);
		factory.setFeature("http://xml.org/sax/features/validation", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource();
		is.setCharacterStream(new StringReader(raw));
		Document document = builder.parse(is);
		return document;
	}
}
