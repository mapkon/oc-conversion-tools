package org.openxdata.oc.transport.soap.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.soap.proxy.ImportWebServiceProxy


@WithGMock
class ImportWebServiceProxyTest extends GroovyTestCase {

	def importProxy

	@Before void setUp(){

		def connectionFactory = setUpConnectionFactoryMock(TestData.importSOAPSuccessResponse)
		importProxy = new ImportWebServiceProxy(connectionFactory:connectionFactory)

	}

	@Test void testGetEnvelopeHasCorrectDataPath(){

		def envelope = importProxy.getSoapEnvelope()
		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/data/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}
	
	@Test void testImportShouldFailOnEmptyInstanceData(){

		shouldFail(ImportException){
			def message = importProxy.importData([])
		}
	}

	@Test void testImportShouldNotReturnNull(){

		play{
			def message = importProxy.importData(TestData.getInstanceData())
			assertNotNull message
		}
	}

	@Test void testImportShouldSuccessOnCorrectInstanceData(){

		play{
			
			def messages = importProxy.importData(TestData.getInstanceData())
			assertEquals 'Success', messages.get("F_MSA2_1")
		}
	}
	
	@Test void testImportShouldReturnFailOnErraticImport(){

		def connectionFactory2 = setUpConnectionFactoryMock(TestData.importSOAPErrorResponse)
		
		play{

			def importProxy2 = new ImportWebServiceProxy(connectionFactory:connectionFactory2)
			def messages = importProxy2.importData(TestData.getInstanceData())
			assertEquals 'Fail: Subject key not found', messages.get("F_MSA2_1")
		}
	}

	@Test void testImportShouldFailOnWithCorrectExceptionOnEmptyInstanceData() {

		def msg = shouldFail(ImportException) {

			def message = importProxy.importData([])
			assertEquals 'Error', message[0]
		}
	}
	
	@Test void testImportShouldFailOnWithCorrectExceptionMessage() {
		
		def msg = shouldFail(ImportException) {
		
			def message = importProxy.importData([])
		}
				
		assertEquals 'Cannot process empty instance data.', msg
	}

	private def setUpConnectionFactoryMock(returnXml) {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST").atMostOnce()
		connection.setRequestProperty("Content-Type", "text/xml").atMostOnce()
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class))).atMostOnce()
		connection.setDoOutput(true).atMostOnce()
		connection.getURL().returns("mock url").atMostOnce()

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream).atMostOnce()
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes())).atMostOnce()

		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection).atMostOnce()

		return factory
	}
}
