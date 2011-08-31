package org.openxdata.oc.convert;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
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

public class XSLTCompiler implements ErrorListener {

	private XsltExecutable executable;
	Processor processor;
	XsltCompiler compiler;

	public XSLTCompiler(String xslt) throws InvalidXMLException {
		processor = new Processor(false);
		compiler = processor.newXsltCompiler();
		compiler.setErrorListener(this);

		try {
			Document xsltDoc = XMLUtil.getDocument(xslt);
			executable = compiler.compile(new DOMSource(xsltDoc));

		} catch (SaxonApiException e) {
			throw new InvalidXMLException("The XSLT is not Valid", e);
		} catch (SAXException e) {
			throw new InvalidXMLException("The string is not a valid XML", e);
		}
	}

	public Document transform(String input) throws InvalidXMLException, TransformationException {
		try {
			XsltTransformer transformer = executable.load();
			transformer.setErrorListener(this);
			Document outputDocument = XMLUtil.createDocument();
			Document inputDocument = XMLUtil.getDocument(input);
			inputDocument.getFirstChild();
			DOMDestination destination = new DOMDestination(outputDocument);
			transformer.setDestination(destination);
			DOMSource inputSource = new DOMSource(inputDocument);
			transformer.setSource(inputSource);
		
			transformer.transform();

			return outputDocument;
		} catch (SAXException e) {
			throw new InvalidXMLException("The intput file is not valid XML", e);
		} catch (SaxonApiException e) {
			throw new TransformationException("A transformation error occured: likely a mismatch between xslt and input xml", e);
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
}
