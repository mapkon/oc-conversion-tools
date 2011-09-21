package org.openxdata.oc

import org.junit.Test
import org.openxdata.oc.convert.XSLTCompiler
import org.openxdata.oc.convert.exception.InvalidXMLException;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext.ExpectedTypeRootLoader;

class CompilerTest extends GroovyTestCase {

	def sampleXslt = """<?xml version="1.0" encoding="UTF-8"?>
							<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
							<xsl:output method="xml" />
							<xsl:template match="/">
							<goddag></goddag>
							</xsl:template>
						</xsl:transform>"""

	void testShouldNotReturnNullOnValidInput() {
		def compiler = new XSLTCompiler(sampleXslt)
		String doc = compiler.transform("<hei/>")
		def transformedDoc = new XmlSlurper().parseText(doc);

		assertNotNull(doc);
		
	}

	void testShouldThrowExceptionOnInvalidXML() {
		shouldFail(InvalidXMLException) {
			def compiler = new XSLTCompiler(sampleXslt)
			compiler.transform("<invalid--xml--!!>");
		}
	}

	void testShouldThrowExceptionOnInvalidXSLTXML(){
		shouldFail(InvalidXMLException) {
			def compiler = new XSLTCompiler("<xslt--dfdf?`/>");
			compiler = new XSLTCompiler("<xslt/>");
			compiler.transform("<hei/>");
		}
	}

	void testShouldThrowExceptionOnInvalidXSLT(){
		shouldFail(InvalidXMLException) {
			def compiler = new XSLTCompiler("<xslt/>");
			compiler = new XSLTCompiler("<xslt/>");
			compiler.transform("<hei/>");
		}
	}
	
	void testShouldCorrectlyTranslateDocument(){
		def compiler = new XSLTCompiler(sampleXslt)
		String doc = compiler.transform("<hei/>")
		def transformedDoc = new XmlSlurper().parseText(doc);
		assertNotNull transformedDoc.goddag
	}
}
