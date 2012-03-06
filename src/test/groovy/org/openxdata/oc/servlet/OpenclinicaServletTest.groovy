package org.openxdata.oc.servlet

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.openxdata.oc.data.TestData
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

		Mockito.when(client.getOpenxdataForm('oid')).thenReturn(TestData.getCRFWebServiceResponse())
		Mockito.when(service.importOpenClinicaStudy('oid')).thenReturn(study)

		request = new MockHttpServletRequest()
		response = new MockHttpServletResponse()

		request.setParameter('oid', 'oid')
		request.setParameter('action', 'Import')
	}

	private def createStudy() {
		
		study = new StudyDef()
		study.setName('Test Study')
		study.setStudyKey('Test Key')
		
		def form = new FormDef()
		form.setName('Test Form')
		
		def version = new FormDefVersion()
		version.setName('Test Version')
		
		form.addVersion(version)

		study.addForm(form)

		return study
	}

	@Test public void testDownloadStudyDoesNotReturnNull() {

		servlet.doPost(request, response)

		def study = request.getAttribute('study')
		assertNotNull 'Study should not be null after successful Import',study
	}

	@Test public void testDownloadStudySetSuccessMessageOnSuccessfulImport() {

		servlet.doPost(request, response)

		def message = request.getAttribute('message')
		assertEquals 'Successful Import', message
	}
	
	@Test public void testDownloadStudyReturnsValidStudyWithCorrectNameInAttribute() {

		servlet.doPost(request, response)

		def name = request.getAttribute('name')
		assertEquals 'Test Study', name
	}

	@Test public void testDownloadStudyReturnsValidStudyWithCorrectKeyInAttribute() {

		servlet.doPost(request, response)

		def key = request.getAttribute('key')
		assertEquals 'Test Key', key
	}
	
	@Test public void testDownloadStudyReturnsValidStudyWithCorrectName() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		assertEquals 'Test Study', convertedStudy.getName()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithForms() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		assertEquals 1, convertedStudy.getForms().size()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithFormVersion() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		assertEquals 1, convertedStudy.getForm(0).getVersions().size()
	}
	
	@Test public void testDownloadStudyReturnsValidWithLatestVersionsAsDefault() {
		
		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		
		FormDef form = convertedStudy.getForm("Test Form")
		FormDefVersion version = form.getVersion("Test Version")
		
		assertTrue version.getIsDefault()
	}
}
