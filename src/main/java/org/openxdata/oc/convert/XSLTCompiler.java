package org.openxdata.oc.convert;

import javax.xml.transform.dom.DOMSource;

import net.sf.saxon.s9api.DOMDestination;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.openxdata.oc.convert.util.XMLUtil;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.sun.org.apache.xml.internal.security.transforms.TransformationException;

public class XSLTCompiler {

	private XsltExecutable executable;
	Processor processor;
	XsltCompiler compiler;

	public XSLTCompiler(String xslt) throws InvalidXMLException {
		processor = new Processor(false);
		compiler = processor.newXsltCompiler();

		try {
			Document xsltDoc = XMLUtil.getDocument(xslt);
			executable = compiler.compile(new DOMSource(xsltDoc));

		} catch (SaxonApiException e) {
			throw new InvalidXMLException("The XSLT is not Valid");
		} catch (SAXException e) {
			throw new InvalidXMLException("The string is not a valid XML");
		}
	}

	public Document transform(String input) throws InvalidXMLException, TransformationException {
		try {
			XsltTransformer transformer = executable.load();
			Document outputDocument = XMLUtil.createDocument();
			Document inputDocument = XMLUtil.getDocument(input);
			DOMDestination destination = new DOMDestination(outputDocument);
			transformer.setDestination(destination);
			DOMSource inputSource = new DOMSource(inputDocument);
			transformer.setSource(inputSource);
		
			transformer.transform();

			return outputDocument;
		} catch (SAXException e) {
			throw new InvalidXMLException("The intput file is not valid XML");
		} catch (SaxonApiException e) {
			throw new TransformationException("Something is wrong with the input xml");
		}
		
	}
}
