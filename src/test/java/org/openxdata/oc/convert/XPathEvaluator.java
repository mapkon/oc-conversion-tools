package org.openxdata.oc.convert;

import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

public class XPathEvaluator {
	
	Document doc;
	XPath xpath;
	
	public XPathEvaluator(Document doc) {
		this.doc = doc;
		XPathFactory xpathFactory = XPathFactory.newInstance();
		this.xpath = xpathFactory.newXPath();
		xpath.setNamespaceContext(new OpenClinicaNamespaceContext());
	}
	
	public Object evaluateXPath(String path, QName constant) throws XPathExpressionException{
		XPathExpression expr = xpath.compile(path);
		Object result = expr.evaluate(doc, constant);
		
		return result;
	}
	
	class OpenClinicaNamespaceContext implements NamespaceContext {

	    public String getNamespaceURI(String prefix) {
	        if (prefix == null) throw new NullPointerException("Null prefix");
	        else if ("oc".equals(prefix)) return "http://www.cdisc.org/ns/odm/v1.3";
	        return XMLConstants.NULL_NS_URI;
	    }

	    // This method isn't necessary for XPath processing.
	    public String getPrefix(String uri) {
	        throw new UnsupportedOperationException();
	    }

	    // This method isn't necessary for XPath processing either.
	    public Iterator<?> getPrefixes(String uri) {
	        throw new UnsupportedOperationException();
	    }

	}

}
