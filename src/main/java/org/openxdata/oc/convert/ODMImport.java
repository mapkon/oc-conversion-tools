package org.openxdata.oc.convert;

import org.openxdata.oc.convert.util.XMLUtil;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Handles the importing of XML from studies in ODM format (openclinica studies).
 * A full compatible openxdata object is constructed.
 */
public class ODMImport {
		
	/**
	 * Imports a study, form, or form version from xml.
	 * 
	 * @param xml String containing xml from odm file.
	 * @return study, form or form version, depending on what is in the xml text.
	 * 
	 * @throws SAXException 
	 */
	public Editable importStudyItem(String xml) throws SAXException {
		
		Document doc = XMLUtil.getDocument(xml);
		Element root = doc.getDocumentElement();
		
		NodeList nodes = root.getElementsByTagName("Study");
		
		if(nodes == null || nodes.getLength() == 0)
			return null;
		
		StudyDef studyDef = new StudyDef();
		Element studyNode = (Element)nodes.item(0);
		studyDef.setStudyKey(studyNode.getAttribute("OID"));
		
		nodes = root.getElementsByTagName("StudyName");
		studyDef.setName(XMLUtil.getTextValue((Element)nodes.item(0)));
		
		nodes = root.getElementsByTagName("StudyDescription");
		studyDef.setDescription(XMLUtil.getTextValue((Element)nodes.item(0)));
		
		Element metaDataVersionNode = (Element)root.getElementsByTagName("MetaDataVersion").item(0);
		
		nodes = root.getElementsByTagName("FormDef");
		for(int index = 0; index < nodes.getLength(); index++)
			importFormItem((Element)nodes.item(index),studyDef,metaDataVersionNode);
		
		return studyDef;
	}
	
	private void importFormItem(Element node, StudyDef studyDef, Element metaDataVersionNode){
		String name = node.getAttribute("Name");
		
		int pos = name.lastIndexOf("-");
		FormDef formDef = new FormDef(0,name.substring(0, pos - 1),studyDef);
		studyDef.addForm(formDef);
		
		FormDefVersion formDefVersion = new FormDefVersion(0,name.substring(pos + 2),formDef);
		formDef.addVersion(formDefVersion);
		
		formDefVersion.setXform(ODMXformImport.importXform(node, metaDataVersionNode));
		
	}
}
