package org.openxdata.oc

import groovy.xml.XmlUtil;

import org.junit.Test


class TransformUtilTest extends GroovyTestCase {
	
	def odmFile
	def transformFile
	def odmFileContent
	def xformWithDuplicateBindings
		
	def util = new TransformUtil()
	
	public void setUp() {
		
		odmFile = util.loadFile("test-odm.xml")
		transformFile = util.loadFile("transform-v0.1.xsl") 
		odmFileContent = util.loadFileContents("test-odm.xml")
		xformWithDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-duplicate-bindings.xml"))
	}
	
	@Test void testLoadFile(){
		assertNotNull odmFile
		assertEquals "test-odm.xml", odmFile.getName()
		
		assertNotNull transformFile
		assertEquals "transform-v0.1.xsl", transformFile.getName()		
		
	}
	
	@Test void testLoadFileMUSTThrowExceptionOnNullOrEmptyFileName() {
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFile("")
		}
	}
	
	@Test void testLoadFileContents(){
				
		assertTrue odmFileContent.contains("<ODM")
		assertTrue odmFileContent.endsWith("</ODM>")
		assertTrue odmFileContent.startsWith("""<?xml version="1.0" encoding="UTF-8"?>""")
	}
	
	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFileContents("")
		}
	}
	
	@Test void testHasDuplicateBindings(){
		
		assertTrue util.hasDuplicateBindings(xformWithDuplicateBindings)
		
		// Triangulating
		def xformWithNoDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-no-duplicate-bindings.xml"))
		assertFalse util.hasDuplicateBindings(xformWithNoDuplicateBindings)
	}
	
	@Test void testGetSimilarBindingsMUSTReturnCorrectSizeOfDuplicateBindings(){
		
		def bindings = util.getSimilarBindings(xformWithDuplicateBindings)
		assertEquals 386, bindings.size()
	}
}
