package org.openxdata.oc.convert.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLUtilTest {
	
	@Test
	public void getDocumentShouldReturnCorrectDocument() throws SAXException,
			IOException {

		String file = XMLUtil.loadFile("/org/openxdata/oc/sample-output-xml.xml");
		Document doc = XMLUtil.getDocument(file);
		assertNotNull(doc);

		Node child = doc.getFirstChild();

		assertEquals("goddag", child.getNodeName());

	}
}
