package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock;
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class ListAllWebServiceProxyTest extends GroovyTestCase {
	
	def listAllProxy
	
	@Before void setUp(){
		
		def connectionFactory = setUpConnectionFactoryMock(listAllReturnSOAPResponse)	
		listAllProxy = new ListAllWebServiceProxy(username:"uname", hashedPassword:"pass", connectionFactory:connectionFactory)
	}
	
	@Test void testListAllShouldReturnValidResponse(){
		play{
			def studies = listAllProxy.listAll()
			assertNotNull studies
		}
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
		
		def responseXml = new XmlParser().parseText(listAllReturnSOAPResponse)
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
	
	def listAllReturnSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										<SOAP-ENV:Header/>
										<SOAP-ENV:Body>
										   <listAllResponse xmlns="http://openclinica.org/ws/study/v1">
											  <result>Success</result>
											  <studies>
												 <study>
													<identifier>default-study</identifier>
													<oid>S_DEFAULTS1</oid>
													<name>Default Study</name>
												 </study>
												 <study>
													<identifier>001</identifier>
													<oid>S_001</oid>
													<name>Test Study</name>
												 </study>
											  </studies>
										   </listAllResponse>
										</SOAP-ENV:Body>
									 </SOAP-ENV:Envelope>"""
}
