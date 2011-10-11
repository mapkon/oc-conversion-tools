package org.openxdata.oc


class TransformTest extends GroovyTestCase {

	def outputDoc
	def inputDoc
	def transformer = new Transform()

	public void setUp(){
		def inputString = new File(getClass().getResource("test-odm.xml").getFile()).text
		def outputString = transformer.transformODM(inputString, ['Jonny', 'Morten', 'Jorn', 'Janne'])
		outputDoc = new XmlParser().parseText(outputString)
		inputDoc = new XmlParser().parseText(inputString)
	}

	void testMUSTContainOXDStudyElement(){		
		assertTrue(outputDoc.name().equals("study"))
		assertTrue(outputDoc.children().size() > 0)
	}

	void testShouldContainCorrectNumberOfFormElements() {
		def formsInInput = inputDoc.Study.MetaDataVersion.Protocol.StudyEventRef
		assertEquals( formsInInput.size(), outputDoc.form.size() )
		assertEquals( formsInInput.size(), outputDoc.children().size() )
	}

	void testShouldContainVersionElement() {
		outputDoc.form.each { assertTrue( it.version.size() == 1 ) }
	}

	void testBindElementsShouldAppearInCorrectPlaceForEachFormAndPage() {

		def versionNode = inputDoc.Study.MetaDataVersion
		def numberOfInputsTested = 0;
		versionNode.Protocol.StudyEventRef.each {
			def eventId = it.@StudyEventOID
			def eventDef = versionNode.StudyEventDef.find {
				it.@OID == eventId
			}
			eventDef.FormRef.each {
				def formId = it.@FormOID
				def formDef = versionNode.FormDef.find {it.@OID==formId}
				formDef.ItemGroupRef.each {

					def groupId = it.@ItemGroupOID
					def groupDef = versionNode.ItemGroupDef.find { it.@OID == groupId }
					groupDef.ItemRef.each {
						def itemId = it.@ItemOID
						def itemDef = versionNode.ItemDef.find { it.@OID == itemId }

						def outputForm = outputDoc.form.find { it.@name.equals(eventDef.@OID) }
						def xforms = outputForm.version.xform
						def xformsNode = new XmlSlurper().parseText(xforms.text())

						def bind = xformsNode.model.bind.find { it.@id = itemId }
						assertNotNull bind
						numberOfInputsTested++
					}
				}
			}
		}

		def itemsInInput = inputDoc.Study.MetaDataVersion.ItemGroupDef.ItemRef.size()

		assertTrue numberOfInputsTested > 1
		assertTrue numberOfInputsTested >= itemsInInput
	}

	void testExistenceOfFormDefGivenStudyEventRef() {
		inputDoc.Study.MetaDataVersion.Protocol.StudyEventRef.each {
			def eventOID = it.@StudyEventOID
			def form = outputDoc.form.find{it.@name==eventOID}
			assertEquals(eventOID, form.@name)
		}
	}
		
	void testMUSTContainXformElement() {

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