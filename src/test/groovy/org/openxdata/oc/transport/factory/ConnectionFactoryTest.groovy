package org.openxdata.oc.transport.factory

import org.junit.Test

class ConnectionFactoryTest extends GroovyTestCase {
	
	def host = 'http://localhost:8080/openclinica'
	def factory = new ConnectionURLFactory(host)
	
	@Test void testGetStudyConnectionShouldReturnCorrectURL(){
		
		assertTrue factory.getStudyConnection().getURL().getHost().toString().equals('localhost')
		assertEquals 'http://localhost:8080/openclinica/ws/study/v1', factory.getStudyConnection().getURL().toString()
	}
	
	@Test void testGetStudySubjectConnectionShouldReturnCorrectURL(){		
		assertEquals 'http://localhost:8080/openclinica/ws/studySubject/v1', factory.getStudySubjectConnection().getURL().toString()
	}
}
