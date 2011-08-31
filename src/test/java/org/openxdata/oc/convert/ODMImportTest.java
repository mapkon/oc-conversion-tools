package org.openxdata.oc.convert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.openxdata.oc.convert.exception.NoStudyDefinitionException;
import org.openxdata.oc.convert.util.XMLUtil;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class ODMImportTest {
	
	private String xml;
	private ODMImport importer = new ODMImport();
	
	@Test(expected=NoStudyDefinitionException.class)
	public void importStudyItemShouldReturnNullOnInvalidODMDile() throws SAXException, NoStudyDefinitionException{
		
		xml = "<invalid-odm-xml/>";
		importer.importStudyItem(xml);

	}
	
	@Test
	public void importStudyItemShouldReturnEditable() throws SAXException, IOException, NoStudyDefinitionException {
		
		Editable editable = importODMFile();
		assertTrue(editable instanceof Editable);
	}
	
	@Test
	public void importStudyItemShouldContainValidOXDFormatElement() throws IOException, SAXException, ParserConfigurationException, XPathExpressionException, NoStudyDefinitionException{
		Editable editable = importODMFile();
		Document doc = XMLUtil.getDocumentFromResource("/org/openxdata/oc/oc-odm.xml");
		StudyDef study = (StudyDef) editable;
		
		XPathEvaluator evaluator = new XPathEvaluator(doc);
		
		String studyName = study.getName();
		String result = (String) evaluator.evaluateXPath("//oc:Study/oc:GlobalVariables/oc:StudyName", XPathConstants.STRING);
		
		assertEquals(result, studyName);
		
		FormDef form = study.getForms().get(0);
		result = (String) evaluator.evaluateXPath("//oc:Study/oc:MetaDataVersion/oc:FormDef/@Name", XPathConstants.STRING);
		String formName = result.substring(0, result.lastIndexOf('-')-1);
		String versionName = result.substring(result.lastIndexOf('-')+2);
		
		assertEquals(formName, form.getName());
		
		FormDefVersion version = form.getDefaultVersion();
		
		Document outputXML = XMLUtil.getDocument(version.getXform());
		System.out.println(XMLUtil.format(outputXML));
		
		assertEquals(versionName, version.getName());
		
	}
	
	private Editable importODMFile() throws IOException, SAXException, NoStudyDefinitionException {
		xml = XMLUtil.loadFile("/org/openxdata/oc/oc-odm.xml");		
		return importer.importStudyItem(xml);
	}
	
	
}
