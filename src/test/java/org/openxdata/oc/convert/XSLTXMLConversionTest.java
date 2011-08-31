package org.openxdata.oc.convert;

import java.io.IOException;

import org.junit.Test;
import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.openxdata.oc.convert.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;

public class XSLTXMLConversionTest {
	
	@Test
	public void test() throws IOException, InvalidXMLException, TransformationException {
		Init.init();
		String xslt = XMLUtil.loadFile("/org/openxdata/oc/oc-oxd.xsl");
		XSLTCompiler compiler = new XSLTCompiler(xslt);		
		String odm = XMLUtil.loadFile("/org/openxdata/oc/oc-odm.xml");
		Document doc = compiler.transform(odm);
		removeNamedAttribute(doc, "study",  "xmlns:OpenClinica");
		removeNamedAttribute(doc, "study",  "xmlns:oc");
		System.out.println(XMLUtil.format(doc));
	}

	private void removeNamedAttribute(Document doc, String tag, String xmlns) {
		NodeList studyDefList = doc.getElementsByTagName(tag);
		Node studyDef = studyDefList.item(0);
		studyDef.getAttributes().removeNamedItem(xmlns);
	}
	

}
