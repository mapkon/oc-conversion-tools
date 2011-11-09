package org.openxdata.oc.transport.proxy;

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class ImportWebServiceProxyTest extends GroovyTestCase {

	def importProxy
	def instanceData = []

	@Before void setUp(){

		def connectionFactory = setUpConnectionFactoryMock(importSOAPSuccessResponse)
		importProxy = new ImportWebServiceProxy(connectionFactory:connectionFactory)

		instanceData.add(testInstanceData)
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
			def message = importProxy.importData(instanceData)
			assertNotNull message
		}
	}

	@Test void testImportShouldSuccessOnCorrectInstanceData(){

		play{
			def message = importProxy.importData(instanceData)
			assertEquals 'Success', message
		}
	}

	@Test void testImportShouldFailOnIncorrectImport(){
		def connectionFactory2 = setUpConnectionFactoryMock(importSOAPErrorResponse)
		play{
			shouldFail(ImportException){
				def importProxy2 = new ImportWebServiceProxy(connectionFactory:connectionFactory2)
				def message = importProxy2.importData(instanceData)
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

	def testInstanceData = """<?xml version="1.0" encoding="UTF-8"?>
								<test_study_se_visit_visit-v1 xmlns="" Description="converted from ODM to Xform" formKey="test_study_se_visit_visit-v1" id="10" name="SE_VISIT_Visit-v1">
								  <ClinicalData xmlns="http://www.w3.org/2002/xforms" MetaDataVersionOID="v1.0.0" StudyOID="S_001">
									<SubjectData SubjectKey="SS_Foo">
									  <StudyEventData StudyEventOID="SE_VISIT">
										<FormData FormOID="F_SAMPLECRF_1">
										  <ItemGroupData ItemGroupOID="IG_SAMPL_UNGROUPED">
											<ItemData ItemOID="I_SAMPL_SC_ITEM_01" Value="really" value=""/>
											<ItemData ItemOID="I_SAMPL_SC_ITEM_02" Value="ok" value=""/>
										  </ItemGroupData>
										  <ItemGroupData ItemGroupOID="IG_SAMPL_GROUP01">
											<ItemData ItemOID="I_SAMPL_SC_REPEATING_ITEM_01" Value="2011-09-15" value=""/>
											<ItemData ItemOID="I_SAMPL_SC_REPEATING_ITEM_02" Value="222" value=""/>
										  </ItemGroupData>
										</FormData>
									  </StudyEventData>
									</SubjectData>
								  </ClinicalData>
								</test_study_se_visit_visit-v1>"""

	def importSOAPSuccessResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										<SOAP-ENV:Header/>
										<SOAP-ENV:Body>
										   <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
											  <result>Success</result>
										   </importDataResponse>
										</SOAP-ENV:Body>
									 </SOAP-ENV:Envelope>'''

	def importSOAPErrorResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									<SOAP-ENV:Header/>
									<SOAP-ENV:Body>
									   <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
										  <result>Fail</result>
										  <error>Error.</error>
									   </importDataResponse>
									</SOAP-ENV:Body>
								 </SOAP-ENV:Envelope>'''
}
