package org.openxdata.oc.model

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData


class StudySubjectTest {

	def studySubject
	
	@Before public void setUp() {
		
		def subjectEventsNode = TestData.getStudySubjects()
		studySubject = new StudySubject(subjectEventsNode.children()[5])
	}
	
	@Test void testThatStudySubjectHasSubjectOID() {
		
		assertNotNull "SubjectOID should never be null", studySubject.subjectOID
	}
	
	@Test void testThatStudySubjectHasCorrectSubjectOID() {
		
		assertEquals "SS_3M0003Z", studySubject.subjectOID
	}
	
	@Test void testThatStudySubjectHasEvents() {
		assertEquals 2, studySubject.getEvents().size()
	}
	
	@Test void testThatStudySubjectHasD7Event() {
		assertEquals "D7", studySubject.getEvents()[0].eventName
	}
	
	@Test void testThatStudySubjectHasSE_SC1Event() {
		assertEquals "SC1", studySubject.getEvents()[1].eventName
	}
}
