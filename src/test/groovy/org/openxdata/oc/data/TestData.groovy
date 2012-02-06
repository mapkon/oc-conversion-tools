package org.openxdata.oc.data

import org.junit.Ignore
import org.openxdata.oc.util.TransformUtil


@Ignore("Some maven installation will attempt to run this as a test")
class TestData {

	static def getReturnXml() {
		
		def response = new TransformUtil().loadFileContents('CRFRequestResponse.xml')
	}
	
	static def getInstanceData() {
		
		def instanceData = []
		
		def testInstanceData = """<test_study_se_visit_visit-v1 xmlns="" Description="converted from ODM to Xform" formKey="test_study_se_visit_visit-v1" id="10" name="SE_VISIT_Visit-v1">
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
								</test_study_se_visit_visit-v1>"""
		
		instanceData.add(testInstanceData)
		
		return instanceData
	
	}
	
	static def getOpenXdataInstanceData() {
		
		def instanceData = []
		def oxdInstanceData = '''
								<ODM id="S_12175 - SE_SC2" StudyOID="S_12175" Description="This Xform was converted from an ODM file using the oc-conversion-tools" formKey="SE_SC2" FormOID="F_MSA2_2" name="SC2" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG" StudyEventOID="SE_SC2" MetaDataVersionOID="v1.0.0" xmlns:xf="http://www.w3.org/2002/xforms">
								  <xf:IG_MSA2_MSA2_POARTPRECG xmlns:xf="http://www.w3.org/2002/xforms">
								    <xf:I_MSA2_MSA2_POARTPREC>Chloroquin</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV>3</xf:I_MSA2_MSA2_POARTNBV>
								  </xf:IG_MSA2_MSA2_POARTPRECG>
								  <xf:I_MSA2_INIT xmlns:xf="http://www.w3.org/2002/xforms">MGM</xf:I_MSA2_INIT>
								  <xf:I_MSA2_FROMD xmlns:xf="http://www.w3.org/2002/xforms">2012-02-03</xf:I_MSA2_FROMD>
								  <xf:I_MSA2_IDV xmlns:xf="http://www.w3.org/2002/xforms">009</xf:I_MSA2_IDV>
								  <xf:I_MSA2_MSA2_INITBF xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_INITBF>
								  <xf:I_MSA2_MSA2_INTBF xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_INTBF>
								  <xf:I_MSA2_MSA2_HAART xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_HAART>
								  <xf:I_MSA2_MSA2_ELHAART xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_ELHAART>
								  <xf:I_MSA2_MSA2_PAZT xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_PAZT>
								  <xf:I_MSA2_MSA2_PAZTNB xmlns:xf="http://www.w3.org/2002/xforms">9</xf:I_MSA2_MSA2_PAZTNB>
								  <xf:I_MSA2_MSA2_POART xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_POART>
								  <xf:I_MSA2_MSA2_LNVPV xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_LNVPV>
								  <xf:I_MSA2_MSA2_LAZTV xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_LAZTV>
								  <xf:I_MSA2_MSA2_L3TC xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_L3TC>
								  <xf:I_MSA2_MSA2_LOART xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_LOART>
								  <xf:I_MSA2_MSA2_LOCATB xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_LOCATB>
								  <xf:I_MSA2_MSA2_OLOCATBPREC xmlns:xf="http://www.w3.org/2002/xforms">kololo</xf:I_MSA2_MSA2_OLOCATBPREC>
								  <xf:I_MSA2_MSA2_TRANSF xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_TRANSF>
								  <xf:I_MSA2_MSA2_TYPED xmlns:xf="http://www.w3.org/2002/xforms">1</xf:I_MSA2_MSA2_TYPED>
								  <xf:I_MSA2_MSA2_PMTCTV xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_PMTCTV>
								  <xf:I_MSA2_MSA2_PMTCT xmlns:xf="http://www.w3.org/2002/xforms">0</xf:I_MSA2_MSA2_PMTCT>
								  <xf:I_MSA2_MSA2_LAZTNBV xmlns:xf="http://www.w3.org/2002/xforms">7</xf:I_MSA2_MSA2_LAZTNBV>
								  <xf:I_MSA2_MSA2_L3TCNB xmlns:xf="http://www.w3.org/2002/xforms">7</xf:I_MSA2_MSA2_L3TCNB>
								</ODM> '''
		
		instanceData.add(oxdInstanceData)

		return instanceData
	}

	static def studySubjectListSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
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
														  <ns2:uniqueIdentifier>SS_Morten</ns2:uniqueIdentifier>
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
														  <ns2:uniqueIdentifier>SS_Jørn</ns2:uniqueIdentifier>
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
														  <ns2:uniqueIdentifier>SS_Jonny</ns2:uniqueIdentifier>
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
														  <ns2:uniqueIdentifier>SS_Janne</ns2:uniqueIdentifier>
														  <ns2:gender>m</ns2:gender>
														  <ns2:dateOfBirth>2011-09-16</ns2:dateOfBirth>
													   </ns2:subject>
													   <ns2:events/>
													</ns2:studySubject>
												 </ns4:studySubjects>
											  </ns4:listAllByStudyResponse>
										   </SOAP-ENV:Body>
										</SOAP-ENV:Envelope>"""

	static def importSOAPSuccessResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										   <SOAP-ENV:Header/>
										   <SOAP-ENV:Body>
											  <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
												 <result>Success</result>
											  </importDataResponse>
										   </SOAP-ENV:Body>
										</SOAP-ENV:Envelope>'''

	static def importSOAPErrorResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									   <SOAP-ENV:Header/>
									   <SOAP-ENV:Body>
									      <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
									         <result>Fail</result>
									         <error>Error.</error>
									      </importDataResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>'''
}
