package org.openxdata.oc.client.convert;

import org.openxdata.oc.client.convert.util.XmlUtil;
import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

/**
 * Imports studies in ODM format.
 */
public class ODMImport {
		
	/**
	 * Imports a study, form, or form version from xml.
	 * 
	 * @param doc Document containing xml from odm file.
	 * @return study, form or form version, depending on what is in the xml text.
	 */
	public static Editable importStudyItem(Document doc){
		Element root = doc.getDocumentElement();
		NodeList nodes = root.getElementsByTagName("Study");
		if(nodes == null || nodes.getLength() == 0)
			return null;
		
		StudyDef studyDef = new StudyDef();
		Element studyNode = (Element)nodes.item(0);
		studyDef.setStudyKey(studyNode.getAttribute("OID"));
		
		nodes = root.getElementsByTagName("StudyName");
		studyDef.setName(XmlUtil.getTextValue((Element)nodes.item(0)));
		
		nodes = root.getElementsByTagName("StudyDescription");
		studyDef.setDescription(XmlUtil.getTextValue((Element)nodes.item(0)));
		
		Element metaDataVersionNode = (Element)root.getElementsByTagName("MetaDataVersion").item(0);
		
		nodes = root.getElementsByTagName("FormDef");
		for(int index = 0; index < nodes.getLength(); index++)
			importFormItem((Element)nodes.item(index),studyDef,metaDataVersionNode);
		
		return studyDef;
	}
	
	private static void importFormItem(Element node, StudyDef studyDef, Element metaDataVersionNode){
		String name = node.getAttribute("Name");
		
		int pos = name.lastIndexOf("-");
		FormDef formDef = new FormDef(0,name.substring(0, pos - 1),studyDef);
		studyDef.addForm(formDef);
		
		FormDefVersion formDefVersion = new FormDefVersion(0,name.substring(pos + 2),formDef);
		formDef.addVersion(formDefVersion);
		
		formDefVersion.setXform(ODMXformImport.importXform(node, metaDataVersionNode));
		
	}
}
