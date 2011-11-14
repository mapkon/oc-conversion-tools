package org.openxdata.oc.model

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.Transform
import org.openxdata.oc.TransformUtil


class ConvertedStudyDefTest extends GroovyTestCase {

	def odmDef
	def subjectKeyGroup
	def convertedStudyDef

	@Before public void setUp(){

		def odmXmlStream = new TransformUtil().loadFileContents("test-odm.xml")

		odmDef = new ODMDefinition()
		odmDef.initializeProperties(odmXmlStream)

		convertedStudyDef  = Transform.getTransformer().ConvertODMToXform(odmXmlStream)
		
		subjectKeyGroup = convertedStudyDef.getSubjectKeyGroupNode()
	}

	@Test void jtestStudyIDMUSTMatchOIDOfODMFile(){

		assertTrue convertedStudyDef.id.equals(odmDef.OID.toString())
	}

	@Test void testStudyNameMUSTMatchODMStudyName(){

		def actual = 'Default Study - Uganda'

		assertEquals actual, odmDef.name 
		assertEquals actual, convertedStudyDef.name
	}

	@Test void testStudyDescriptionMUSTMatchODMStudyDescription(){

		def actual = 'test instance - Site of Uganda'

		assertEquals actual, odmDef.description
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
	}

	@Test void testStudyShouldFormWithNameEqualsToOIDOfStudyEventDef(){

		assertEquals odmDef.studyEventDefs[0].@OID.text(), convertedStudyDef.forms[0].@name.text()
		assertEquals odmDef.studyEventDefs[1].@OID.text(), convertedStudyDef.forms[1].@name.text()
	}

	@Test void testStudyShouldFormWithDescriptionEqualsToNameOfStudyEventDef(){

		assertEquals odmDef.studyEventDefs[0].@Name.text(), convertedStudyDef.forms[0].@description.text()
		assertEquals odmDef.studyEventDefs[1].@Name.text(), convertedStudyDef.forms[1].@description.text()
	}
	
	@Test void testStudyFormMUSTHaveVersion(){
		
		def form = convertedStudyDef.forms[0]
		def version = convertedStudyDef.getFormVersion(form)
		
		assertTrue version.'@name'.text().contains('-v1')
	}
	
	@Test void testVersionNameHasVNumberSequence(){
		
		def form = convertedStudyDef.forms[0]
		def version = convertedStudyDef.getFormVersion(form)
		
		assertEquals "${form.'@description'}-v1", version.'@name'.text()
	}
	
	@Test void testConvertedStudyXmlGroupName(){
		assertEquals 'group', subjectKeyGroup.name()
	}

	@Test void testSubjectGroupHasTwoElements(){

		assertEquals 2, subjectKeyGroup.children().size()
	}
	
	@Test void testSubjectGroupElementHasLabelNode(){
		
		
		def labelNode = subjectKeyGroup.children()[0]
		
		assertEquals 'label', labelNode.name()
	}
	
	@Test void testSubjectGroupElementHasInputNode(){
		
		def inputNode = subjectKeyGroup.children()[1]
		
		assertEquals 'input', inputNode[0].name()
	}
	
	@Test void testInputBindingShouldBeEqualToSubjectKeyBind(){
		
		def inputNode = subjectKeyGroup.children()[1]
		
		assertEquals 'subjectKeyBind', inputNode.'@bind'.text()
	}
	
	@Test void testParseMeasureUnitsShouldConvertOneMeasurementUnit(){
		
		def measurementUnits = convertedStudyDef.parseMeasurementUnits()
		
		assertEquals 1, measurementUnits.size()
	}
	
	@Test void testParseMeasureUnitsShouldConvertOCMeasureunitsToXformHints(){
		def measurementUnits = convertedStudyDef.parseMeasurementUnits()
		
		assertEquals "10^3/MM^3", measurementUnits.get("10<SUP>3</SUP>/MM<SUP>3</SUP>")
	}
	
	@Test void testSerializeXformNodeShouldConvertXformNodeToString(){
		
		def xformText = convertedStudyDef.serializeXformNode()
		
		assertTrue xformText instanceof String
	}
}
