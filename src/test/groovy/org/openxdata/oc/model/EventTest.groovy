package org.openxdata.oc.model

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData;

class EventTest {


	def event
	@Before public void setUp() {
		
		def eventXml = new XmlSlurper().parseText(TestData.event1Xml)
		event = new Event(eventXml)
	}

	@Test void testNewEventHasOrdinal() {
		assertNotNull "Event must have ordinal", event.ordinal
	}
	
	@Test void testNewEventHasCorrectOrdinal() {
		
		assertEquals 1, Integer.valueOf(event.ordinal.toString())	
	}
	
	@Test void testNewEventHasEventDefinitionOID() {

		assertNotNull "EventDefinitionOID should not be null", event.eventDefinitionOID
	}

	@Test void testNewEventHasCorrectEventDefinitionOID() {

		assertEquals "SE_ADVERSEE", event.eventDefinitionOID.toString()
	}

	@Test void testNewEventHasFormOID() {

		assertNotNull "Event Should Have a FormOID", event.formOID
	}

	@Test void testNewEventHasCorrectFormOID() {
		assertEquals "F_AEAD_3", event.formOID.toString()
	}
	
	@Test void testNewEventHasSubjectKeys() {
		def subjectKeys = event.subjectKeys
		
		assertEquals "There should be 3 Study Subjects attached to this event.", 3, event.getSubjectKeys().size()
	}
	
	@Test void testNewEventHas_SS_20100200_SubjectKey() {
		def subjects = event.getSubjectKeys()
		
		assertEquals "SS_20100200", subjects[0]
	}
	
	@Test void testNewEventHas_SS_2M89098L_SubjectKey() {
		def subjects = event.getSubjectKeys()
		
		assertEquals "SS_2M89098L", subjects[1]
	}
	
	@Test void testNewEventHas_SS_3M9779A_SubjectKey() {
		def subjects = event.getSubjectKeys()
		
		assertEquals "SS_3M9779A", subjects[2]
	}
}
