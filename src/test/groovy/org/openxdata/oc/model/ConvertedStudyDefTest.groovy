package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.Transform
import org.openxdata.oc.TransformUtil


class ConvertedStudyDefTest extends GroovyTestCase {

	def odmDef
	def convertedStudyDef
	def subjects = ['Jonny', 'Morten', 'Jorn', 'Janne']

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
	
	@Test void testAppendSubjectKeysMUSTAppendCorrectNumberOfSubjectKeys(){
		
		convertedStudyDef.appendSubjectKeyNode(subjects)
		
		def subjectKeyGroup = convertedStudyDef.getSubjectKeyGroupNode()
				
		assertEquals 2, subjectKeyGroup.size()
		assertEquals 'group', subjectKeyGroup[0].name()
		subjectKeyGroup.each {
			assertEquals 2, it.getAt(0).children().size()
		}
		
		def labelNode = subjectKeyGroup[0].getAt(0).children()[0]
		
		assertNotNull labelNode
		assertEquals 'label', labelNode.name()
		
		def select1Node = subjectKeyGroup[1].getAt(0).children()[1]
		
		assertNotNull select1Node
		assertEquals 'select1', select1Node.name().toString()
		assertEquals 'subjectKeyBind', select1Node.@bind.toString()
		assertEquals 4, select1Node.children().size()
		
	}
	
	@Test void testAppendSubjectsShouldAppendCorrectSubjectKeys(){
		
		convertedStudyDef.appendSubjectKeyNode(subjects)
		def subjectKeyGroup = convertedStudyDef.getSubjectKeyGroupNode()
		def select1Node = subjectKeyGroup[1].getAt(0).children()[1]

		assertEquals 4, select1Node.children().size()
		assertEquals 'Jonny', select1Node.children()[0].@id.toString()
		assertEquals 'Morten', select1Node.children()[1].@id.toString()
		assertEquals 'Jorn', select1Node.children()[2].@id.toString()
		assertEquals 'Janne', select1Node.children()[3].@id.toString()

	}
	
	@Test void testAppendSubjectsShouldAppendSelect1NodeWithLabelNode(){
		convertedStudyDef.appendSubjectKeyNode(subjects)
		def subjectKeyGroup = convertedStudyDef.getSubjectKeyGroupNode()
		def select1Node = subjectKeyGroup[1].getAt(0).children()[1]
		
		select1Node.children().each{
			assertEquals 'label', it.children()[0].name().toString()
			assertEquals 'value', it.children()[1].name().toString()
		}
	}
	
	@Test void testAppendNullSubjectsShouldAppendInputNode(){
		
		convertedStudyDef.appendSubjectKeyNode([:])
		def subjectKeyGroup = convertedStudyDef.getSubjectKeyGroupNode()
		def input1Node = subjectKeyGroup[1].getAt(0).children()[1]
		
		assertNotNull input1Node
		assertEquals 'input', input1Node.name().toString()
		assertEquals 'subjectKeyBind', input1Node.@bind.toString()
	}
	
	@Test void testParseMeasureUnits(){
		def measurementUnits = convertedStudyDef.parseMeasurementUnits()
		
		assertNotNull measurementUnits
		assertEquals 1, measurementUnits.size()
		assertEquals "10^3/MM^3", measurementUnits.get("10<SUP>3</SUP>/MM<SUP>3</SUP>")
	}
	
	@Test void testSerializeXformNode(){
		
		def xformText = convertedStudyDef.serializeXformNode()
		
		assertNotNull xformText
		assertTrue xformText instanceof String
	}
		
	@Test void testGetNodeListShouldReturnExistingNodeList(){
		
		def hintNodeList = convertedStudyDef.getNodeList("hint")
		
		assertNotNull hintNodeList
		assertEquals 1, hintNodeList.size()
		
		def groupNodeList = convertedStudyDef.getNodeList("group")
		
		assertNotNull groupNodeList
		assertEquals 6, groupNodeList.size()
	}

}
