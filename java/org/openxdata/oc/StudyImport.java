package org.openxdata.oc;

import org.openxdata.server.admin.model.Editable;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.FormDefVersionText;
import org.openxdata.server.admin.model.StudyDef;

import org.openxdata.server.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This is a utility for importing studies, forms and form versions from xml files.
 */
public class StudyImport {

	/**
	 * Imports a study, form, or form version from xml.
	 * 
	 * @param xml
	 *            the xml text.
	 * @return study, form or form version, depending on what is in the xml text.
	 */
	public static Editable importStudyItem(String xml) {
		if (xml == null || xml.trim().length() == 0)
			return null;

		Editable editable = null;

		StudyDef studyDef = null;
		FormDef formDef = null;
		FormDefVersion formDefVersion = null;

		Document doc = null;

		try {
			doc = XmlUtil.fromString2Doc(xml);
		} catch (Exception ex) {
			return null;
		}

		Element root = doc.getDocumentElement();
		if (root != null) {

			String name = root.getAttribute("name");
			String description = root.getAttribute("description");
			String studyKey = root.getAttribute("studyKey");

			if (name == null || name.trim().length() == 0)
				return null;

			if (root.getNodeName().equalsIgnoreCase("study")) {
				studyDef = new StudyDef(0, name, description);
				studyDef.setStudyKey(studyKey);
				editable = studyDef;
			} else if (root.getNodeName().equalsIgnoreCase("form")) {
				formDef = new FormDef(0, name, description, studyDef);
				editable = formDef;
			} else if (root.getNodeName().equalsIgnoreCase("version")) {
				formDefVersion = new FormDefVersion(0, name, description, formDef);
				editable = formDefVersion;
			}

			NodeList formNodes = root.getChildNodes();
			for (int index = 0; index < formNodes.getLength(); index++) {
				Node node = formNodes.item(index);
				if (node.getNodeType() != Node.ELEMENT_NODE)
					continue;
				if (node.getNodeName().equalsIgnoreCase("form"))
					importForm(studyDef, (Element) node);
				else if (node.getNodeName().equalsIgnoreCase("version"))
					importFormVersion(formDef, (Element) node);
				else if (node.getNodeName().equalsIgnoreCase("versionText"))
					importFormVersionText(formDefVersion, (Element) node);
				else if (node.getNodeName().equalsIgnoreCase("xform"))
					formDefVersion.setXform(getTextValue((Element) node));
				else if (node.getNodeName().equalsIgnoreCase("layout"))
					formDefVersion.setLayout(getTextValue((Element) node));
			}
		}

		return editable;
	}

	/**
	 * Imports a form definition from an xml node and adds it to a study definition object.
	 * 
	 * @param studyDef
	 *            the study definition object.
	 * @param formNode
	 *            the xml node containing the form definition.
	 */
	private static void importForm(StudyDef studyDef, Element formNode) {
		FormDef formDef = new FormDef(0, formNode.getAttribute("name"), formNode.getAttribute("description"), studyDef);
		studyDef.addForm(formDef);

		NodeList versionNodes = formNode.getChildNodes();
		for (int index = 0; index < versionNodes.getLength(); index++) {
			Node node = versionNodes.item(index);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			else if (node.getNodeName().equalsIgnoreCase("version"))
				importFormVersion(formDef, (Element) node);
		}
	}

	/**
	 * Imports a form version from an xml node and adds it to a form definition object.
	 * 
	 * @param formDef
	 *            the form definition object.
	 * @param versionNode
	 *            the xml node containing the form version.
	 */
	private static void importFormVersion(FormDef formDef, Element versionNode) {
		FormDefVersion formDefVersion = new FormDefVersion(0, versionNode.getAttribute("name"), versionNode
				.getAttribute("description"), formDef);
		formDef.addVersion(formDefVersion);

		NodeList versionTextNodes = versionNode.getChildNodes();
		for (int index = 0; index < versionTextNodes.getLength(); index++) {
			Node node = versionTextNodes.item(index);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			else if (node.getNodeName().equalsIgnoreCase("xform"))
				formDefVersion.setXform(getTextValue((Element) node));
			else if (node.getNodeName().equalsIgnoreCase("layout"))
				formDefVersion.setLayout(getTextValue((Element) node));
			else if (node.getNodeName().equalsIgnoreCase("versionText"))
				importFormVersionText(formDefVersion, (Element) node);
		}
	}

	/**
	 * Imports form version text from an xml node and adds it to a form version object.
	 * 
	 * @param formDefVersion
	 *            the form version object.
	 * @param versionTextNode
	 *            the xml node containing form version text.
	 */
	private static void importFormVersionText(FormDefVersion formDefVersion, Element versionTextNode) {
		FormDefVersionText formDefVersionText = new FormDefVersionText();
		formDefVersion.addVersionText(formDefVersionText);

		formDefVersionText.setLocaleKey(versionTextNode.getAttribute("locale"));

		NodeList nodes = versionTextNode.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			Node node = nodes.item(index);
			if (node.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if (node.getNodeName().equalsIgnoreCase("xform"))
				formDefVersionText.setXformText(getTextValue((Element) node));
			else if (node.getNodeName().equalsIgnoreCase("layout"))
				formDefVersionText.setLayoutText(getTextValue((Element) node));
		}
	}

	public static String getTextValue(Node node) {
		int numOfEntries = node.getChildNodes().getLength();
		for (int i = 0; i < numOfEntries; i++) {
			if (node.getChildNodes().item(i).getNodeType() == Node.TEXT_NODE) {

				//These iterations are for particularly firefox which when comes accross
				//text bigger than 4096, splits it into multiple adjacent text nodes
				//each not exceeding the maximum 4096. This is as of 04/04/2009
				//and for Firefox version 3.0.8
				String s = "";

				for (int index = i; index < numOfEntries; index++) {
					Node currentNode = node.getChildNodes().item(index);
					String value = currentNode.getNodeValue();
					if (currentNode.getNodeType() == Node.TEXT_NODE && value != null)
						s += value;
					else
						break;
				}

				return s;
				//return node.getChildNodes().item(i).getNodeValue();
			}

			if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				String val = getTextValue((Element) node.getChildNodes().item(i));
				if (val != null)
					return val;
			}
		}

		return null;
	}
}
