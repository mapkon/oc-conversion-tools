package org.openxdata.oc

import groovy.xml.XmlUtil;

import org.junit.Test


class TransformTest extends GroovyTestCase {

	def inputDoc
	def convertedStudy

	public void setUp(){
		def inputString = new TransformUtil().loadFileContents("test-odm.xml")
		
		convertedStudy = new Transform().ConvertODMToXform(inputString)
		inputDoc = new XmlParser().parseText(inputString)
	}

	@Test void testConvertedStudyDefProperties(){	
		
		def actual = "Default Study - Uganda"
		
		assertEquals actual, convertedStudy.name
		assertEquals 2, convertedStudy.forms.size()
		assertNotNull convertedStudy.getFormVersion(convertedStudy.forms[0])
	}

	@Test void testShouldContainCorrectNumberOfFormElements() {
		def formsInInput = inputDoc.Study.MetaDataVersion.Protocol.StudyEventRef
		assertEquals( formsInInput.size(), convertedStudy.forms.size() )
		assertEquals( formsInInput.size(), convertedStudy.forms.children().size() )
	}

	@Test void testShouldContainVersionElement() {
		assertNotNull convertedStudy.forms
		convertedStudy.forms.each { assertTrue( it.version.size() == 1 ) }
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
					groupDef.ItemRef.each {
						def itemId = it.@ItemOID
						def itemDef = versionNode.ItemDef.find { it.@OID == itemId }

						def outputForm = convertedStudy.forms.find { it.@name.equals(eventDef.@OID) }
						def xformNode = outputForm.version.xform

						def bind = xformNode.xforms.model.bind.find { it.@id = itemId }
						
						assertNotNull bind
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
			def form = convertedStudy.forms.find{it.@name==eventOID}
			assertEquals eventOID, form.@name.text()
		}
	}
		
	@Test void testMUSTContainXformElement() {

		def xformNode = convertedStudy.forms.version.xform
		
		xformNode.each { xNode ->
			
			assertEquals 1, xNode.children().size()
			
			xNode.children().each{
				assertEquals "xforms", it.name().toString()
			}
		}	
	}
}