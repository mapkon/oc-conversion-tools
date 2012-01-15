package org.openxdata.oc.transport.proxy;

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class ListAllByStudyWebServiceProxyTest {

	def listAllByStudyProxy
	@Before void setUp(){
		def connectionFactory = setUpConnectionFactoryMock(TestData.studySubjectListSOAPResponse)
		listAllByStudyProxy = new ListAllByStudyWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:connectionFactory)
	}

	@Test void testGetEnvelopeHasCorrectSubjectPath(){
		
		def envelope = listAllByStudyProxy.getSoapEnvelope()
		def envelopeXml = new XmlSlurper().parseText(envelope)
		
		def actual = 'http://openclinica.org/ws/studySubject/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()
		
		assertEquals actual, namespaceList[2].toString()
	}
	
	@Test void testListAllByStudyShouldNotReturnNull(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertNotNull subjects
		}
	}

	@Test void testListAllByStudyReturnsCorrectNumberOfSubjects(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertEquals 4, subjects.size()
		}
	}

	@Test void testListAllByStudyReturnsValidSubjectKeys(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertEquals 'SS_Morten', subjects[0]
			assertEquals 'SS_Jørn', subjects[1]
			assertEquals 'SS_Jonny', subjects[2]
			assertEquals 'SS_Janne', subjects[3]
		}
	}

	private def setUpConnectionFactoryMock(returnXml) {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod('POST')
		connection.setRequestProperty('Content-Type', 'text/xml')
		connection.setRequestProperty('Content-Length', is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns('mock url')

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getStudySubjectConnection().returns(connection)

		return factory
	}
}
