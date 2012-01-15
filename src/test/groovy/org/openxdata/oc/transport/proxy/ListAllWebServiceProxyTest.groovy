package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class ListAllWebServiceProxyTest extends GroovyTestCase {
	
	def listAllProxy
	
	@Before void setUp(){
		
		def connectionFactory = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)	
		listAllProxy = new ListAllWebServiceProxy(username:"uname", hashedPassword:"pass", connectionFactory:connectionFactory)
	}
	
	@Test void testListAllShouldReturnValidResponse(){
		play{
			def studies = listAllProxy.listAll()
			assertNotNull studies
		}
	}
	
	@Test void testGetEnvelopeHasCorrectStudyPath(){

		def envelope = listAllProxy.getSoapEnvelope()
		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/study/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}
	
	@Test void testListAllShouldReturnCorrectNumberOfStudies() {
		play{
			def studies = listAllProxy.listAll()
			assertEquals 2, studies.size()
		}
	}
	
	@Test void testListAllShouldReturnValidStudiesWithIdentifier() {
		play{
			def studies = listAllProxy.listAll()
			
			def actual1 = studies[0]
			def actual2 = studies[1]
			
			assertEquals 'default-study', actual1.identifier
			assertEquals '001', actual2.identifier
		}
	}
	
	@Test void testListAllShouldReturnValidStudiesWithOID() {
		play{
			def studies = listAllProxy.listAll()
			
			def actual1 = studies[0]
			def actual2 = studies[1]
			
			assertEquals 'S_DEFAULTS1', actual1.OID
			assertEquals 'S_001', actual2.OID
		}
	}
	
	@Test void testListAllShouldReturnValidStudiesWithName() {
		play{
			def studies = listAllProxy.listAll()
			
			def actual1 = studies[0]
			def actual2 = studies[1]
			
			assertEquals 'Default Study', actual1.name
			assertEquals 'Test Study', actual2.name
		}
	}
	
	@Test void testExtractStudiesShouldExtractCorrectNumberOfStudiesFromResponse(){
		
		def responseXml = new XmlParser().parseText(TestUtils.listAllReturnSOAPResponse)
		def studiesSize = listAllProxy.extractStudies(responseXml)
		
		assertEquals 2, studiesSize.size() 
	}
	
	private def setUpConnectionFactoryMock(returnXml) {
		
		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST")
		connection.setRequestProperty("Content-Type", "text/xml")
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns("mock url")
		
		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes()))
		
		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection)
		
		return factory
	}
}
