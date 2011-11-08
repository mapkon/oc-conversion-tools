package org.openxdata.oc.transport.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class StudyMetaDataWebServiceProxyTest extends GroovyTestCase {

	def getMetadataProxy

	@Before void setUp(){

		def connectionFactory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		getMetadataProxy = new StudyMetaDataWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:connectionFactory)
	}

	@Test void testGetMetaDataShouldNotReturnNull(){
		play{

			def response = getMetadataProxy.getMetaData('OID')
			assertNotNull response
		}
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

	def metaDataReturnSOAPResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										<SOAP-ENV:Header/>
										<SOAP-ENV:Body>
										   <createResponse xmlns="http://openclinica.org/ws/study/v1">
											  <result>Success</result>
											  <odm><![CDATA[<Study OID="S_001">
											 <GlobalVariables>
												 <StudyName>Test Study</StudyName>
												 <StudyDescription>
													 Morten wears glasses so I think he is a medical doctor
												 </StudyDescription>
												 <ProtocolName>001</ProtocolName>
											 </GlobalVariables>
											 <BasicDefinitions>
												 <MeasurementUnit OID="MU_NOUNITS" Name="no units">
													 <Symbol>
														 <TranslatedText>no units</TranslatedText>
													 </Symbol>
												 </MeasurementUnit>
											 </BasicDefinitions>
											 <MetaDataVersion OID="v1.0.0" Name="MetaDataVersion_v1.0.0">
												 <Protocol>
													 <StudyEventRef StudyEventOID="SE_VISIT" OrderNumber="1" Mandatory="Yes"/>
												 </Protocol>
												 <StudyEventDef OID="SE_VISIT"  Name="Visit" Repeating="No" Type="Scheduled">
													 <FormRef FormOID="F_SAMPLECRF_1" Mandatory="Yes"/>
												 </StudyEventDef>
												 <FormDef OID="F_SAMPLECRF_1" Name="SampleCRF - 1" Repeating="No">
													 <ItemGroupRef ItemGroupOID="IG_SAMPL_UNGROUPED" Mandatory="Yes"/>
													 <ItemGroupRef ItemGroupOID="IG_SAMPL_GROUP01" Mandatory="No"/>
												 </FormDef>
												 <ItemGroupDef OID="IG_SAMPL_UNGROUPED" Name="IG_SAMPL_UNGROUPED" Repeating="No" SASDatasetName="UNGROUPE">
													 <ItemRef ItemOID="I_SAMPL_SC_ITEM_01" OrderNumber="1" Mandatory="No"/>
													 <ItemRef ItemOID="I_SAMPL_SC_ITEM_02" OrderNumber="2" Mandatory="Yes"/>
												 </ItemGroupDef>
												 <ItemGroupDef OID="IG_SAMPL_GROUP01" Name="Group01" Repeating="Yes" SASDatasetName="GROUP01" Comment="Repeating items">
													 <ItemRef ItemOID="I_SAMPL_SC_REPEATING_ITEM_01" OrderNumber="1" Mandatory="No"/>
													 <ItemRef ItemOID="I_SAMPL_SC_REPEATING_ITEM_02" OrderNumber="2" Mandatory="No"/>
												 </ItemGroupDef>
												 <ItemDef OID="I_SAMPL_SC_ITEM_01" Name="SC_ITEM_01" DataType="text" Length="6" SASFieldName="SC_ITEM_" Comment="Sample CRF item 1">
													 <Question>
														 <TranslatedText>
														 This is an example of a text field  - This item is not required
														 </TranslatedText>
													 </Question>
													 <MeasurementUnitRef MeasurementUnitOID="MU_NOUNITS"/>
												 </ItemDef>
												 <ItemDef OID="I_SAMPL_SC_ITEM_02" Name="SC_ITEM_02" DataType="text" Length="2" SASFieldName="SC_IT001" Comment="Sample CRF item 2">
													 <Question>
														 <TranslatedText>
														 And this of a &lt;b&gt;text&lt;/b&gt; area  - This item is required
														 </TranslatedText>
													 </Question>
												 </ItemDef>
												 <ItemDef OID="I_SAMPL_SC_REPEATING_ITEM_01" Name="SC_REPEATING_ITEM_01" DataType="date" SASFieldName="SC_REPEA" Comment="Item 1 in a grid/table">
													 <Question>
														 <TranslatedText>
														 Date
														 </TranslatedText>
													 </Question>
												 </ItemDef>
												 <ItemDef OID="I_SAMPL_SC_REPEATING_ITEM_02" Name="SC_REPEATING_ITEM_02" DataType="integer" Length="10" SASFieldName="SC_RE002" Comment="Item 2 in a grid/table">
													 <Question>
														 <TranslatedText>
														 Score
														 </TranslatedText>
													 </Question>
													 <RangeCheck Comparator="GE" SoftHard="Soft">
														 <CheckValue>0</CheckValue>
														 <ErrorMessage><TranslatedText>Score must be an integer between 0 and 7</TranslatedText></ErrorMessage>
													 </RangeCheck>
													 <RangeCheck Comparator="LE" SoftHard="Soft">
														 <CheckValue>7</CheckValue>
														 <ErrorMessage><TranslatedText>Score must be an integer between 0 and 7</TranslatedText></ErrorMessage>
													 </RangeCheck>
												 </ItemDef>
											 </MetaDataVersion>
										 </Study>]]></odm>
										   </createResponse>
										</SOAP-ENV:Body>
									 </SOAP-ENV:Envelope>'''
}
