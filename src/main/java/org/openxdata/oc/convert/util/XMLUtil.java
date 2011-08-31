package org.openxdata.oc.convert.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * Utility methods used when manipulating xml documents.
 * 
 */
public class XMLUtil {

	/**
	 * All methods in this class are static and hence we expect no external
	 * Instantiation of this class.
	 */
	private XMLUtil() {

	}
	
	public static String format(Document document) {
        try {
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Checks if a node name equals a particular name, regardless of prefix.
	 * NOTE: If checking names containing subsets of others using case or if
	 * else statements, start with one which is not contained by others. e.g
	 * "itemset" should be before "item".
	 * 
	 * @param nodeName
	 *            the node name.
	 * @param name
	 *            the name to compare with.
	 * @return true if they are the same, else false.
	 */
	public static boolean nodeNameEquals(String nodeName, String name) {
		return nodeName.equals(name) || nodeName.contains(":" + name);

	}

	/**
	 * Gets the text value of a node.
	 * 
	 * @param node
	 *            the node whose text value to get.
	 * @return the text value.
	 */
	public static String getTextValue(Node node) {
		int numOfEntries = node.getChildNodes().getLength();
		for (int i = 0; i < numOfEntries; i++) {
			if (node.getChildNodes().item(i).getNodeType() == Node.TEXT_NODE) {

				// These iterations are for particularly firefox which when
				// comes accross
				// text bigger than 4096, splits it into multiple adjacent text
				// nodes
				// each not exceeding the maximum 4096. This is as of 04/04/2009
				// and for Firefox version 3.0.8
				String s = "";

				for (int index = i; index < numOfEntries; index++) {
					Node currentNode = node.getChildNodes().item(index);
					String value = currentNode.getNodeValue();
					if (currentNode.getNodeType() == Node.TEXT_NODE
							&& value != null)
						s += value;
					else
						break;
				}

				return s;
			}

			if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				String val = getTextValue((Element) node.getChildNodes()
						.item(i));
				if (val != null)
					return val;
			}
		}

		return null;
	}

	/**
	 * Sets the text value of a node.
	 * 
	 * @param node
	 *            the node whose text value to set.
	 * @param value
	 *            the text value.
	 * @return true if the value was set successfully, else false.
	 */
	public static boolean setTextNodeValue(Element node, String value) {
		if (node == null)
			return false;

		int numOfEntries = node.getChildNodes().getLength();
		for (int i = 0; i < numOfEntries; i++) {
			if (node.getChildNodes().item(i).getNodeType() == Node.TEXT_NODE) {
				node.getChildNodes().item(i).setNodeValue(value);
				return true;
			}

			if (node.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {
				if (setTextNodeValue((Element) node.getChildNodes().item(i),
						value))
					return true;
			}
		}

		if (numOfEntries == 0) {
			node.appendChild(node.getOwnerDocument().createTextNode(value));
			return true;
		}

		return false;
	}

	/**
	 * Gets a node name without the namespace prefix.
	 * 
	 * @param element
	 *            the node.
	 * @return the name.
	 */
	public static String getNodeName(Element element) {
		String name = element.getNodeName();
		String prefix = element.getPrefix();
		if (prefix != null) {
			if (name.startsWith(prefix))
				name = name.replace(prefix + ":", "");
		}
		return name;
	}

	/**
	 * Gets a child element of a parent node with a given name.
	 * 
	 * @param parent
	 *            - the parent element
	 * @param name
	 *            - the name of the child.
	 * @return - the child element.
	 */
	public static Element getNode(Element parent, String name) {
		if (parent == null)
			return null;

		for (int i = 0; i < parent.getChildNodes().getLength(); i++) {
			if (parent.getChildNodes().item(i).getNodeType() != Node.ELEMENT_NODE)
				continue;

			Element child = (Element) parent.getChildNodes().item(i);
			if (XMLUtil.getNodeName(child).equals(name))
				return child;
			else if (name.contains("/")) {
				String parentName = name.substring(0, name.indexOf('/'));
				if (XMLUtil.getNodeName(child).equals(parentName)) {
					child = getNode(child,
							name.substring(name.indexOf('/') + 1));
					if (child != null)
						return child;
				}
			}

			child = getNode(child, name);
			if (child != null)
				return child;
		}

		return null;
	}

	/**
	 * Gets the text value of a node with a given name.
	 * 
	 * @param parentNode
	 *            the parent node of the node whose text value we are to get.
	 *            this node is normally an xforms instance data node.
	 * @param name
	 *            the name of the node.
	 * @return the node text value.
	 */
	public static String getNodeTextValue(Element parentNode, String name) {
		Element node = XMLUtil.getNode(parentNode, name);
		if (node != null)
			return XMLUtil.getTextValue(node);
		return null;
	}

	/**
	 * Creates an xml document object from its text xml.
	 * 
	 * @param xml
	 *            the text xml.
	 * @return the document object.
	 * @throws SAXException 
	 */
	public static Document getDocument(String xml) throws SAXException {
		InputSource input = new InputSource(new StringReader(xml));
		return getDocument(input);
	}

	private static Document getDocument(InputSource input) throws SAXException {
		DOMParser parser = new DOMParser();
		try {
			parser.parse(input);
		} catch (IOException e) {
			
			// Since we reading a string, IOException is highly unlikely.
			throw new RuntimeException("An unexpected IOException occurred.");
		}
		
		Document doc = parser.getDocument();
		return doc;
	}
	
	public static Document getDocumentFromResource(String file) throws FileNotFoundException, SAXException {
		InputStream stream = XMLUtil.class.getResourceAsStream(file);
		InputSource input = new InputSource(new InputStreamReader(stream));
		return getDocument(input);
	}

	/**
	 * Converts an xml document to a string.
	 * 
	 * @param doc
	 *            the document.
	 * @return the xml string.
	 */
	public static String fromDoc2String(Document doc) {
		return format(doc);
	}

	/**
	 * Gets the next sibling of a node whose type is Node.ELEMENT_NODE
	 * 
	 * @param node
	 *            the node whose next sibling element to get.
	 * @return the next sibling element.
	 */
	public static Element getNextElementSibling(Element node) {
		Node sibling = node.getNextSibling();
		while (sibling != null) {
			if (sibling.getNodeType() == Node.ELEMENT_NODE)
				return (Element) sibling;
			sibling = sibling.getNextSibling();
		}

		return node;
	}

	public static Node getChildElement(Node node) {
		NodeList nodes = node.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			Node child = nodes.item(index);
			if (child.getNodeType() == Node.ELEMENT_NODE)
				return child;
		}

		return node;
	}

	public static Node getChildCDATA(Node node) {
		NodeList nodes = node.getChildNodes();
		for (int index = 0; index < nodes.getLength(); index++) {
			Node child = nodes.item(index);
			if (child.getNodeType() == Node.CDATA_SECTION_NODE)
				return child;
		}

		return node;
	}
	
	public static Document createDocument(){
		DOMImplementation impl = DOMImplementationImpl.getDOMImplementation();

		Document doc = impl.createDocument(null, null, null);

		return doc;
	}
	

	public static String loadFile(String file) throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(
			    XMLUtil.class.getResourceAsStream(file)));

		StringBuilder xsltContent = new StringBuilder();
		String line;
		while((line = input.readLine()) != null){
			xsltContent.append(line + "\n");
		}
		return xsltContent.toString();
	}
}
