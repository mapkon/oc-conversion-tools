package org.openxdata.oc.transport.proxy;

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class ListAllByStudyWebServiceProxyTest {

	def listAllByStudyProxy
	@Before void setUp(){
		def connectionFactory = setUpConnectionFactoryMock(studySubjectListSOAPResponse)
		listAllByStudyProxy = new ListAllByStudyWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:connectionFactory)
	}

	@Test void testListAllByStudyShouldNotReturnNull(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertNotNull subjects
		}
	}

	@Test void testListAllByStudyReturnsCorrectNumberOfSubjects(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertEquals 4, subjects.size()
		}
	}

	@Test void testListAllByStudyReturnsValidSubjectKeys(){
		play{

			def subjects = listAllByStudyProxy.listAllByStudy("identifier")
			assertEquals 'SS_Morten', subjects[0]
			assertEquals 'SS_J¿rn', subjects[1]
			assertEquals 'SS_Jonny', subjects[2]
			assertEquals 'SS_Janne', subjects[3]
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
		factory.getStudySubjectConnection().returns(connection)

		return factory
	}

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
														   <ns2:uniqueIdentifier>SS_Morten</ns2:uniqueIdentifier>
														   <ns2:gender>m</ns2:gender>
														   <ns2:dateOfBirth>2011-09-06</ns2:dateOfBirth>
														</ns2:subject>
														<ns2:events/>
													 </ns2:studySubject>
													 <ns2:studySubject>
														<ns2:label>J¿rnrn</ns2:label>
														<ns2:secondaryLabel/>
														<ns2:enrollmentDate>2011-09-08</ns2:enrollmentDate>
														<ns2:subject>
														   <ns2:uniqueIdentifier>SS_J¿rn</ns2:uniqueIdentifier>
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
}
