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
	
	@Test void testThatTheNumberOfBindsAre47Given45ItemRefsInODM() {
		
		def binds = getBinds();
		
		// The extra two bindings are because of the repeat parent bindings
		assertEquals 47, binds.size()
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
	
	@Test void testODMDataBindsHaveFormOIDAttribute() {
		
		def xformNodes = getXformNodes()
		
		xformNodes.each { 
			it.model.instance.ODM.children().each { element ->
				def text = element.@FormOID.text()
				assertTrue "Should have attribute FormOID", text.size() > 0
			}
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
		versionNode.Protocol.StudyEventRef.each {
			def studyEventId = it.@StudyEventOID
			def studyEventDef = versionNode.StudyEventDef.find { it.@OID == studyEventId }
			
			def form = convertedXform.form.find { it.@name == studyEventDef.@OID }
			def xformNode = new XmlSlurper().parseText(form.version.xform.text())
			
			def formBinds = xformNode.depthFirst().findAll{ it.name().toString() == 'bind'}
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
			if(it.toString() == itemOID) {
				bind = it
			}
		}
		
		return bind
	}
}