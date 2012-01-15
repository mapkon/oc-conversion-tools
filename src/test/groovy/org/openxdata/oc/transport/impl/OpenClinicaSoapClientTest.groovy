package org.openxdata.oc.transport.impl

import static org.hamcrest.Matchers.*
import groovy.mock.interceptor.MockFor

import org.gmock.WithGMock
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.exception.ParseException
import org.openxdata.oc.exception.UnAvailableException
import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class OpenClinicaSoapClientTest extends GroovyTestCase {
	
	def latestCRFVersions
	
	void setUp(){
		
		latestCRFVersions = TestData.getReturnXml()
	}
	
	@Test void testListAllMUSTReturnListOfCorrectSize() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.listAllReturnSOAPResponse)

		play {
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def studies = client.listAll()
			assertEquals 2, studies.size()
		}
	}

	@Test void testListAllMUSTReturnsValidOpenclinicaStudies() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.listAllReturnSOAPResponse)

		play {
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def actual = client.listAll()
			
			def study1 = new ConvertedOpenclinicaStudy(identifier: "default-study", OID: "S_DEFAULTS1", name:"Default Study")
			def study2 = new ConvertedOpenclinicaStudy(identifier: "001", OID: "S_001", name: "Test Study")
			
			assertEquals actual[0], study1
			assertEquals actual[1], study2
		}
	}

	@Test void testGetMetaDataMUSTReturnsValidXmlWithODMRootElement() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.metaDataReturnSOAPResponse)
		
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def response = client.getMetadata("001")
			def xml = new XmlSlurper().parseText(response)
						
			assertEquals  "ODM", xml.name()
		}	
	}
	
	@Test void testGetMetaDataMUSTReturnsValidXmlWithStudyElement() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.metaDataReturnSOAPResponse)
		
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def response = client.getMetadata("001")
			def xml = new XmlSlurper().parseText(response)
						
			assertEquals  "Study", xml.Study[0].name()
		}
	}
	
	@Test void testGetMetaDataMUSTReturnsValidXmlWithOnlyOneStudyElement() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.metaDataReturnSOAPResponse)
		
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def response = client.getMetadata("001")
			def xml = new XmlSlurper().parseText(response).declareNamespace(oc: "http://www.cdisc.org/ns/odm/v1.3")
						
			assertEquals  1, xml.Study[0].size()
		}
	}
	
	@Test void testGetMetaDataMUSTReturnsValidXmlWithCorrectStudyOID() {
		def connectionFactory = setUpConnectionFactoryMock(TestData.metaDataReturnSOAPResponse)
		
		play{
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def response = client.getMetadata("001")
			def xml = new XmlSlurper().parseText(response).declareNamespace(oc: "http://www.cdisc.org/ns/odm/v1.3")
						
			assertEquals  "S_001", xml.Study[0].@OID.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXmlWithCorrectStudyName() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {
			
			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			
			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()
			
			assertEquals  "Test Study", convertedStudyXml.@name.text()
						
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyRootElement() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			assertEquals 'study', convertedStudyXml.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyName() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			assertEquals 'Test Study', convertedStudyXml.@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithStudyStudyKey() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			assertEquals 'S_001', convertedStudyXml.@studyKey.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithCorrectNumberOfForms() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()


			assertEquals 1, forms.size()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithCorrectFormName() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()


			assertEquals  "SE_VISIT", forms[0].@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionElement() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()[0]

			assertEquals  "version", version.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionName() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()

			assertEquals  "Visit-v1", version.@name.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsValidXformWithVersionDescription() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()

			assertEquals  "Converted from ODM", version.@description.text()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformElement() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()

			assertEquals  "xform", forms.xform[0].name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformsElement() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

			def xforms = convertedStudyXml.form.version.xform.xforms[0]

			assertEquals  "xforms", xforms.name()
		}
	}
	
	@Test void testGetOpenxdataFormReturnsCorrectXformWithXformsIsSerialized() {
		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {

			def client = new OpenClinicaSoapClientImpl(connectionFactory)

			def convertedStudyXml = client.getOpenxdataForm("001")

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
	
	@Test void testWrongURLMUSTThrowUnAvailableException(){

		def mock = new MockFor(ConnectionFactory)
		mock.demand.getStudyConnection { throw new UnAvailableException("Incorrect url") }
		def connectionFactory = mock.proxyInstance()
		shouldFail(UnAvailableException){

			def client = new OpenClinicaSoapClientImpl(connectionFactory)
			client.listAll()
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
		connectionFactory.getStudyConnection().returns(connection).atMostOnce()
		connectionFactory.getStudySubjectConnection().returns(connection).atMostOnce()
		
		return connectionFactory
	}
}
