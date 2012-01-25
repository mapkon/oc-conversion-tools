package org.openxdata.oc.transport.impl

import static org.hamcrest.Matchers.*
import groovy.mock.interceptor.MockFor

import org.gmock.WithGMock
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.exception.ParseException
import org.openxdata.oc.exception.UnAvailableException
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class OpenClinicaSoapClientTest extends GroovyTestCase {
	
	def latestCRFVersions
	
	void setUp(){
		
		latestCRFVersions = TestData.getReturnXml()
	}
	
	@Test void testGetOpenxdataFormReturnsValidXmlWithCorrectStudyName() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()
			
			assertEquals  "Uganda", convertedStudyXml.@name.text()
						
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyRootElement() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			assertEquals 'study', convertedStudyXml.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyName() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			assertEquals 'Uganda', convertedStudyXml.@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyStudyKey() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			assertEquals 'S_12175', convertedStudyXml.@studyKey.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithCorrectNumberOfForms() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()


			assertEquals 8, forms.size()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithCorrectFormName() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()


			assertEquals  "SE_INTERCUR", forms[0].@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionElement() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()[0]

			assertEquals  "version", version.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionName() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()[0]

			assertEquals  "Intercurrent visit-v1", version.@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionDescription() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()[0]

			assertEquals  "Converted from ODM", version.@description.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformElement() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def forms = convertedStudyXml.children()
			def version = forms.children()

			assertEquals  "xform", forms.xform[0].name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformsElement() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def xforms = convertedStudyXml.form.version.xform.xforms[0]

			assertEquals  "xforms", xforms.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformsIsSerialized() {
		def connectionFactory = setUpConnectionFactoryMock(latestCRFVersions)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("oid")

			def xformsContent = convertedStudyXml.form.version.xform[0].text()

			assertTrue xformsContent instanceof String
		}
	}
	
	@Test void testGetSubjectKeysSHOULDReturnSubjectKeys(){
		def connectionFactory = setUpConnectionFactoryMock(TestData.studySubjectListSOAPResponse)
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def subjectKeys = client.getSubjectKeys("default-study")
			
			assertEquals 4, subjectKeys.size()
		}
	}
	
	@Test void testThatImportDataReturnsSuccessResponseOnCorrectODMFormat(){
		
		def connectionFactory = setUpConnectionFactoryMock(TestData.importSOAPSuccessResponse)
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			def reponse = client.importData(TestData.instanceData)
			
			assertNotNull reponse
			assertEquals 'Success', reponse
		}
	}
	
	@Test void testThatImportDataReturnsErrorOnIncorrectODM(){
		def connectionFactory = setUpConnectionFactoryMock(TestData.importSOAPErrorResponse)
		play{

			shouldFail(ImportException){
				def client = new OpenClinicaSoapClientImpl(connectionFactory)
				def reponse = client.importData(TestData.instanceData)
			}
		}
	}
	
	@Test void testThatInvalidXmlThrowsParseException(){
		def connectionFactory = setUpConnectionFactoryMock('''<////ODM>''')
		play{
			shouldFail(ParseException){
				def client = new OpenClinicaSoapClientImpl(connectionFactory)
				def xml = client.getOpenxdataForm("001")
			}
		}
	}
	
	@Test void testFindAllCRFSDoesNotReturnNull() {
		
		def connectionFactory = setUpConnectionFactoryMock(TestData.getReturnXml())
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def subjectKeys = client.findAllCRFS("oid")
			
			assertNotNull subjectKeys
		}
	}
	
	private def setUpConnectionFactoryMock(returnXml) {
		
		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST")
		connection.setRequestProperty("Content-Type", "text/xml")
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns("mock url").atMostOnce()

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes()))
		
		def connectionFactory = mock(ConnectionFactory.class)
		connectionFactory.getCRFConnection().returns(connection).atMostOnce()
		connectionFactory.getStudyConnection().returns(connection).atMostOnce()
		connectionFactory.getStudySubjectConnection().returns(connection).atMostOnce()
		
		return connectionFactory
	}
}
