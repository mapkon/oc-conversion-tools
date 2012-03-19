package org.openxdata.oc

import groovy.util.XmlParser
import groovy.util.XmlSlurper

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
		
		assertEquals 4, convertedXform.children().size()
	}
	
	@Test void testThatConvertedXformHasFormsWithFormVersions(){
		
		convertedXform.children().each{
			assertEquals 'version', it.version[0].name()
		}
	}

	@Test void testConvertedXformShouldContainCorrectNumberOfFormElementsAsInODM() {
		
		def formRefs = inputDoc.Study.MetaDataVersion.StudyEventDef.FormRef
		assertEquals formRefs.size(), convertedXform.form.children().size() 
	}

	@Test void testConvertedXformShouldHaveFormsContainingOneVersionElement() {
		convertedXform.form.each { assertEquals 1, it.version.size()  }
	}
	
	@Test void testConvertedXformFormMUSTHaveVersion(){

		def version = convertedXform.form[0].children()

		assertTrue version.'@name'.text().contains('-v1')
	}
	
	@Test void testThatTheNumberOfBindsAre47Given45ItemRefsInODM() {
		
		def binds = getBinds()
		
		// The extra two bindings are because of the repeat parent bindings
		assertEquals 51, binds.size()
	}
	
	@Test void testThatNumberOfBindingsInXformIsGreaterOrEqualsToNumberOfItemRefsInODM() {
		
		def binds = getBinds()
		def itemRefs = getItemRefs()
		
		// The extra two bindings are because of the repeat parent bindings
		assertTrue  'The number of binds should be equal to the number of ItemRefs in ODM + 2', binds.size() >= itemRefs.size()
	}
	
	@Test void testThatEveryItemRefIntheODMHasABindInTheConvertedXform() {
		
		def itemRefs = getItemRefs()
		itemRefs.each {
			
			def bind = getBind(it.@ItemOID)
			assertEquals bind, it.@ItemOID
		}
	}

	@Test void testExistenceOfFormDefGivenFormRef() {
		inputDoc.Study.MetaDataVersion.StudyEventDef.FormRef.each {
			
			def formOID = it.@FormOID
			def form = convertedXform.form.find{it.@description == formOID}
			
			assertEquals formOID, form.@description.text()
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

		assertEquals 'MSA1: Mother Screening Assessment 1 - 3', convertedXform.form[0].'@name'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormDescriptionAttribute(){

		assertEquals 'F_MSA1_3', convertedXform.form[0].'@description'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormNameAttributeForSecondForm(){

		assertEquals 'MSA1: Mother Screening Assessment 1 - 2', convertedXform.form[1].'@name'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormDescriptionAttributeForSecondForm(){

		assertEquals 'F_MSA1_2', convertedXform.form[1].@description.text()
	}

	@Test void testConvertedXformShouldHaveCorrectNumberOfFormsGivenFormefs(){

		assertEquals inputDoc.depthFirst().FormRef.size(), convertedXform.form.size()
	}

	@Test void testConvertedXformShouldHaveFormWithNameEquallingTheOIDOfTheFormRef(){

		def formOID = inputDoc.depthFirst().StudyEventDef[0].children()[0].@FormOID
		def formDef = inputDoc.depthFirst().FormDef.find {it.@OID == formOID }
		
		assertEquals formDef.@Name, convertedXform.form[0].'@name'.text()
	}

	@Test void testConvertedXformShouldHaveFormWithNameEquallingTheOIDOfTheFormRef2(){

		def formOID = inputDoc.depthFirst().StudyEventDef[0].children()[1].@FormOID
		def formDef = inputDoc.depthFirst().FormDef.find {it.@OID == formOID }
		
		assertEquals formDef.@Name, convertedXform.form[1].'@name'.text()
	}

	@Test void testConvertedXformShouldFormWithDescriptionEqualsToNameOfFormDef(){

		def formOID = inputDoc.depthFirst().StudyEventDef[0].children()[0].@FormOID
		def formDef = inputDoc.depthFirst().FormDef.find {it.@OID == formOID }
		
		assertEquals formDef.@Name, convertedXform.form[0].@name.text()
	}

	@Test void testConvertedXformShouldFormWithDescriptionEqualsToNameOfFormDef2(){

		def formOID = inputDoc.depthFirst().StudyEventDef[0].children()[1].@FormOID
		def formDef = inputDoc.depthFirst().FormDef.find {it.@OID == formOID }
		
		assertEquals formDef.@Name, convertedXform.form[1].@name.text()
		
	}

	@Test void testVersionNameHasVNumberSequence(){

		def version = convertedXform.form[0].children()

		assertEquals "${convertedXform.form[0].'@name'}-v1", version.'@name'.text()
	}
	
	@Test void testODMDataBindsHaveFormOIDAttribute() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each { 
			it.model.instance.ODM.children().each { element ->
				
				if(element.children().size() == 0 && !element.name().toString() == 'SubjectKey') {
					def text = element.@FormOID.text()
					assertTrue "Should have attribute FormOID", text.size() > 0
				}
			}
		}
	}
	
	@Test void testODMDataBindsHaveItemGroupOIDAttribute() {

		def xformNodes = getXformNodes()

		xformNodes.each {
			it.model.instance.ODM.children().each { element ->

				if(element.children().size() == 0 && !element.name().toString() == 'SubjectKey') {
					def text = element.@ItemGroupOID.text()
					assertTrue "Should have attribute ItemGroupOID", text.size() > 0
				}
			}
		}
	}
	
	@Test void testConvertedXmlShouldHaveSubjectkeyAttributeInTheInstanceODMElement() {
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			def odmNode = it.model.instance.ODM
			
			assertEquals "", odmNode.'@SubjectKey'.text()
		}
	}
	
	@Test void testThatEveryXformNodeInTheConvertedXformHasTheSubjectGroup() {
		
		def xformNodes = getXformNodes()
		xformNodes.each {
			
			def subjectGroup = it.depthFirst().find { it.@id == '1'}
			assertNotNull subjectGroup
		}
	}
	
	@Test void testThatEveryXformNodeInTheConvertedXformHasTheSubjectGroupWithSubjectKeyLabel() {
		
		def xformNodes = getXformNodes()
		xformNodes.each {
			
			def subjectGroup = it.depthFirst().find { it.@id == '1'}
			assertEquals "Subject key", subjectGroup.children()[0].text()
		}
	}
	
	@Test void testConvertedXformShouldHaveRepeatItems() {
		
		def repeats = getRepeats()
		
		assertEquals 4, repeats.size()
	}
	
	@Test void testConvertedXformHasInnerGroupWhenThereIsARepeat() {
		
		def form = convertedXform.form.find {
			it.@description.toString().equals("F_MSA2_1")
		}
		
		def xformNode = new XmlSlurper().parseText(form.version.xform.text())
		def group = xformNode.group.find {it.@id == "3"}
		
		assertEquals "group", group.children()[1].name()
	}
	
	@Test void testConvertedXformHasRepeatInGroupWithId3() {
		
		def form = convertedXform.form.find {
			it.@description.toString().equals("F_MSA2_1")
		}
		
		def xformNode = new XmlSlurper().parseText(form.version.xform.text())
		def group = xformNode.group.find {it.@id == "3"}
		
		assertEquals "repeat", group.children()[1].children()[1].name()
	}
	
	@Test void testThatConvertedXmlHasSubjectKeySetToRequired() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def subjectKeyBind = it.model.bind.find{ it.@id.equals("subjectKeyBind")}
			assertEquals "SubjectKey should be required", "true()", subjectKeyBind.@required.text()
		}
	}
	
	def getRepeats() {
		
		def repeats = []
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			def rpt = it.depthFirst().findAll{ it.name().toString() == 'repeat'}
			repeats.addAll(rpt)
		}
	}
	
	def getXformNodes() {

		def nodes = []
		convertedXform.form.each {
			
			def xformNode = new XmlSlurper().parseText(it.version.xform.text()).declareNamespace(xf:"http://www.w3.org/2002/xforms")
			
			nodes.add(xformNode)
		}
		
		return nodes
	}
	
	def getItemRefs() {

		def itemRefs = []
		def versionNode = inputDoc.Study.MetaDataVersion
		versionNode.Protocol.StudyEventRef.each {
			def studyEventId = it.@StudyEventOID
			def studyEventDef = versionNode.StudyEventDef.find { it.@OID == studyEventId }

			studyEventDef.FormRef.each {

				def formId = it.@FormOID
				def formDef = versionNode.FormDef.find { it.@OID == formId }

				formDef.ItemGroupRef.each {

					def itemGroupOID = it.@ItemGroupOID
					def itemGroupDef = versionNode.ItemGroupDef.find { it.@OID == itemGroupOID }
					
					itemRefs.addAll(itemGroupDef.children())
				}
			}
		}
		
		return itemRefs
	}
	
	def getBinds() {
		
		def binds = []
		def versionNode = inputDoc.Study.MetaDataVersion
		versionNode.StudyEventDef.FormRef.each {
			def formOID = it.@FormOID
			def formDef = versionNode.FormDef.find { it.@OID == formOID }
			
			def form = convertedXform.form.find { it.@description == formDef.@OID }
			def xformNode = new XmlSlurper().parseText(form.version.xform.text())
			
			def formBinds = xformNode.depthFirst().findAll{ it.name() == 'bind'}
			formBinds.each {
				binds.add(it.@id)
			}
		}
		
		return binds
	}
	
	def getBind(def itemOID) {

		def bind
		def binds = getBinds()
		
		binds.each {

			if(it.@id == itemOID) {
				bind = it
			}
		}
		
		return bind
	}
}