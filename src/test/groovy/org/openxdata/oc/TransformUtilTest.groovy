package org.openxdata.oc

import org.apache.commons.collections.CollectionUtils
import org.junit.Test


class TransformUtilTest extends GroovyTestCase {
	
	def odmFileContent
	
	def xformWithDuplicateBindings
	def xformWithNoDuplicateBindings
	
	def util = new TransformUtil()
	
	public void setUp() {
		
		odmFileContent = util.loadFileContents("test-odm.xml")
		xformWithDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-duplicate-bindings.xml"))
		xformWithNoDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-no-duplicate-bindings.xml"))
	}
	
	@Test void testLoadFileContents(){
				
		assertTrue odmFileContent.contains("<ODM")
		assertTrue odmFileContent.endsWith("</ODM>")
	}
	
	@Test void testLoadFileContentsStartWithXmlProcessingInstructions(){
		assertTrue odmFileContent.startsWith("""<?xml version="1.0" encoding="UTF-8"?>""")
	}
	
	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFileContents("")
		}
	}
	
	@Test void testLoadFileContentsRendersCorrectMessageOnEmptyFileName(){
		try{
			util.loadFileContents("")
		}catch(def ex){
			assertEquals 'File name cannot be null or empty.', ex.getMessage()
		}
	}
	
	@Test void testHasDuplicateBindings(){
		
		assertTrue util.hasDuplicateBindings(xformWithDuplicateBindings)
		
		// Triangulating
		assertFalse util.hasDuplicateBindings(xformWithNoDuplicateBindings)
	}
	
	@Test void testGetSimilarBindingsMUSTReturnCorrectSizeOfDuplicateBindings(){
		
		def duplicateBindings = util.getDuplicateBindings(xformWithDuplicateBindings)
		assertEquals 386, duplicateBindings.size()
	}
}
