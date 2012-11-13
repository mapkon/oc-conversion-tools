package org.openxdata.oc.data

import org.junit.Ignore
import org.openxdata.oc.model.StudySubject
import org.openxdata.oc.util.TransformUtil
import org.openxdata.server.admin.model.FormData


@Ignore("Some maven installations will attempt to run this as a test")
class TestData {

	static def createImportMessages(){

		def messages = [:]

		messages.put("F_MSA2_1",'Success')
		messages.put("key1", "Fail: Incorrect FormData OID")
		messages.put("F_MSA2_2", 'Success')
		messages.put("key3", 'Fail: Subject key not found')

		return messages
	}

	static def getCRFWebServiceResponse() {

		def response = new TransformUtil().loadFileContents("crf-metadata-response.xml")
	}

	static def getConvertedXform() {

		def response = new TransformUtil().loadFileContents("test-converted-xform.xml")

		return new XmlSlurper().parseText(response)
	}

	static def getOpenXdataInstanceData() {

		def instanceData = '''
								<ODM StudyOID="S_12175" MetaDataVersionOID="v1.0.0" Description="This Xform was converted from an ODM file using the oc-conversion-tools" formKey="F_MSA2_1" name="SC2" StudyEventOID="SE_SC2" id="7" xmlns:xf="http://www.w3.org/2002/xforms">
								  <subjectkey>Foo_Key</subjectkey>
								  <I_MSA2_INIT ItemGroupOID="IG_MSA2_UNGROUPED">CTK</I_MSA2_INIT>
								  <I_MSA2_FROMD ItemGroupOID="IG_MSA2_UNGROUPED">2012-02-07</I_MSA2_FROMD>
								  <I_MSA2_IDV ItemGroupOID="IG_MSA2_UNGROUPED">009</I_MSA2_IDV>
								  <I_MSA2_MSA2_INITBF ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_INITBF>
								  <I_MSA2_MSA2_INTBF ItemGroupOID="IG_MSA2_UNGROUPED">3</I_MSA2_MSA2_INTBF>
								  <I_MSA2_MSA2_HAART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_HAART>
								  <I_MSA2_MSA2_ELHAART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_ELHAART>
								  <I_MSA2_MSA2_PAZT ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PAZT>
								  <I_MSA2_MSA2_PAZTNB ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_PAZTNB>
								  <I_MSA2_MSA2_POART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_POART>
								  <I_MSA2_MSA2_LNVPV ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LNVPV>
								  <I_MSA2_MSA2_LAZTV ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_LAZTV>
								  <I_MSA2_MSA2_L3TC ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_L3TC>
								  <I_MSA2_MSA2_LOART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LOART>
								  <I_MSA2_MSA2_LOCATB ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_LOCATB>
								  <I_MSA2_MSA2_OLOCATBPREC ItemGroupOID="IG_MSA2_UNGROUPED">None</I_MSA2_MSA2_OLOCATBPREC>
								  <I_MSA2_MSA2_TRANSF ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_TRANSF>
								  <I_MSA2_MSA2_TYPED ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_TYPED>
								  <I_MSA2_MSA2_PMTCTV ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_PMTCTV>
								  <I_MSA2_MSA2_PMTCT ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PMTCT>
								  <I_MSA2_MSA2_LAZTNBV ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_LAZTNBV>
								  <I_MSA2_MSA2_L3TCNB ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_L3TCNB>
								  <I_MSA2_MSA2_L3TCNB2 ItemGroupOID="IG_MSA2_UNGROUPED_2">4</I_MSA2_MSA2_L3TCNB2>
								  <I_MSA2_MSA2_HAART_T ItemGroupOID="IG_MSA2_MSA2_POARTPRECG2">0</I_MSA2_MSA2_HAART_T>

								  <IG_MSA2_MSA2_POARTPRECG>
								    <xf:I_MSA2_MSA2_POARTPREC>Chloroquine</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV>3</xf:I_MSA2_MSA2_POARTNBV>
									<xf:I_MSA2_MSA2_POARTNBV_MULT>1 2 3 4</xf:I_MSA2_MSA2_POARTNBV_MULT>
								  </IG_MSA2_MSA2_POARTPRECG>
								  <IG_MSA2_MSA2_POARTPRECH>
									<xf:I_MSA2_MSA2_POARTPREC>Quinin</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV>2</xf:I_MSA2_MSA2_POARTNBV>
									<xf:I_MSA2_MSA2_POARTNBV_MULT>1 2 3 4</xf:I_MSA2_MSA2_POARTNBV_MULT>
								  </IG_MSA2_MSA2_POARTPRECH>
								  <IG_MSA2_MSA2_POARTPRECG>
									<xf:I_MSA2_MSA2_POARTPREC>Panadol</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV>4</xf:I_MSA2_MSA2_POARTNBV>
									<xf:I_MSA2_MSA2_POARTNBV_MULT>1 2 3 4</xf:I_MSA2_MSA2_POARTNBV_MULT>
								  </IG_MSA2_MSA2_POARTPRECG>
								</ODM>'''

		return instanceData
	}

	static List<FormData> getInstanceData() {

		def formData = new FormData()
		formData.setData(TestData.getOpenXdataInstanceData())

		def instanceDataList = []
		instanceDataList.add(formData)

		return instanceDataList
	}

	static def getStudySubjectEventWebServiceResponse() {

		def events = new TransformUtil().loadFileContents("subject-event-response.xml")
	}

	static def getStudySubjects() {

		def response = new TransformUtil().loadFileContents("subject-event-response.xml")

		def xml = new XmlSlurper().parseText(response)

		return xml.depthFirst().find { it.name().equals("studySubjects") }
	}

	static List<StudySubject> getStudySubjectsAsList() {

		def subjects = []

		def subjectEventNode = getStudySubjects()

		subjectEventNode.studySubject.each {

			def subject = new StudySubject(it)
			subjects.add(subject)
		}

		return subjects
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
									         <error>Subject key not found</error>
									      </importDataResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>'''
	
	static def userDetailsResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										   <SOAP-ENV:Header/>
										   <SOAP-ENV:Body>
										      <findUserResponse xmlns="http://openclinica.org/ws/data/v1">
										         <result>Success</result>
										         <username>foo</username>
										         <hashedPassword>hash LoL</hashedPassword>
										         <canUseWebservices>false</canUseWebservices>
										         <allowedStudyOid>S_DEFAULTS1</allowedStudyOid>
										         <allowedStudyOid>S_123456</allowedStudyOid>
										      </findUserResponse>
										   </SOAP-ENV:Body>
										</SOAP-ENV:Envelope>'''
	
	static def getFindUserResponse() {
		
		def xml = new XmlSlurper().parseText(userDetailsResponse)
		
		return xml.depthFirst().find {it.name().equals("findUserResponse")}
	}

	static def nonExistingUserResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									   <SOAP-ENV:Header/>
									   <SOAP-ENV:Body>
									      <findUserResponse xmlns="http://openclinica.org/ws/data/v1">
									         <result>Fail</result>
									         <error>This user does not exist in the database.</error>
									      </findUserResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>'''

	static def getUserDetailsPermissionUserResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									   <SOAP-ENV:Header/>
									   <SOAP-ENV:Body>
									      <findUserResponse xmlns="http://openclinica.org/ws/data/v1">
									         <result>Fail</result>
									         <error>You do not have enough privileges to retrieve this information.</error>
									      </findUserResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>'''
}
