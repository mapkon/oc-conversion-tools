package org.openxdata.oc.convert;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.security.transforms.TransformationException;

public class XSLTCompilerTest {
	
	XSLTCompiler compiler;
	
	@Before
	public void setup() throws InvalidXMLException {
		compiler = new XSLTCompiler("<xslt/>");
	}

	@Test
	public void compileShouldNotReturnNull() throws TransformationException, InvalidXMLException {
		Document doc = compiler.transform("<hei/>");
		assertNotNull(doc);
	}
	
	@Test
	public void compileShouldReturnErrorDocumentOnInvalidXML() throws TransformationException, InvalidXMLException {
		Document doc = compiler.transform("<hei/>");
		NodeList elements = doc.getChildNodes();
		for(int i = 0; i < elements.getLength(); i++) {
			Node node = elements.item(i);
			short nodeType = node.getNodeType();
			if (nodeType == Node.ELEMENT_NODE) {
				String name = node.getNodeName();
				assertEquals("error", name);
			}
		}
	}
	
	@Test(expected=Exception.class)
	public void compileShouldThrowExceptionOnInvalidXML() throws TransformationException, InvalidXMLException {
		Document doc = compiler.transform("<hei/>");
	}
	
	@Test(expected=Exception.class)
	public void compileShouldThrowExceptionOnInvalidXSLT() throws InvalidXMLException, TransformationException {
		compiler = new XSLTCompiler("<xslt/>");
		Document doc = compiler.transform("<hei/>");
	}
	
	

}
