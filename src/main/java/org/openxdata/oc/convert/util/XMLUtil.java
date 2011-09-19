package org.openxdata.oc.convert.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
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
