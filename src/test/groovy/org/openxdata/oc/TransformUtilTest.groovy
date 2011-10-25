package org.openxdata.oc

import org.junit.Test


class TransformUtilTest extends GroovyTestCase {
	
	def util = new TransformUtil()
	
	@Test void testLoadFile(){
		def file = util.loadFile("transform-v0.1.xsl") 
		
		assertNotNull file
		assertEquals "transform-v0.1.xsl", file.getName()
		
		def file2 = util.loadFile("test-odm.xml")
		
		assertNotNull file2
		assertEquals "test-odm.xml", file2.getName()
		
	}
	
	@Test void testLoadFileMUSTThrowExceptionOnNullOrEmptyFileName() {
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFile("")
		}
	}
	
	@Test void testLoadFileContents(){
		
		def fileContents = util.loadFileContents("test-odm.xml")
		
		assertTrue fileContents.contains("<ODM")
		assertTrue fileContents.endsWith("</ODM>")
		assertTrue fileContents.startsWith("""<?xml version="1.0" encoding="UTF-8"?>""")
	}
	
	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFileContents("")
		}
	}
	
	@Test void testHasDuplicateBindings(){
		
		def xformWithDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-duplicate-bindings.xml"))
		assertTrue util.hasDuplicateBindings(xformWithDuplicateBindings)
		
		// Triangulating
		def xformWithNoDuplicateBindings = new XmlParser().parseText(util.loadFileContents("test-xform-no-duplicate-bindings.xml"))
		assertFalse util.hasDuplicateBindings(xformWithNoDuplicateBindings)
	}
}
