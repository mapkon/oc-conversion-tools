package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*
import groovy.util.GroovyTestCase

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
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

			assertEquals 'S_12175', studyOID.text()
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

			assertEquals 'Uganda', studyNameText
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

			assertEquals 'Site of Uganda', studyDescriptionElement.text().trim()
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

			assertEquals '12175', protocolNameElement.text()
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
	
	private def setUpConnectionFactoryMock() {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod('POST')
		connection.setRequestProperty('Content-Type', 'text/xml')
		connection.setRequestProperty('Content-Length', is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns('mock url')

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(TestUtils.getReturnXml().getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection)
		return factory
	}
}

