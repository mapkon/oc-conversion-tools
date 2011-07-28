package org.openxdata.oc.convert;

import java.util.Date;

import org.openxdata.oc.convert.util.XformConstants;
import org.openxdata.oc.convert.util.XmlUtil;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


public class ODMXformImport {

	private static int groupId = 0;
		
	public static String importXform(Element node, Element metaDataVersionNode){

		groupId = 0;
		
		// Create a new document.
		Document doc = XMLParser.createDocument();

		// Create the document root node.
		Element xformsNode = doc.createElement(XformConstants.NODE_NAME_XFORMS);

		// Set the xf and xsd prefix values and then add the root node to the document. 
		xformsNode.setAttribute(XformConstants.XML_NAMESPACE_PREFIX+XformConstants.PREFIX_XFORMS, XformConstants.NAMESPACE_XFORMS);
		xformsNode.setAttribute(XformConstants.XML_NAMESPACE_PREFIX+XformConstants.PREFIX_XML_SCHEMA, XformConstants.NAMESPACE_XML_SCHEMA);
		doc.appendChild(xformsNode);

		// Create the xforms model node and add it to the root node.
		Element modelNode =  doc.createElement(XformConstants.NODE_NAME_MODEL);
		xformsNode.appendChild(modelNode);

		// Create the instance node and add it to the model node.
		Element instanceNode =  doc.createElement(XformConstants.NODE_NAME_INSTANCE);
		instanceNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, "ODM");
		modelNode.appendChild(instanceNode);

