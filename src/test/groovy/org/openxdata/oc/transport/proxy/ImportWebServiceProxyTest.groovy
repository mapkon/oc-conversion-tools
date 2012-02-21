package org.openxdata.oc.transport.proxy;

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class ImportWebServiceProxyTest extends GroovyTestCase {

	def importProxy

	@Before void setUp(){

		def connectionFactory = setUpConnectionFactoryMock(TestData.importSOAPSuccessResponse)
		importProxy = new ImportWebServiceProxy(connectionFactory:connectionFactory)

	}

	@Test void testImportShouldFailOnEmptyInstanceData(){

		shouldFail(ImportException){
			def message = importProxy.importData([])
		}
	}

	@Test void testGetEnvelopeHasCorrectDataPath(){

		def envelope = importProxy.getSoapEnvelope()
		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/data/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}

	@Test void testImportShouldNotReturnNull(){

		play{
			def message = importProxy.importData(TestData.getOpenXdataInstanceData())
			assertNotNull message
		}
	}

	@Test void testImportShouldSuccessOnCorrectInstanceData(){

		play{
			def message = importProxy.importData(TestData.getOpenXdataInstanceData())
			assertEquals 'Success', message
		}
	}

	@Test void testImportShouldFailOnIncorrectImport(){
		def connectionFactory2 = setUpConnectionFactoryMock(TestData.importSOAPErrorResponse)
		play{
			shouldFail(ImportException){
				def importProxy2 = new ImportWebServiceProxy(connectionFactory:connectionFactory2)
				def message = importProxy2.importData(TestData.getOpenXdataInstanceData())
				assertEquals 'Error', message
			}
		}
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
