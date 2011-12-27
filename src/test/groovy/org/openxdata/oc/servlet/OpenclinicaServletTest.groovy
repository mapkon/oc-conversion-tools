package org.openxdata.oc.servlet

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.openxdata.oc.service.OpenclinicaService
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse

@RunWith(MockitoJUnitRunner.class)
class OpenclinicaServletTest extends GroovyTestCase {

	def study
	def request
	def response

	@Mock OpenclinicaService service
	@Mock OpenClinicaSoapClient client
	@InjectMocks def servlet = new OpenclinicaServlet(openclinicaService:service)

	@Before void setUp() {

		study = createStudy()

		Mockito.when(client.getOpenxdataForm('oid')).thenReturn(metaDataReturnSOAPResponse)
		Mockito.when(service.importOpenClinicaStudy('oid')).thenReturn(study)

		request = new MockHttpServletRequest()
		response = new MockHttpServletResponse()

		request.setParameter('oid', 'oid')
		request.setParameter('action', 'downloadAndConvert')
	}

	public def createStudy() {
		study = new StudyDef()
		study.setName('Test Study')

		def version = new FormDefVersion()

		def form = new FormDef()

		form.addVersion(version)

		study.addForm(form)

		return study
	}

	@Test public void testDownloadStudyDoesNotReturnNull() {

		servlet.doGet(request, response)

		def study = request.getSession().getAttribute('study')
		assertNotNull study
	}

	@Test public void testDownloadStudyReturnsValidStudyWithCorrectName() {

		servlet.doGet(request, response)

		def convertedStudy = request.getSession().getAttribute('study')
		assertEquals 'Test Study', convertedStudy.getName()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithForms() {

		servlet.doGet(request, response)

		def convertedStudy = request.getSession().getAttribute('study')
		assertEquals 1, convertedStudy.getForms().size()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithFormVersion() {

		servlet.doGet(request, response)

		def convertedStudy = request.getSession().getAttribute('study')
		assertEquals 1, convertedStudy.getForm(0).getVersions().size()
	}

	def metaDataReturnSOAPResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
											<SOAP-ENV:Header/>
											<SOAP-ENV:Body>
											   <createResponse xmlns="http://openclinica.org/ws/study/v1">
												  <result>Success</result>
													<odm><![CDATA[<?xml version="1.0" encoding="UTF-8"?>
													<ODM FileOID="Study-MetaD20111115202320+0200" Description="Study Metadata" CreationDateTime="2011-11-15T20:23:20+02:00" FileType="Snapshot"
													ODMVersion="1.3" xmlns="http://www.cdisc.org/ns/odm/v1.3" xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1"
													xmlns:OpenClinicaRules="http://www.openclinica.org/ns/rules/v3.1" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
													xsi:schemaLocation="http://www.cdisc.org/ns/odm/v1.3 OpenClinica-ODM1-3-0-OC2-0.xsd">
										
													<Study OID="S_001">
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
														 <ItemGroupDef OID="IG_SAMPL_UNGROUPED" Name="IG_SAMPL_UNGROUPED" Repeating="Yes" SASDatasetName="UNGROUPE">
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
													</Study>
												</ODM>]]></odm>
											   </createResponse>
											</SOAP-ENV:Body>
										 </SOAP-ENV:Envelope>'''
}