		// Create the form data node and add it to the instance node.
		Element odmNode =  doc.createElement("ODM");
		odmNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NAME, node.getAttribute("Name"));
		
		odmNode.setAttribute(XformConstants.ATTRIBUTE_NAME_FORM_KEY, node.getAttribute("OID"));
		instanceNode.appendChild(odmNode);

		odmNode.setAttribute("xmlns", "http://www.cdisc.org/ns/odm/v1.3");
		odmNode.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		odmNode.setAttribute("xsi:schemaLocation", "http://www.cdisc.org/ns/odm/v1.3 ODM1-3.xsd");

		odmNode.setAttribute("ODMVersion", "1.3");
		odmNode.setAttribute("FileOID", new Date().toString());
		odmNode.setAttribute("FileType", "Snapshot");
		odmNode.setAttribute("Description", "");
		odmNode.setAttribute("CreationDateTime", new Date().toString());

		Element clinicalDataNode =  doc.createElement("ClinicalData");
		clinicalDataNode.setAttribute("StudyOID", "");
		clinicalDataNode.setAttribute("MetaDataVersionOID", "v1.0.0");
		clinicalDataNode.setAttribute("UserID", "");
		odmNode.appendChild(clinicalDataNode);

		Element subjectDataNode =  doc.createElement("SubjectData");
		subjectDataNode.setAttribute("SubjectKey", "");
		clinicalDataNode.appendChild(subjectDataNode);

		Element studyEventDataNode =  doc.createElement("StudyEventData");
		studyEventDataNode.setAttribute("StudyEventOID", "");
		subjectDataNode.appendChild(studyEventDataNode);

		Element formDataNode =  doc.createElement("FormData");
		formDataNode.setAttribute("FormOID", node.getAttribute("OID"));
		studyEventDataNode.appendChild(formDataNode);

		NodeList nodes = node.getElementsByTagName("ItemGroupDef");
		for(int index = 0; index < nodes.getLength(); index++)
			importItemGroupDef(doc,(Element)nodes.item(index),formDataNode,metaDataVersionNode,xformsNode,modelNode);

		nodes = node.getElementsByTagName("ItemGroupRef");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			importItemGroupRef(doc,element,formDataNode,metaDataVersionNode,element.getAttribute("ItemGroupOID"),xformsNode, modelNode);
		}

		return XmlUtil.fromDoc2String(doc);
	}

	private static void importItemGroupDef(Document doc, Element node, Element formDataNode, Element metaDataVersionNode,Element xformsNode, Element modelNode){
		Element itemGroupDataNode =  doc.createElement("ItemGroupData");
		itemGroupDataNode.setAttribute("ItemGroupOID", node.getAttribute("OID"));
		itemGroupDataNode.setAttribute("TransactionType", "Insert");
		formDataNode.appendChild(itemGroupDataNode);

		Element groupNode =  doc.createElement(XformConstants.NODE_NAME_GROUP);
		groupNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, ++groupId+"");
		xformsNode.appendChild(groupNode);
		
		if(groupId == 1){
			
			// StudyOID
			Element bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, "StudyOID");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, "/ODM/ClinicalData/@StudyOID");
			bindNode.setAttribute("type", "xsd:string");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
			modelNode.appendChild(bindNode);
			
			Element widgetNode =  doc.createElement(XformConstants.NODE_NAME_INPUT);
			widgetNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, "StudyOID");
			groupNode.appendChild(widgetNode);
			
			Element labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
			labelNode.appendChild(doc.createTextNode("StudyOID"));
			widgetNode.appendChild(labelNode);
			
			
			// SubjectKey
			bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, "SubjectKey");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, "/ODM/ClinicalData/SubjectData/@SubjectKey");
			bindNode.setAttribute("type", "xsd:string");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
			modelNode.appendChild(bindNode);
			
			widgetNode =  doc.createElement(XformConstants.NODE_NAME_INPUT);
			widgetNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, "SubjectKey");
			groupNode.appendChild(widgetNode);
			
			labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
			labelNode.appendChild(doc.createTextNode("SubjectKey"));
			widgetNode.appendChild(labelNode);
			
			
			// StudyEventData
			bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, "StudyEventData");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, "/ODM/ClinicalData/SubjectData/StudyEventData/@StudyEventOID");
			bindNode.setAttribute("type", "xsd:string");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
			modelNode.appendChild(bindNode);
			
			widgetNode =  doc.createElement(XformConstants.NODE_NAME_INPUT);
			widgetNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, "StudyEventData");
			groupNode.appendChild(widgetNode);
			
			labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
			labelNode.appendChild(doc.createTextNode("StudyEventData"));
			widgetNode.appendChild(labelNode);
			
			
			// UserID
			bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, "UserID");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, "/ODM/ClinicalData/@UserID");
			bindNode.setAttribute("type", "xsd:int");
			bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_REQUIRED, XformConstants.XPATH_VALUE_TRUE);
			modelNode.appendChild(bindNode);
			
			widgetNode =  doc.createElement(XformConstants.NODE_NAME_INPUT);
			widgetNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, "UserID");
			groupNode.appendChild(widgetNode);
			
			labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
			labelNode.appendChild(doc.createTextNode("UserID"));
			widgetNode.appendChild(labelNode);
		}
		
		NodeList nodes = node.getElementsByTagName("ItemDef");
		for(int index = 0; index < nodes.getLength(); index++)
			importItemDef(doc,(Element)nodes.item(index),itemGroupDataNode,formDataNode,metaDataVersionNode, modelNode,groupNode);

		nodes = node.getElementsByTagName("ItemRef");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			importItemRef(doc,element,itemGroupDataNode,formDataNode,metaDataVersionNode,element.getAttribute("ItemOID"), modelNode, groupNode);
		}
	}

	private static void importItemGroupRef(Document doc, Element node, Element formDataNode, Element metaDataVersionNode, String OID,Element xformsNode, Element modelNode){
		NodeList nodes = metaDataVersionNode.getElementsByTagName("ItemGroupDef");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			if(OID.equalsIgnoreCase(element.getAttribute("OID"))){
				importItemGroupDef(doc,element,formDataNode,metaDataVersionNode,xformsNode, modelNode);
				return;
			}
		}
	}

	private static void importItemRef(Document doc, Element node, Element itemGroupDataNode, Element formDataNode, Element metaDataVersionNode, String OID, Element modelNode, Element groupNode){
		NodeList nodes = metaDataVersionNode.getElementsByTagName("ItemDef");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			if(OID.equalsIgnoreCase(element.getAttribute("OID"))){
				importItemDef(doc,element,itemGroupDataNode,formDataNode,metaDataVersionNode, modelNode, groupNode);
				return;
			}
		}
	}

	private static void importItemDef(Document doc, Element node, Element itemGroupDataNode, Element formDataNode, Element metaDataVersionNode, Element modelNode, Element groupNode){
		String OID = node.getAttribute("OID");
		
		Element itemDataNode =  doc.createElement("ItemData");
		itemDataNode.setAttribute("ItemOID", OID);
		itemDataNode.setAttribute("Value", "");
		itemGroupDataNode.appendChild(itemDataNode);
		
		Element bindNode =  doc.createElement(XformConstants.NODE_NAME_BIND);
		bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, OID);
		bindNode.setAttribute(XformConstants.ATTRIBUTE_NAME_NODESET, "/ODM/ClinicalData/SubjectData/StudyEventData/FormData/ItemGroupData/ItemData[@ItemOID='" + OID + "']/@Value");
		bindNode.setAttribute("type", fromOdm2XformDataType(node.getAttribute("DataType")));
		modelNode.appendChild(bindNode);
		
		boolean isCodeListRef = false;
		NodeList nodes = node.getElementsByTagName("CodeListRef");
		if(nodes == null || nodes.getLength() == 0)
			nodes = node.getElementsByTagName("CodeList");
		else
			isCodeListRef = true;
		
		Element widgetNode =  doc.createElement((nodes == null || nodes.getLength() == 0) ? XformConstants.NODE_NAME_INPUT : XformConstants.NODE_NAME_SELECT1);
		widgetNode.setAttribute(XformConstants.ATTRIBUTE_NAME_BIND, OID);
		groupNode.appendChild(widgetNode);
		
		String text = XmlUtil.getTextValue((Element)node.getElementsByTagName("TranslatedText").item(0));
		Element labelNode =  doc.createElement(XformConstants.NODE_NAME_LABEL);
		labelNode.appendChild(doc.createTextNode(text.trim()));
		widgetNode.appendChild(labelNode);
		
		if(nodes != null && nodes.getLength() > 0){
			Element element = (Element)nodes.item(0);
			if(isCodeListRef)
				inmportCodeListRef(doc,widgetNode,metaDataVersionNode,element.getAttribute("CodeListOID"));
			else
				inmportCodeListDef(doc,element, widgetNode);
		}
	}
	
	private static void inmportCodeListDef(Document doc,Element node, Element widgetNode){
		NodeList nodes = node.getElementsByTagName("CodeListItem");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			String value = element.getAttribute("CodedValue");
			
			Element itemNode = doc.createElement(XformConstants.NODE_NAME_ITEM);
			itemNode.setAttribute(XformConstants.ATTRIBUTE_NAME_ID, value);
			widgetNode.appendChild(itemNode);
			
			Element labelNode = doc.createElement(XformConstants.NODE_NAME_LABEL);
			labelNode.appendChild(doc.createTextNode(getTranslatedText(element).trim()));
			itemNode.appendChild(labelNode);
			
			Element valueNode = doc.createElement(XformConstants.NODE_NAME_VALUE);
			valueNode.appendChild(doc.createTextNode(value));
			itemNode.appendChild(valueNode);
		}
	}
	
	private static String getTranslatedText(Element node){
		NodeList nodes = node.getElementsByTagName("TranslatedText");
		if(nodes != null && nodes.getLength() > 0)
			return XmlUtil.getTextValue((Element)nodes.item(0));
		return "";
	}
	
	private static void inmportCodeListRef(Document doc, Element widgetNode, Element metaDataVersionNode, String OID){
		NodeList nodes = metaDataVersionNode.getElementsByTagName("CodeList");
		for(int index = 0; index < nodes.getLength(); index++){
			Element element = (Element)nodes.item(index);
			if(OID.equalsIgnoreCase(element.getAttribute("OID"))){
				inmportCodeListDef(doc,element,widgetNode);
				return;
			}
		}
	}
	
	private static String fromOdm2XformDataType(String dataType){
		if(dataType.equalsIgnoreCase("integer"))
			return "xsd:int";
		else if(dataType.equalsIgnoreCase("float"))
			return "xsd:decimal";
		else if(dataType.equalsIgnoreCase("date"))
			return "xsd:date";
		else if(dataType.equalsIgnoreCase("time"))
			return "xsd:time";
		else if(dataType.equalsIgnoreCase("datetime"))
			return "xsd:dateTime";
		else if(dataType.equalsIgnoreCase("boolean"))
			return "xsd:boolean";
		else if(dataType.equalsIgnoreCase("double"))
			return "xsd:decimal";
		else if(dataType.equalsIgnoreCase("base64Binary"))
			return "xsd:base64Binary";
		
		return "xsd:string"; //text,string, etc
	}
}
