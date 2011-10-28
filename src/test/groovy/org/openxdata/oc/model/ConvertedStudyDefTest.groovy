package org.openxdata.oc.model

import org.junit.Ignore
import org.junit.Test
import org.openxdata.oc.Transform
import org.openxdata.oc.TransformUtil


class ConvertedStudyDefTest extends GroovyTestCase {

	def odmDef
	def convertedStudyDef

	public void setUp(){

		def odmXmlStream = new TransformUtil().loadFileContents("test-odm.xml")

		odmDef = new ODMDefinition()
		odmDef.initializeProperties(odmXmlStream)

		convertedStudyDef  = Transform.getTransformer().ConvertODMToXform(odmXmlStream)
	}

	@Test void testStudyIDMUSTMatchOIDOfODMFile(){

		assertNotNull convertedStudyDef.id
		assertTrue convertedStudyDef.id.equals(odmDef.OID.toString())
	}

	@Test void testStudyNameMUSTMatchODMStudyName(){

		def actual = 'Default Study - Uganda'

		assertEquals actual, odmDef.name.toString()
		assertEquals actual, convertedStudyDef.name
	}

	@Test void testStudyDescriptionMUSTMatchODMStudyDescription(){

		def actual = 'test instance - Site of Uganda'

		assertEquals actual, odmDef.description.toString()
		assertEquals actual, convertedStudyDef.description
	}

	@Test void testStudyShouldHaveCorrectNumberOfForms(){

		def actual1 = 'SE_SC1'
		def actual2 = 'SE_SC2'

		assertEquals 2, convertedStudyDef.forms.size()

		assertEquals actual1, convertedStudyDef.forms[0].@name.text()
		assertEquals 'SC1', convertedStudyDef.forms[0].@description.text()

		assertEquals actual2, convertedStudyDef.forms[1].@name.text()
		assertEquals 'SC2', convertedStudyDef.forms[1].@description.text()

		assertEquals 2, odmDef.studyEventDefs.size()
	}

	@Test void testStudyShouldHaveCorrectNumberOfFormsGivenStudyEventDefs(){

		assertEquals odmDef.studyEventDefs.size(), convertedStudyDef.forms.size()

		assertEquals odmDef.studyEventDefs[0].@OID.text(), convertedStudyDef.forms[0].@name.text()
		assertEquals odmDef.studyEventDefs[0].@Name.text(), convertedStudyDef.forms[0].@description.text()

		assertEquals odmDef.studyEventDefs[1].@OID.text(), convertedStudyDef.forms[1].@name.text()
		assertEquals odmDef.studyEventDefs[1].@Name.text(), convertedStudyDef.forms[1].@description.text()
	}
	
	@Test void testStudyFormMUSTHaveVersion(){
		
		def form = convertedStudyDef.forms[0]
		def version = convertedStudyDef.getFormVersion(form)
		
		assertNotNull version
		assertTrue version.@name.text().contains('-v1')
		assertEquals form.@description.text()+'-v1', version.@name.text()
	}
}
