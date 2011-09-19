package org.openxdata.oc

import groovy.xml.XmlUtil;

import org.junit.Test

class TransformTest extends GroovyTestCase {

	def outputDoc
	def inputDoc
	
	public void setUp(){
		def transformer = new Transform();
		def inputString = new File(getClass().getResource("/org/openxdata/oc/test-odm.xml").getFile()).text
		def outputString = transformer.transformODM(inputString)
		outputDoc = new XmlParser().parseText(outputString)
		inputDoc = new XmlParser().parseText(inputString)
	}

	@Test
	void testShouldContainOXDStudyElement(){

		assertTrue(outputDoc.name().equals("study"))
		assertTrue(outputDoc.children().size() > 0)
	}

	@Test
	void testShouldContainFormElement() {
		def formsInInput = inputDoc.Study.MetaDataVersion.StudyEventDef
		assertEquals( formsInInput.size(), outputDoc.form.size() )
		assertEquals( formsInInput.size(), outputDoc.children().size() )
	}

	@Test
	void testShouldContainVersionElement() {
		outputDoc.form.each { assertTrue( it.version.size() == 1 ) }
	}

	@Test
	void testShouldContainXformElement() {

		outputDoc.form.version.each{
			assertTrue(it.xform.size() == 1)
		}

		outputDoc.form.version.xform.each {
			def xformString = it.text()

			def parsedXform = new XmlSlurper().parseText(xformString)

			assertNotNull(parsedXform)
			assertEquals("xforms", parsedXform.name())
		}
	}
}