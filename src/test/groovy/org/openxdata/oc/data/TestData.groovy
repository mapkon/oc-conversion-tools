package org.openxdata.oc.data

import org.junit.Ignore
import org.openxdata.oc.util.TransformUtil


@Ignore("Some maven installation will attempt to run this as a test")
class TestData {

	static def getReturnXml() {
		
		def response = new TransformUtil().loadFileContents('CRFRequestResponse.xml')
	}
	
	static def getOpenXdataInstanceData() {
		
		def instanceData = []
		def oxdInstanceData = '''
								<ODM StudyOID="S_12175" MetaDataVersionOID="v1.0.0" Description="This Xform was converted from an ODM file using the oc-conversion-tools" formKey="SE_SC2" name="SC2" StudyEventOID="SE_SC2" id="7" SubjectKey="Foo_Key" xmlns:xf="http://www.w3.org/2002/xforms">
								  <IG_MSA2_MSA2_POARTPRECG FormOID="F_MSA2_2" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">
								    <xf:I_MSA2_MSA2_POARTPREC xmlns:xf="http://www.w3.org/2002/xforms" FormOID="F_MSA2_2" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">Chloroquine</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV xmlns:xf="http://www.w3.org/2002/xforms" FormOID="F_MSA2_2" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">3</xf:I_MSA2_MSA2_POARTNBV>
								  </IG_MSA2_MSA2_POARTPRECG>
								  <I_MSA2_INIT FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">CTK</I_MSA2_INIT>
								  <I_MSA2_FROMD FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">2012-02-07</I_MSA2_FROMD>
								  <I_MSA2_IDV FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">009</I_MSA2_IDV>
								  <I_MSA2_MSA2_INITBF FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_INITBF>
								  <I_MSA2_MSA2_INTBF FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">3</I_MSA2_MSA2_INTBF>
								  <I_MSA2_MSA2_HAART FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_HAART>
								  <I_MSA2_MSA2_ELHAART FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_ELHAART>
								  <I_MSA2_MSA2_PAZT FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PAZT>
								  <I_MSA2_MSA2_PAZTNB FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_PAZTNB>
								  <I_MSA2_MSA2_POART FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_POART>
								  <I_MSA2_MSA2_LNVPV FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LNVPV>
								  <I_MSA2_MSA2_LAZTV FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_LAZTV>
								  <I_MSA2_MSA2_L3TC FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_L3TC>
								  <I_MSA2_MSA2_LOART FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LOART>
								  <I_MSA2_MSA2_LOCATB FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_LOCATB>
								  <I_MSA2_MSA2_OLOCATBPREC FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">None</I_MSA2_MSA2_OLOCATBPREC>
								  <I_MSA2_MSA2_TRANSF FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_TRANSF>
								  <I_MSA2_MSA2_TYPED FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_TYPED>
								  <I_MSA2_MSA2_PMTCTV FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_PMTCTV>
								  <I_MSA2_MSA2_PMTCT FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PMTCT>
								  <I_MSA2_MSA2_LAZTNBV FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_LAZTNBV>
								  <I_MSA2_MSA2_L3TCNB FormOID="F_MSA2_1" ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_L3TCNB>
								  <I_MSA2_MSA2_L3TCNB2 FormOID="F_MSA2_1_2" ItemGroupOID="IG_MSA2_UNGROUPED_2">4</I_MSA2_MSA2_L3TCNB2>
								  <I_MSA2_MSA2_HAART_T FormOID="F_MSA2_2" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">0</I_MSA2_MSA2_HAART_T>

								</ODM>
								 '''
		
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
