package org.openxdata.oc.transport

import static org.hamcrest.Matchers.*
import groovy.mock.interceptor.MockFor

import org.gmock.WithGMock
import org.junit.Test
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.exception.UnAvailableException
import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl


@WithGMock
class OpenClinicaSoapClientTest extends GroovyTestCase {

	def username = "user"
	def password = "pass"
	def instanceData = []
	
	void setUp(){
		instanceData.add("""<?xml version="1.0" encoding="UTF-8"?>
							<test_study_se_visit_visit-v1 xmlns="" Description="converted from ODM to Xform" formKey="test_study_se_visit_visit-v1" id="10" name="SE_VISIT_Visit-v1">
							  <ClinicalData xmlns="http://www.w3.org/2002/xforms" MetaDataVersionOID="v1.0.0" StudyOID="S_001">
								<SubjectData SubjectKey="SS_MARK">
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
							</test_study_se_visit_visit-v1>""")
	}
	
	@Test void testListAllMUSTReturnListOfCorrectSize() {
		def factory = setUpConnectionFactoryMock(listAllReturnSOAPResponse)

		play {
			
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def all = client.listAll()
			assertEquals 2, all.size()
		}
	}

	@Test void testListAllMUSTReturnValidOpenclinicaStudies() {
		def factory = setUpConnectionFactoryMock(listAllReturnSOAPResponse)

		play {
			
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def actual = client.listAll()
			
			def study1 = new ConvertedOpenclinicaStudy(identifier: "default-study", OID: "S_DEFAULTS1", name:"Default Study")
			def study2 = new ConvertedOpenclinicaStudy(identifier: "001", OID: "S_001", name: "Test Study")
			
			assertEquals actual[0], study1
			assertEquals actual[1], study2
		}
	}

	@Test void testGetMetaDataMUSTReturnCorrectStudy() {
		def factory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		
		play{
			
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def response = client.getMetadata("001")
			def xml = new XmlSlurper().parseText(response).declareNamespace(oc: "http://www.cdisc.org/ns/odm/v1.3")
						
			assertEquals  "ODM", xml.name()
			assertEquals  "S_001", xml.Study[0].@OID.text()
		}	
	}
	
	@Test void testGetOpenxdataFormMUSTReturnValidForm() {
		def factory = setUpConnectionFactoryMock(metaDataReturnSOAPResponse)
		play {
			
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def convertedStudyXml = client.getOpenxdataForm("001")

			def forms = convertedStudyXml.children()
			def version = forms.children()
			
			assertEquals  "Test Study", convertedStudyXml.@name.text()
			
			assertEquals 1, forms.size()
			assertEquals  "SE_VISIT", forms[0].@name.text()			
			assertEquals  "Visit-v1", version.@name.text()
			assertEquals  "xform", forms.xform[0].name()
			
		}
	}
	
	@Test void testGetSubjectKeysSHOULDReturnSubjectKeys(){
		def factory = setUpConnectionFactoryMock(studySubjectListSOAPResponse)
		play{
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def subjectKeys = client.getSubjectKeys("default-study")
			
			assertNotNull subjectKeys
			assertEquals 4, subjectKeys.size()
		}
	}
	
	@Test void testWrongURLMUSTThrowUnAvailableException(){

		def mock = new MockFor(ConnectionFactory)
		mock.demand.getStudyConnection { throw new UnAvailableException("Incorrect url") }
		def factory = mock.proxyInstance()
		shouldFail(UnAvailableException){

			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			client.listAll()
		}
	}
	
	@Test void testThatImportDataReturnsSuccessResponseOnCorrectODMFormat(){
		
		def factory = setUpConnectionFactoryMock(importSOAPSuccessResponse)
		play{
			
			def client = new OpenClinicaSoapClientImpl(username, password)
			client.setConnectionFactory(factory)
			def reponse = client.importData(instanceData)
			
			assertNotNull reponse
			assertEquals 'Success', reponse
		}
	}
	
