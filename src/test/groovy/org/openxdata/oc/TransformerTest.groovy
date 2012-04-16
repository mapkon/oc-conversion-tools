package org.openxdata.oc

import groovy.util.XmlParser
import groovy.util.XmlSlurper

import org.junit.Test
import org.openxdata.oc.util.TransformUtil


class TransformerTest extends GroovyTestCase {

	def inputDoc
	def convertedXform

	public void setUp(){
		
		def inputString = new TransformUtil().loadFileContents("test-odm.xml")
		
		convertedXform = new Transformer().convert(inputString)
		inputDoc = new XmlParser().parseText(inputString)
		
	}

	@Test void testconvertedXformHasCorrectName(){	
				
		assertEquals "Test Study", convertedXform.@name.text()
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
	
	@Test void testThatTheNumberOfBindsAre61Given48ItemRefsInODM() {
		
		def binds = getBinds()
		
		// The extra bindings are because of the repeat parent bindings
		assertEquals 65, binds.size()
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

		assertEquals 'F_TEST_1', convertedXform.form[0].'@description'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormNameAttributeForSecondForm(){

		assertEquals 'MSA1: Mother Screening Assessment 1 - 2', convertedXform.form[1].'@name'.text()
	}
	
	@Test void testConvertedXformShouldHaveCorrectFormDescriptionAttributeForSecondForm(){

		assertEquals 'F_TEST_2', convertedXform.form[1].@description.text()
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
	
	@Test void testThatEveryXformNodeInTheConvertedXformHasTheSubjectQuestionWithCorrectBind() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def firstGroup = it.depthFirst().find { it.@id == '1'}
			
			assertEquals "subjectKey", firstGroup.children()[1].@bind.text()
		}
	}
	
	@Test void testThatEveryXformNodeInTheConvertedXformHasTheSubjectQuestionWithCorrectLabel() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def firstGroup = it.depthFirst().find { it.@id == '1'}
			
			def label = firstGroup.children()[1].depthFirst().find{it.name().equals('label')}
			
			assertEquals "Subject Key", label.text()
		}
	}
	
	@Test void testThatEveryXformNodeInTheConvertedXformHasTheSubjectQuestionWithCorrectHint() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def firstGroup = it.depthFirst().find { it.@id == '1'}
			
			def label = firstGroup.children()[1].depthFirst().find{it.name().equals('hint')}
			
			assertEquals "The subject key for whom you are collecting data for.", label.text()
		}
	}
	
	@Test void testConvertedXformShouldHaveRepeatItems() {
		
		def repeats = getRepeats()
		
		assertEquals 4, repeats.size()
	}
	
	@Test void testConvertedXformHasInnerGroupWhenThereIsARepeat() {
		
		def form = convertedXform.form.find {
			it.@description.toString().equals("F_TEST_2")
		}
		
		def xformNode = new XmlSlurper().parseText(form.version.xform.text())
		def group = xformNode.group.find {it.@id == "1"}
		
		assertEquals "group", group.children()[2].name()
	}
	
	@Test void testConvertedXformHasRepeatInGroupWithId2() {
		
		def form = convertedXform.form.find {
			it.@description.toString().equals("F_TEST_4")
		}
		
		def xformNode = new XmlSlurper().parseText(form.version.xform.text())
		def group = xformNode.group.find {it.@id == "1"}
		
		assertEquals "repeat", group.children()[2].children()[1].name()
	}
	
	@Test void testThatConvertedXmlHasSubjectKeySetToRequired() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def subjectKey = it.model.bind.find{ it.@id.equals("subjectKey")}
			assertEquals "SubjectKey should be required", "true()", subjectKey.@required.text()
		}
	}
	
	@Test void testThatSubjectQuestionIsLocked() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def subjectKey = it.model.bind.find{ it.@id.equals("subjectKey")}
			assertEquals "SubjectKey should be locked", "true()", subjectKey.@locked.text()
		}
	}
	
	@Test void testThatSubjectQuestionIsHidden() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def subjectKey = it.model.bind.find{ it.@id.equals("subjectKey")}
			assertEquals "SubjectKey should be Hidden", "false()", subjectKey.@visible.text()
		}
	}
	
	@Test void testThatThereAreEqualNumberOfRequiredBindsAsMandatoryItemRefsInODM() {
		
		def requiredQtns = getRequiredQuestions()
		
		assertEquals "Required questions should equals Mandatory questions in ODM file.", 30, requiredQtns.size()

	}
	
	@Test void testThatEveryQuestionHasAHint() {

		getGroups().each {

			def hintNode = getHintNode(it)
			assertEquals "Every question must have a hint", "hint", hintNode.name()
		}
	}
	
	@Test void testThatHintTextIsNotNull() {
		
		getGroups().each {

			def hintNode = getHintNode(it)
			assertNotNull "Hint text should not be null", hintNode.text()
		}
	}
	
	@Test void testThatHintTextForSubjectKeyIsCorrect() {
		
		def subjectGroup = getGroups()[0]
		
		def subjectGroupHintNode = getHintNode(subjectGroup)
		
		assertEquals "The subject key for whom you are collecting data for.", subjectGroupHintNode.text()
	}
	
	private def getGroups() {
		
		def groups = []
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			
			def xGroups = it.group.findAll { it.name().is("group")}
			xGroups.each { group ->
				groups.add(group)
			}
		}
		
		return groups
	}

	private def getHintNode(def group) {
		
		return group.depthFirst().find{it.name().is("hint")}
	}
	
	private def getRequiredQuestions() {

		def qtns = []

		getXformNodes().each {
			
			def binds = it.model.bind.each {
				if(it.@required.equals("true()")) {
					qtns.add(it)
				}
			}
		}
		
		return qtns
	}
	
	private def getRepeats() {
		
		def repeats = []
		def xformNodes = getXformNodes()
		
		xformNodes.each {
			def rpt = it.depthFirst().findAll{ it.name().is('repeat')}
			repeats.addAll(rpt)
		}
	}
	
	private def getXformNodes() {

		def nodes = []
		convertedXform.form.each {
			
			def xformNode = new XmlSlurper().parseText(it.version.xform.text()).declareNamespace(xf:"http://www.w3.org/2002/xforms")
			
			nodes.add(xformNode)
		}
		
		return nodes
	}
	
	private def getItemRefs() {

		def itemRefs = []
		def versionNode = inputDoc.Study.MetaDataVersion
		versionNode.Protocol.StudyEventRef.each {
			def studyEventId = it.@StudyEventOID
			def studyEventDef = versionNode.StudyEventDef.find { it.@OID.equals(studyEventId) }

			studyEventDef.FormRef.each {

				def formId = it.@FormOID
				def formDef = versionNode.FormDef.find { it.@OID.equals(formId) }

				formDef.ItemGroupRef.each {

					def itemGroupOID = it.@ItemGroupOID
					def itemGroupDef = versionNode.ItemGroupDef.find { it.@OID.equals(itemGroupOID) }
					
					itemRefs.addAll(itemGroupDef.children())
				}
			}
		}
		
		return itemRefs
	}
	
	private def getBinds() {
		
		def binds = []
		def versionNode = inputDoc.Study.MetaDataVersion
		versionNode.StudyEventDef.FormRef.each {
			def formOID = it.@FormOID
			def formDef = versionNode.FormDef.find { it.@OID.equals(formOID) }
			
			def form = convertedXform.form.find { it.@description.equals(formDef.@OID) }
			def xformNode = new XmlSlurper().parseText(form.version.xform.text())
			
			def formBinds = xformNode.depthFirst().findAll{ it.name().is('bind')}
			formBinds.each {
				binds.add(it.@id)
			}
		}
		
		return binds
	}
	
	private def getBind(def itemOID) {

		def bind
		def binds = getBinds()
		
		binds.each {

			if(it.@id.equals(itemOID)) {
				bind = it
			}
		}
		
		return bind
	}
}