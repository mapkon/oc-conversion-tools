package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.TestUtils
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class StudyMetaDataWebServiceProxyTest extends GroovyTestCase {

	def getMetadataProxy

	@Before void setUp(){

		def connectionFactory = setUpConnectionFactoryMock(TestUtils.metaDataReturnSOAPResponse)
		getMetadataProxy = new StudyMetaDataWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:connectionFactory)
	}

	@Test void testGetMetaDataShouldNotReturnNull(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			assertNotNull response
		}
	}
	
	@Test void testGetEnvelopeHasCorrectStudyPath(){

		def envelope = getMetadataProxy.getSoapEnvelope()
		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/study/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}

	@Test void testGetMetaDataShouldReturnValidResponseWithODMRoot(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)
			assertEquals 'ODM', xml.name()
		}
	}

	@Test void testGetMetaDataShouldReturnValidResponseWithStudyElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyRootElementName = xml.Study[0].name()
			assertEquals  'Study', studyRootElementName
		}
	}

	@Test void testGetMetaDataShouldReturnValidResponseWithStudyOID(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyOID = xml.Study[0].@OID.text()
			assertEquals  'S_001', studyOID
		}
	}

	@Test void testGetMetaDataShouldReturnValidResponseWithStudyNameElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyNameElementName = xml.Study.GlobalVariables.StudyName[0].name()
			assertEquals  'StudyName', studyNameElementName
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyNameElementHavingCorrectName(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyNameElementText = xml.Study.GlobalVariables.StudyName[0].text()
			assertEquals  'Test Study', studyNameElementText
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyDescriptionElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyDescriptionElementName = xml.Study.GlobalVariables.StudyDescription[0].name()
			assertEquals  'StudyDescription', studyDescriptionElementName
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyDescriptionElementHavingCorrectDescription(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyDescriptionText = xml.Study.GlobalVariables.StudyDescription[0].text()
			assertEquals  'Morten wears glasses so I think he is a medical doctor', studyDescriptionText.trim()
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithProtocolNameElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolNameElement = xml.Study.GlobalVariables.ProtocolName[0].name()
			assertEquals  'ProtocolName', protocolNameElement
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithProtocolNameElementHavingCorrectProtocol(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolNameElement = xml.Study.GlobalVariables.ProtocolName[0].text()
			assertEquals  '001', protocolNameElement
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithMetaDataVersionElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def metaDataVersionElementName = xml.Study.MetaDataVersion[0].name()
			assertEquals  'MetaDataVersion', metaDataVersionElementName
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithMetaDataVersionElementWithCorrectAttributes(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def metaDataVersionElement = xml.Study.MetaDataVersion[0]
			
			assertEquals  'v1.0.0', metaDataVersionElement.@OID.text()
			assertEquals  'MetaDataVersion_v1.0.0', metaDataVersionElement.@Name.text()
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithProtocolElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def protocolElementName = xml.Study.MetaDataVersion.Protocol[0].name()
			
			assertEquals  'Protocol', protocolElementName
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithProtocolElementWithStudyEventRefElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventRefs = xml.Study.MetaDataVersion.Protocol[0].children()
			
			assertEquals  1, studyEventRefs.size()
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyEventRefElement(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventRefName = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0].name()
			
			assertEquals  'StudyEventRef', studyEventRefName
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyEventRefElementWithCorrectOID(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventRefOID = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0].@StudyEventOID.text()
			
			assertEquals  'SE_VISIT', studyEventRefOID
		}
	}
	
	@Test void testGetMetaDataShouldReturnValidResponseWithStudyEventRefHasCorrespondingStudyEventDef(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			def xml = new XmlSlurper().parseText(response)

			def studyEventDefElement = xml.Study.MetaDataVersion.StudyEventDef[0]
			def studyEventRefElement = xml.Study.MetaDataVersion.Protocol.StudyEventRef[0]
			
			assertEquals  studyEventDefElement.@OID, studyEventRefElement.@StudyEventOID
		}
	}
	
	private def setUpConnectionFactoryMock(returnXml) {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod('POST')
		connection.setRequestProperty('Content-Type', 'text/xml')
		connection.setRequestProperty('Content-Length', is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns('mock url')

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection)

		return factory
	}
}