	@Test void testThatImportDataReturnsErrorOnIncorrectODM(){
		def factory = setUpConnectionFactoryMock(importSOAPErrorResponse)
		play{

			shouldFail(ImportException){
				def client = new OpenClinicaSoapClientImpl(username, password, factory)
				def reponse = client.importData(instanceData)
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
		
		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection).atMostOnce()
		factory.getStudySubjectConnection().returns(connection).atMostOnce()
		
		return factory
	}
	
	def listAllReturnSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
								<SOAP-ENV:Header/>
								<SOAP-ENV:Body>
								   <listAllResponse xmlns="http://openclinica.org/ws/study/v1">
									  <result>Success</result>
									  <studies>
										 <study>
											<identifier>default-study</identifier>
											<oid>S_DEFAULTS1</oid>
											<name>Default Study</name>
										 </study>
										 <study>
											<identifier>001</identifier>
											<oid>S_001</oid>
											<name>Test Study</name>
										 </study>
									  </studies>
								   </listAllResponse>
								</SOAP-ENV:Body>
							 </SOAP-ENV:Envelope>"""
	
 	def metaDataReturnSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
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
								</SOAP-ENV:Envelope>"""
	 
	def studySubjectListSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
								   <SOAP-ENV:Header/>
								   <SOAP-ENV:Body>
								      <ns4:listAllByStudyResponse xmlns:ns4="http://openclinica.org/ws/studySubject/v1" xmlns:ns2="http://openclinica.org/ws/beans" xmlns:ns3="http://openclinica.org/ws/crf/v1">
								         <ns4:result>Success</ns4:result>
								         <ns4:studySubjects>
								            <ns2:studySubject>
								               <ns2:label>Morten</ns2:label>
								               <ns2:secondaryLabel/>
								               <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
								               <ns2:subject>
								                  <ns2:uniqueIdentifier>bend</ns2:uniqueIdentifier>
								                  <ns2:gender>m</ns2:gender>
								                  <ns2:dateOfBirth>2011-09-06</ns2:dateOfBirth>
								               </ns2:subject>
								               <ns2:events/>
								            </ns2:studySubject>
								            <ns2:studySubject>
								               <ns2:label>jorn</ns2:label>
								               <ns2:secondaryLabel/>
								               <ns2:enrollmentDate>2011-09-08</ns2:enrollmentDate>
								               <ns2:subject>
								                  <ns2:uniqueIdentifier>ivar</ns2:uniqueIdentifier>
								                  <ns2:gender>m</ns2:gender>
								                  <ns2:dateOfBirth>2011-09-29</ns2:dateOfBirth>
								               </ns2:subject>
								               <ns2:events/>
								            </ns2:studySubject>
								            <ns2:studySubject>
								               <ns2:label>jonny</ns2:label>
								               <ns2:secondaryLabel/>
								               <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
								               <ns2:subject>
								                  <ns2:uniqueIdentifier>jonny</ns2:uniqueIdentifier>
								                  <ns2:gender>m</ns2:gender>
								                  <ns2:dateOfBirth>2011-09-12</ns2:dateOfBirth>
								               </ns2:subject>
								               <ns2:events/>
								            </ns2:studySubject>
								            <ns2:studySubject>
								               <ns2:label>janne</ns2:label>
								               <ns2:secondaryLabel/>
								               <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
								               <ns2:subject>
								                  <ns2:uniqueIdentifier>janne</ns2:uniqueIdentifier>
								                  <ns2:gender>m</ns2:gender>
								                  <ns2:dateOfBirth>2011-09-16</ns2:dateOfBirth>
								               </ns2:subject>
								               <ns2:events/>
								            </ns2:studySubject>
								         </ns4:studySubjects>
								      </ns4:listAllByStudyResponse>
								   </SOAP-ENV:Body>
								</SOAP-ENV:Envelope>"""
	
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
