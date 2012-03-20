package org.openxdata.oc.model

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData

class EventTest {


	def event
	@Before public void setUp() {
		
		def eventNode = TestData.getStudySubjects().children()[0]
		event = new Event(eventNode.children()[1].event)
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

		assertEquals "SE_SC2", event.eventDefinitionOID
	}

	@Test void testThatEventHasAFormOIDList() {
		
		def formOIDs = event.getFormOIDs()
		
		assertEquals 1, formOIDs.size()
	}
	
	@Test void testNewEventHasFormOID() {

		assertNotNull "Event Should Have a FormOID", event.getFormOIDs()[0]
	}

	@Test void testNewEventHasCorrectFormOID() {
		assertEquals "F_MSA2_2", event.getFormOIDs()[0]
	}
	
	@Test void testEventHasName() {
		assertNotNull "Event name should not be null", event.eventName
	}
	
	@Test void testEventHasCorrectName() {
		assertEquals "SC2", event.eventName
	}
}
