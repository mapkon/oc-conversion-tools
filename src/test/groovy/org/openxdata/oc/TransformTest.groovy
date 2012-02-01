package org.openxdata.oc

import org.junit.Test
import org.openxdata.oc.util.TransformUtil


class TransformTest extends GroovyTestCase {

	def inputDoc
	def convertedXform
	def subjectKeyGroup

	public void setUp(){
		def inputString = new TransformUtil().loadFileContents("test-odm.xml")
		
		convertedXform = new Transform().ConvertODMToXform(inputString)
		inputDoc = new XmlParser().parseText(inputString)
		
	}

	@Test void testconvertedXformHasCorrectName(){	
				
		assertEquals "Default Study - Uganda", convertedXform.@name.text()
	}
	
	@Test void testconvertedXformHasCorrectNumberOfForms(){
		
		assertEquals 2, convertedXform.children().size()
	}
	
	@Test void testThatConvertedXformHasFormsWithFormVersions(){
		
		convertedXform.children().each{
			assertEquals 'version', it.version[0].name()
		}
	}

	@Test void testConvertedXformShouldContainCorrectNumberOfFormElementsAsInODM() {
		
		def studyEventRefs = inputDoc.Study.MetaDataVersion.Protocol.StudyEventRef
		assertEquals studyEventRefs.size(), convertedXform.form.children().size() 
	}

	@Test void testConvertedXformShouldHaveFormsContainingOneVersionElement() {
		convertedXform.form.each { assertEquals 1, it.version.size()  }
	}
	
	@Test void testConvertedXformFormMUSTHaveVersion(){

		def version = convertedXform.form[0].children()

		assertTrue version.'@name'.text().contains('-v1')
	}

	@Test void testBindElementsShouldAppearInCorrectPlaceForEachFormAndPage() {

		def versionNode = inputDoc.Study.MetaDataVersion
		def numberOfInputsTested = 0
		versionNode.Protocol.StudyEventRef.each {
			def eventId = it.@StudyEventOID
			def eventDef = versionNode.StudyEventDef.find { it.@OID == eventId }
			eventDef.FormRef.each {
				def formId = it.@FormOID
				def formDef = versionNode.FormDef.find {it.@OID==formId}
				formDef.ItemGroupRef.each {

					def groupId = it.@ItemGroupOID
					def groupDef = versionNode.ItemGroupDef.find { it.@OID == groupId }
					groupDef.ItemRef.each { itemDef -> 
						
						def form = convertedXform.form.find { it.'@name'.equals(eventDef.'@OID') }
						
						def xformNode = new XmlSlurper().parseText(form.version.xform.text())
						
						def bind = xformNode.model.bind.find { it.@id.equals("""${itemDef.@ItemOID}""") }
						
						assertEquals 1, bind.size()

						numberOfInputsTested++
					}
				}
			}
		}

		def itemsInInput = inputDoc.Study.MetaDataVersion.ItemGroupDef.ItemRef.size()

		assertTrue numberOfInputsTested > 1
		assertTrue numberOfInputsTested >= itemsInInput
	}

	@Test void testExistenceOfFormDefGivenStudyEventRef() {
		inputDoc.Study.MetaDataVersion.Protocol.StudyEventRef.each {
			def eventOID = it.@StudyEventOID
			def form = convertedXform.form.find{it.@name==eventOID}
			assertEquals eventOID, form.@name.text()
		}
	}
	
	@Test void testConvertedXformIDMUSTMatchOIDOfODMFile(){

		assertEquals convertedXform.@studyKey.text(), inputDoc.Study.'@OID'.text()
	}
		
	@Test void testConvertedXformNameMUSTMatchODMStudyName(){

		assertEquals inputDoc.Study.GlobalVariables.StudyName.text(), convertedXform.'@name'.text()
	}

	@Test void testConvertedXformDescriptionMUSTMatchODMStudyDescription(){

		assertEquals inputDoc.Study.GlobalVariables.StudyDescription.text(), convertedXform.'@description'.text()
	}

	@Test void testConvertedXformShouldHaveCorrectFormNameAttribute(){

		assertEquals 'SE_SC1', convertedXform.form[0].'@name'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormDescriptionAttribute(){

		assertEquals 'SC1', convertedXform.form[0].'@description'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormNameAttributeForSecondForm(){

		assertEquals 'SE_SC2', convertedXform.form[1].'@name'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormDescriptionAttributeForSecondForm(){

		assertEquals 'SC2', convertedXform.form[1].'@description'.text()
	}

	@Test void testConvertedXformShouldHaveCorrectNumberOfFormsGivenStudyEventDefs(){

		assertEquals inputDoc.depthFirst().StudyEventDef.size(), convertedXform.form.size()
	}

	@Test void testConvertedXformShouldHaveFormWithNameEquallingTheOIDOfTheStudyEventDef(){

		assertEquals inputDoc.depthFirst().StudyEventDef[0].'@OID', convertedXform.form[0].'@name'.text()
	}

	@Test void testConvertedXformShouldHaveFormWithNameEquallingTheOIDOfTheStudyEventDef2(){

		assertEquals inputDoc.depthFirst().StudyEventDef[1].'@OID', convertedXform.form[1].'@name'.text()
	}

	@Test void testConvertedXformShouldFormWithDescriptionEqualsToNameOfStudyEventDef(){

		assertEquals inputDoc.depthFirst().StudyEventDef[0].'@Name', convertedXform.form[0].'@description'.text()
	}

	@Test void testConvertedXformShouldFormWithDescriptionEqualsToNameOfStudyEventDef2(){

		assertEquals inputDoc.depthFirst().StudyEventDef[1].'@Name', convertedXform.form[1].'@description'.text()
	}

	@Test void testVersionNameHasVNumberSequence(){

		def version = convertedXform.form[0].children()

		assertEquals "${convertedXform.form[0].'@description'}-v1", version.'@name'.text()
	}
}