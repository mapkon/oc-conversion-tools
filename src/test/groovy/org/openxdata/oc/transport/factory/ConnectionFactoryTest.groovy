package org.openxdata.oc.transport.factory

import org.junit.Test

class ConnectionFactoryTest extends GroovyTestCase {
	
	def factory = new ConnectionFactory()
	
	@Test void testHostNameEqualsPropertiesFileHostName() {
		
		def host = factory.getStudyConnection().getURL().getHost()
		assertEquals '158.37.6.164', host
	}
	
	@Test void testGetStudyConnectionShouldReturnCorrectURL(){
		
		def url = factory.getStudyConnection().getURL().toString()
		assertEquals 'http://158.37.6.164/OpenClinica-ws/ws/study/v1', url
	}
	
	@Test void testGetStudySubjectConnectionShouldReturnCorrectURL(){		
		
		def url = factory.getStudySubjectConnection().getURL().toString()
		assertEquals 'http://158.37.6.164/OpenClinica-ws/ws/studySubject/v1', url
	}
	
	@Test void testGetCRFConnectionURLDoesNotReturnNull() {
		def url = factory.getCRFConnection().getURL().toString()
		assertNotNull url
	}
	
	@Test void testGetCRFConnectionURLReturnsCorrectURL() {
		def url = factory.getCRFConnection().getURL().toString()
		assertEquals 'http://158.37.6.164/OpenClinica-ws/ws/crf/v1', url
	}
}
