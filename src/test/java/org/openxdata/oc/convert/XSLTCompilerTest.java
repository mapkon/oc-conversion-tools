package org.openxdata.oc.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.openxdata.oc.convert.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.transforms.TransformationException;

public class XSLTCompilerTest {

	private String sampleXslt;
	private String input;
	private String output;

	@Before
	public void setup() throws InvalidXMLException, IOException {
		sampleXslt = XMLUtil.loadFile("/org/openxdata/oc/sample-xslt.xsl");
		input = XMLUtil.loadFile("/org/openxdata/oc/sample-input-xml.xml");
		output = XMLUtil.loadFile("/org/openxdata/oc/sample-output-xml.xml");
	}


	@Test
	public void compileShouldNotReturnNullOnValidInput() throws TransformationException,
			InvalidXMLException {
	
		
		XSLTCompiler compiler = new XSLTCompiler(sampleXslt);
		Document doc = compiler.transform("<hei/>");
		assertNotNull(doc);
	}

	@Test(expected = InvalidXMLException.class)
	public void compileShouldThrowExceptionOnInvalidXML()
			throws TransformationException, InvalidXMLException {
		XSLTCompiler compiler = new XSLTCompiler(sampleXslt);
		compiler.transform("<invalid--xml--!!>");
	}

	@Test(expected = InvalidXMLException.class)
	public void compileShouldThrowExceptionOnInvalidXSLTXML()
			throws InvalidXMLException, TransformationException {
		XSLTCompiler compiler = new XSLTCompiler("<xslt--dfdf?`/>");
		compiler = new XSLTCompiler("<xslt/>");
		compiler.transform("<hei/>");
	}
	
	@Test(expected = InvalidXMLException.class)
	public void compileShouldThrowExceptionOnInvalidXSLT()
			throws InvalidXMLException, TransformationException {
		XSLTCompiler compiler = new XSLTCompiler("<xslt/>");
		compiler = new XSLTCompiler("<xslt/>");
		compiler.transform("<hei/>");
	}

	@Test
	public void compileShouldCorrectlyTranslateDocument()
			throws InvalidXMLException, TransformationException, ParserConfigurationException, SAXException, IOException {
		
		XSLTCompiler compiler = new XSLTCompiler(sampleXslt);
		Document doc = compiler.transform(input);
		
		Document expectedDoc = XMLUtil.getDocument(output);
		
		assertEquals(expectedDoc.getFirstChild().getNodeName(), doc.getFirstChild().getNodeName());
	}

}
