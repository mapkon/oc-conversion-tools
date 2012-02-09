package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*
import groovy.util.GroovyTestCase

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class CRFMetaDataVersionProxyTest extends GroovyTestCase {

	def crfMetaDataVersionProxy

	@Before public void setUp() {

		def connectionFactory = setUpConnectionFactoryMock()
		crfMetaDataVersionProxy = new CRFMetaDataVersionProxy(username:'uname', hashedPassword:'pass', connectionFactory:connectionFactory)
	}

	@Test void testFindCRFHasCorrectEndPoint() {

		def envelope = crfMetaDataVersionProxy.getSoapEnvelope()

		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/crf/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}

	@Test void testFindCRFSDoesNotReturnNull(){

		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			assertNotNull response
		}
	}

	@Test void testFindCRFSReturnsResponseWithODMAsRoot() {

		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			assertEquals 'ODM', xml.name()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyElement() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyElement = xml.Study[0]

			assertEquals 'Study', studyElement.name()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyElementWithOID() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyOID = xml.Study[0].@OID

			assertEquals 'OID', studyOID.name()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyElementWithCorrectOID() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyOID = xml.Study[0].@OID

			assertEquals 'S_DEFAULTS1', studyOID.text()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyNameElement() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyNameElement = xml.Study.GlobalVariables.StudyName[0]

			assertEquals 'StudyName', studyNameElement.name()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyNameElementhavingCorrectName() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyNameText = xml.Study.GlobalVariables.StudyName[0].text()

			assertEquals 'Default Study', studyNameText
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyDescriptionElement() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyDescriptionElement = xml.Study.GlobalVariables.StudyDescription[0]

			assertEquals 'StudyDescription', studyDescriptionElement.name()
		}
	}

	@Test void testFindCRFSReturnsResponseWithStudyDescriptionElementWithCorrectDescription() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyDescriptionElement = xml.Study.GlobalVariables.StudyDescription[0]

			assertEquals 'test instance', studyDescriptionElement.text().trim()
		}
	}

	@Test void testFindCRFSReturnsResponseWithProtocolNameElement() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolNameElement = xml.Study.GlobalVariables.ProtocolName[0]

			assertEquals 'ProtocolName', protocolNameElement.name()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithProtocolNameElementWithCorrectProtocol() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolNameElement = xml.Study.GlobalVariables.ProtocolName[0]

			assertEquals 'default-study', protocolNameElement.text()
		}
	}

	@Test void testFindCRFSReturnsResponseWithProtocolElement() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolElement = xml.Study.MetaDataVersion.Protocol[0]

			assertEquals 'Protocol', protocolElement.name()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithProtocolElementHasCorrectNumberOfStudyEventRefs() {
		play{
			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolElement = xml.Study.MetaDataVersion.Protocol[0].children()

			assertEquals 8, protocolElement.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithStudyEventRefElement(){
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventRefName = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0].name()
			
			assertEquals  'StudyEventRef', studyEventRefName
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithStudyEventRefElementWithCorrectOID(){
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventRefOID = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0].@StudyEventOID.text()
			
			assertEquals  'SE_INTERCUR', studyEventRefOID
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithStudyEventRefHasCorrespondingStudyEventDef(){
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventDefElement = xml.Study.MetaDataVersion.StudyEventDef[0]
			def studyEventRefElement = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0]
			
			assertEquals  studyEventDefElement.@OID, studyEventRefElement.@StudyEventOID
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithStudyEventDefHavingFormRef() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[0].children()

			assertNotNull formRefs
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithFirstStudyEventDefHavingONEFormRef() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[0].children()[0]

			assertEquals 1, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithStudyEventDefHavingFormRefWithFormOID_F_MBA2_1() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[0].children()[0]

			assertEquals 'F_MBA2_1', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSECONDStudyEventDefHavingFormRefs() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[1].children()

			assertNotNull formRefs
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSECONDStudyEventDefHavingTWOFormRefs() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[1].children()

			assertEquals 2, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSECONDStudyEventDefHavingFormRefWithFormOID_F_CSAC_3() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[1].children()[0]

			assertEquals 'F_CSAC_3', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSECONDStudyEventDefHavingFormRefWithFormOID_F_MSA2_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[1].children()[1]

			assertEquals 'F_MSA2_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithTHIRDStudyEventDefHavingTWOFormRefs() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[2].children()

			assertEquals 2, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithTHIRDStudyEventDefHavingFormRefWithFormOID_F_AEAD_3() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[2].children()[0]

			assertEquals 'F_AEAD_3', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithTHIRDStudyEventDefHavingFormRefWithFormOID_F_SAES_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[2].children()[1]

			assertEquals 'F_SAES_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithFOURTHStudyEventDefHavingONEFormRef() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[3].children()

			assertEquals 1, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithFOURTHStudyEventDefHavingFormRefWithFormOID_F_SAES_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[3].children()[0]

			assertEquals 'F_MSA1_3', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithFIFTHStudyEventDefHavingONEFormRef() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[4].children()

			assertEquals 1, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithFIFTHStudyEventDefHavingFormRefWithFormOID_F_CILC_1() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[4].children()[0]

			assertEquals 'F_CILC_1', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSIXTHStudyEventDefHavingTWOFormRefs() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[5].children()

			assertEquals 3, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSIXTHStudyEventDefHavingFormRefWithFormOID_F_MEAM_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[5].children()[0]

			assertEquals 'F_MEAM_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSIXTHStudyEventDefHavingFormRefWithFormOID_F_CEAC_4() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[5].children()[1]

			assertEquals 'F_CEAC_4', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSIXTHStudyEventDefHavingFormRefWithFormOID_F_CSDI_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[5].children()[2]

			assertEquals 'F_CSDI_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSEVENTHStudyEventDefHavingONEFormRef() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[6].children()

			assertEquals 1, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithSEVENTHStudyEventDefHavingFormRefWithFormOID_F_CFSC_1() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[6].children()[0]

			assertEquals 'F_CFSC_1', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGHTHStudyEventDefHavingTENFormRefs() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()

			assertEquals 10, formRefs.size()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CCAC_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[0]

			assertEquals 'F_CCAC_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CCMC_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[1]

			assertEquals 'F_CCMC_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CMIC_1() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[2]

			assertEquals 'F_CMIC_1', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_MLLM_5() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[3]

			assertEquals 'F_MLLM_5', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_MHIV_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[4]

			assertEquals 'F_MHIV_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CLLC_4() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[5]

			assertEquals 'F_CLLC_4', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_MMLM_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[6]

			assertEquals 'F_MMLM_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CBLC_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[7]

			assertEquals 'F_CBLC_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_CSDC_2() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[8]

			assertEquals 'F_CSDC_2', formRefs.@FormOID.text()
		}
	}
	
	@Test void testFindCRFSReturnsResponseWithEIGTHStudyEventDefHavingFormRefWithFormOID_F_MBA1_1() {
		play{

			def response = crfMetaDataVersionProxy.findAllCRFS('OID')
			def xml = new XmlSlurper().parseText(response)

			def formRefs = xml.Study.MetaDataVersion.StudyEventDef[7].children()[9]

			assertEquals 'F_MBA1_1', formRefs.@FormOID.text()
		}
	}
	
	private def setUpConnectionFactoryMock() {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod('POST')
		connection.setRequestProperty('Content-Type', 'text/xml')
		connection.setRequestProperty('Content-Length', is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns('mock url')

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(TestData.getCRFWebServiceResponse().getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getCRFConnection().returns(connection)
		return factory
	}
}

