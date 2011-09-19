package org.openxdata.oc.convert;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Destination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XSLTCompiler implements ErrorListener {

	private Processor processor;
	private XsltCompiler compiler;
	
	private XsltExecutable executable;
	private CompilerUtils utils = new CompilerUtils();

	public XSLTCompiler(String xslt) throws InvalidXMLException {
		processor = new Processor(false);
		compiler = processor.newXsltCompiler();
		compiler.setErrorListener(this);

		try {
			Document xsltDoc = utils.getDocument(xslt);
			executable = compiler.compile(new DOMSource(xsltDoc));

		} catch (SaxonApiException e) {
			throw new InvalidXMLException("The XSLT is not Valid", e);
		} catch (SAXException e) {
			throw new InvalidXMLException("The string is not a valid XML", e);
		}
	}

	public String transform(String input) throws InvalidXMLException,
			TransformationException {
		try {
			XsltTransformer transformer = executable.load();
			transformer.setErrorListener(this);
			Document outputDocument = utils.createDocument();
			Document inputDocument = utils.getDocument(input);
			inputDocument.getFirstChild();
			Destination destination = new DOMDestination(outputDocument);
			transformer.setDestination(destination);
			DOMSource inputSource = new DOMSource(inputDocument);
			transformer.setSource(inputSource);

			transformer.transform();

			return utils.format(outputDocument);
		} catch (SAXException e) {
			throw new InvalidXMLException("The intput file is not valid XML", e);
		} catch (SaxonApiException e) {
			throw new TransformationException(
					"A transformation error occured: likely a mismatch between xslt and input xml", e);
		}
	}

	@Override
	public void warning(TransformerException exception)
			throws TransformerException {
		System.err.println(exception);
	}

	@Override
	public void error(TransformerException exception)
			throws TransformerException {
		System.err.println(exception);
	}

	@Override
	public void fatalError(TransformerException exception)
			throws TransformerException {
		System.err.println(exception);
	}
	
	private class CompilerUtils{
		
		Document createDocument(){
			
			DOMImplementation impl = DOMImplementationImpl.getDOMImplementation();
			Document doc = impl.createDocument(null, null, null);

			return doc;
		}
		
		Document getDocument(String xml) throws SAXException {
			InputSource input = new InputSource(new StringReader(xml));
			return getDocument(input);
		}

		Document getDocument(InputSource input) throws SAXException {
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
		
		String format(Document document) {
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
	}
}
