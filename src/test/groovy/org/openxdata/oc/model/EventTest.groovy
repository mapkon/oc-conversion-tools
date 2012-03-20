package org.openxdata.oc.model

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData

class EventTest {


	def event
	@Before public void setUp() {
		
		event = new Event(TestData.getEvents().children()[0])
	}

	@Test void testNewEventHasOrdinal() {
		assertNotNull "Event must have ordinal", event.ordinal
	}
	
	@Test void testNewEventHasCorrectOrdinal() {
		
		assertEquals 1, Integer.valueOf(event.ordinal)	
	}
	
	@Test void testNewEventHasEventDefinitionOID() {

		assertNotNull "EventDefinitionOID should not be null", event.eventDefinitionOID
	}

	@Test void testNewEventHasCorrectEventDefinitionOID() {

		assertEquals "SE_ADVERSEE", event.eventDefinitionOID
	}

	@Test void testThatEventHasAFormOIDList() {
		
		def formOIDs = event.getFormOIDs()
		
		assertEquals 1, formOIDs.size()
	}
	
	@Test void testNewEventHasFormOID() {

		assertNotNull "Event Should Have a FormOID", event.getFormOIDs()[0]
	}

	@Test void testNewEventHasCorrectFormOID() {
		assertEquals "F_AEAD_3", event.getFormOIDs()[0]
	}
	
	@Test void testNewEventHasSubjectKeys() {
		def subjectKeys = event.subjectKeys
		
		assertEquals "There should be 3 Study Subjects attached to this event.", 2, event.getSubjectKeys().size()
	}
	
	@Test void testNewEventHas_SS_20100200_SubjectKey() {
		def subjects = event.getSubjectKeys()
		
		assertEquals "SS_2M89098L", subjects[0]
	}
	
	@Test void testNewEventHas_SS_2M89098L_SubjectKey() {
		def subjects = event.getSubjectKeys()
		
		assertEquals "SS_20100200", subjects[1]
	}
	
	@Test void testEventHasName() {
		assertNotNull "Event name should not be null", event.name
	}
	
	@Test void testEventHasCorrectName() {
		assertEquals "Adverse Event Reporting", event.name
	}
}
