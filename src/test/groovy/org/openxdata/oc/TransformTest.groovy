package org.openxdata.oc

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
						def xforms = outputForm.version.xform
						def xformsNode = new XmlSlurper().parseText(xforms.text())

						def bind = xformsNode.model.bind.find { it.@id = itemId }
						
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
			assertEquals(eventOID, form.@name.text())
		}
	}
		
	@Test void testMUSTContainXformElement() {

		convertedStudy.forms.version.each{
			assertTrue(it.xform.size() == 1)
		}

		convertedStudy.forms.version.xform.each {
			def xformString = it.text()

			def parsedXform = new XmlSlurper().parseText(xformString)

			assertNotNull(parsedXform)
			assertEquals("xforms", parsedXform.name())
		}		
	}
}