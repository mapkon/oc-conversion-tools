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
import org.openxdata.oc.service.OpenClinicaService
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse


@RunWith(MockitoJUnitRunner.class)
class OpenClinicaServletTest extends GroovyTestCase {

	def request
	def response

	@Mock OpenClinicaService service
	@Mock OpenClinicaSoapClient client
	@InjectMocks def servlet = new OpenClinicaServlet()

	@Before void setUp() {

		def map = new HashMap()
		map.put(null, "No data items found to export.")
		
		Mockito.when(client.importData(Mockito.anyList())).thenReturn("Success")
		Mockito.when(client.getOpenxdataForm('oid')).thenReturn(TestData.getCRFWebServiceResponse())
		Mockito.when(service.importOpenClinicaStudy('oid')).thenReturn(createStudy())
		Mockito.when(service.exportOpenClinicaStudyData()).thenReturn(map)
		
		request = new MockHttpServletRequest()
		response = new MockHttpServletResponse()

		request.setParameter('oid', 'oid')
		request.setParameter('action', 'Import')
	}

	private def createStudy() {
		
		def study = new StudyDef()
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
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertNotNull 'Study should not be null after successful Import', study
	}

	@Test public void testDownloadStudySetsSuccessMessageOnSuccessfulImport() {

		servlet.doPost(request, response)

		def message = request.getAttribute('message')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 'OpenClinica Study successfully converted and Imported', message.get("")
	}
	
	@Test public void testDownloadStudyReturnsValidStudyWithCorrectNameInAttribute() {

		servlet.doPost(request, response)

		def name = request.getAttribute('name')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 'Test Study', name
	}

	@Test public void testDownloadStudyReturnsValidStudyWithCorrectKeyInAttribute() {

		servlet.doPost(request, response)

		def key = request.getAttribute('key')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 'Test Key', key
	}
	
	@Test public void testDownloadStudyReturnsValidStudyWithCorrectName() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 'Test Study', convertedStudy.getName()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithForms() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 1, convertedStudy.getForms().size()
	}

	@Test public void testDownloadStudyReturnsValidStudyWithFormVersion() {

		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertEquals 1, convertedStudy.getForm(0).getVersions().size()
	}
	
	@Test public void testDownloadStudyReturnsValidWithLatestVersionsAsDefault() {
		
		servlet.doPost(request, response)

		def convertedStudy = request.getAttribute('study')
		
		FormDef form = convertedStudy.getForm("Test Form")
		FormDefVersion version = form.getVersion("Test Version")
		
		Mockito.verify(service, Mockito.atLeastOnce()).importOpenClinicaStudy("oid")
		
		assertTrue version.getIsDefault()
	}
	
	@Test public void testExportStudyReturnsCorrectMessageOnEmptyData() {
		
		request.setParameter('action', 'Export')

		servlet.doPost(request, response)

		def messages = request.getAttribute('message')

		Mockito.verify(service).exportOpenClinicaStudyData()
		
		assertEquals 'No data items found to export.', messages.get(null)
	}
	
	@Test public void testExportStudyHasCorrectMessageOnSuccessfulExport() {
		
		Mockito.when(service.exportOpenClinicaStudyData()).thenReturn(TestData.createImportMessages())
		
		request.setParameter('action', 'Export')

		servlet.doPost(request, response)

		def message = request.getAttribute('message')
		
		Mockito.verify(service).exportOpenClinicaStudyData()
		
		assertEquals 'Success', message.get("F_MSA2_1")
	}
	
	@Test public void testExportStudyHasCorrectMessageOnErraticExport() {
		
		Mockito.when(service.exportOpenClinicaStudyData()).thenReturn(TestData.createImportMessages())
		
		request.setParameter('action', 'Export')

		servlet.doPost(request, response)

		def message = request.getAttribute('message')
		
		Mockito.verify(service).exportOpenClinicaStudyData()
		
		assertEquals 'Fail: Incorrect FormData OID', message.get("key1")
	}
}
