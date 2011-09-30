package org.openxdata.oc

import static org.hamcrest.Matchers.*
import groovy.xml.XmlUtil;

import org.gmock.WithGMock
import org.openxdata.oc.transport.OpenClinicaSoapClientImpl
import org.openxdata.oc.transport.factory.ConnectionFactory


@WithGMock
class OpenClinicaSoapClientTest extends GroovyTestCase {

	def username = "user"
	def password = "pass"
	
	void testListAllMUSTReturnListOfCorrectSize() {
		def factory = setUpMocks(listAllReturnXML)

		play {
			
			def soapClient = new OpenClinicaSoapClientImpl(factory, username, password)
			def all = soapClient.listAll()
			assertEquals 2, all.size()
		}
	}

	void testListAllMUSTReturnValidOpenclinicaStudies() {
		def factory = setUpMocks(listAllReturnXML)

		play {
			
			def soapClient = new OpenClinicaSoapClientImpl(factory, username, password)
			def all = soapClient.listAll()

			def xml = new XmlParser().parseText(listAllReturnXML)
			def studies = xml.depthFirst().study
			
			assertEquals all[0].OID, studies[0].oid.text()
			assertEquals all[0].name,studies[0].name.text()
			assertEquals all[0].identifier,studies[0].identifier.text()

			assertEquals all[1].OID, studies[1].oid.text()
			assertEquals all[1].name,studies[1].name.text()
			assertEquals all[1].identifier,studies[1].identifier.text()
		}
	}

	void testGetMetaDataMUSTReturnCorrectStudy() {
		def factory = setUpMocks(getMetaDataReturnXML)
		
		play{
			
			def soapClient = new OpenClinicaSoapClientImpl(factory, username, password)
			def response = soapClient.getMetadata("001")
			def xml = new XmlSlurper().parseText(response).declareNamespace(oc: "http://www.cdisc.org/ns/odm/v1.3")
						
			assertEquals  "ODM", xml.name()
			assertEquals  "S_001", xml.Study[0].@OID.text()
		}	
	}
	
	void testGetOpenxdataFormMUSTReturnValidForm() {
		def factory = setUpMocks(getMetaDataReturnXML)
		play {
			
			def soapClient = new OpenClinicaSoapClientImpl(factory, username, password)
			def response = soapClient.getOpenxdataForm("001", ['Jonny','Jorn', 'Janne','Morten'])
			def xml = new XmlParser().parseText(response)

			assertEquals  "study", xml.name()
			assertEquals  "form", xml.form[0].name()
			assertEquals  "version", xml.form.version[0].name()
			assertEquals  "xform", xml.form.version.xform[0].name()
			
		}
	}
	
	void testGetSubjectKeysSHOULDReturnSubjectKeys(){
		def factory = setUpMocks(studySubjectListXML)
		play{
			def soapClient = new OpenClinicaSoapClientImpl(factory, username, password)
			def subjectKeys = soapClient.getSubjectKeys("default-study")
			
			assertNotNull subjectKeys
			assertEquals 4, subjectKeys.size()
		}
	}

	private setUpMocks(def returnXml) {
		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST")
		connection.setRequestProperty("Content-Type", "text/xml")
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class)))
		connection.setDoOutput(true)
		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes()))
		def factory = mock(ConnectionFactory.class)
		factory.getConnection().returns(connection)
		return factory
	}
	
	
	def listAllReturnXML = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
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

 	def getMetaDataReturnXML = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
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

	def studySubjectListXML = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
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
}
