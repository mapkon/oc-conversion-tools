package org.openxdata.oc.transport;

import static org.hamcrest.Matchers.*
import groovy.mock.interceptor.MockFor

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.exception.UnAvailableException


@WithGMock
class HttpTransportHandlerTest extends GroovyTestCase {
	
	def transportHandler
	
	@Before public void setUp(){
		transportHandler = new HttpTransportHandler(envelope:envelope)
		
	}
	
	@Test void testSendRequestShouldNotReturnNull(){
		
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			assertNotNull response
		}
	}
	
	@Test void testSendRequestShouldReturnValidResponseContainingEnvelope(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def name = response.name().localPart
			assertEquals 'Envelope', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidResponseContainingHeader(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def name = response.children()[0].name().localPart
			assertEquals 'Header', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidResponseContainingBody(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def name = response.children()[1].name().localPart
			assertEquals 'Body', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidBodyContainingChildren(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def body = response.children()[1]
			assertEquals 1, body.children().size()
		}
	}
	
	@Test void testSendRequestShouldReturnValidBodyContainingListAllResponse(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def body = response.children()[1]
			def name = body.children()[0].name().localPart
			assertEquals 'listAllResponse', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidListAllResponseContainResultElement(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def body = response.children()[1]
			def listAllResponse = body.children()[0]
			def result = listAllResponse.children()[0]
			
			def name = result.name().localPart
			assertEquals 'result', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidListAllResponseContainStudiesElement(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def body = response.children()[1]
			def listAllResponse = body.children()[0]
			def studies = listAllResponse.children()[1]
			
			def name = studies.name().localPart
			assertEquals 'studies', name
		}
	}
	
	@Test void testSendRequestShouldReturnValidListAllResponseContainStudiesElementContains2Studies(){
		def connection = setUpConnectionFactoryMock(TestUtils.listAllReturnSOAPResponse)
		play{
			def response = transportHandler.sendRequest(connection)
			
			def body = response.children()[1]
			def listAllResponse = body.children()[0]
			def studies = listAllResponse.children()[1]
			
			assertEquals 2, studies.children().size()
		}
	}
	
	@Test void testUnAvailableExceptionShouldBeThrownWhenServerCannotBeReached(){
		def mock = new MockFor(HttpTransportHandler)
		mock.demand.sendRequest { throw new UnAvailableException("Server is unreacheable!") }
		
		def handler = mock.proxyInstance()
		
		play{
			shouldFail(UnAvailableException) {
				def response = handler.sendRequest(null)
			}
		}
	}
	
	private def setUpConnectionFactoryMock(responseStream) {
		
		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST")
		connection.setRequestProperty("Content-Type", "text/xml")
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns("mock url").atMostOnce()
		
		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(responseStream.getBytes()))
		
		return connection
	}
	
	def envelope = '''<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/study/v1">
						<soapenv:Header/>
						<soapenv:Body>
		   					<v1:listAllRequest>?</v1:listAllRequest>
		  				</soapenv:Body>
	     			</soapenv:Envelope>'''
}
