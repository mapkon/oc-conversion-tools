package org.openxdata.oc.service

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertTrue

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.openxdata.oc.data.TestData
import org.openxdata.oc.model.StudySubject
import org.openxdata.oc.service.impl.OpenClinicaServiceImpl
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.server.admin.model.Editable
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.export.ExportConstants
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService


@RunWith(MockitoJUnitRunner.class)
public class OpenClinicaServiceTest extends GroovyTestCase {

	def formDataList = []
	
	@Mock private FormService formService
	@Mock private OpenClinicaSoapClient client
	@Mock private StudyManagerService studyService
	@Mock private DataExportService dataExportService
	
	@InjectMocks private def openClinicaService = new OpenClinicaServiceImpl()

	@Before public void setUp() throws Exception {

		createFormDataList()

		Mockito.when(client.findStudySubjectEventsByStudyOID(Mockito.anyString())).thenReturn(createStudySubjectEvents())
		Mockito.when(client.importData(Mockito.anyCollection())).thenReturn(TestData.createImportMessages())
		Mockito.when(client.getOpenxdataForm(Mockito.anyString())).thenReturn(TestData.getConvertedXform())
		
		Mockito.when(studyService.getStudies()).thenReturn(createStudyList())
		Mockito.when(studyService.getStudyKey(Mockito.anyInt())).thenReturn("key")
		Mockito.when(studyService.getStudyByKey(Mockito.anyString())).thenReturn(createStudy())
		Mockito.when(studyService.hasEditableData(Mockito.any(Editable.class))).thenReturn(Boolean.TRUE)
		
		Mockito.when(dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)).thenReturn(formDataList)

	}
	
	private void createFormDataList() {

		FormData formData = new FormData()
		formData.setId(1)
		formData.setData("""<ODM formKey="F_MSA2_1"></ODM>""")
		
		FormData formData2 = new FormData()
		formData2.setId(2)
		formData2.setData("""<ODM formKey="F_MSA2_2"></ODM>""")

		formDataList.add(formData)
		formDataList.add(formData2)
	}

	private StudyDef createStudy() {

		StudyDef study = new StudyDef()

		FormDef form = new FormDef()

		study.addForm(form)

		return study
	}

	private List<StudyDef> createStudyList() {
		
		def studies = []
		
		StudyDef study = new StudyDef()
		study.setName("study")
		study.setStudyKey("oid")

		studies.add(study)
		
		return studies
	}

	private List<StudySubject> createStudySubjectEvents(){
		
		def subjects = []
		
		def studySubjectEventNode = TestData.getStudySubjects()
		
		studySubjectEventNode.studySubject.each {
			def subject = new StudySubject(it)
			subjects.add(subject)
		}
		
		return subjects
	}
	
	@Test public void testHasStudyData(){

		String studyKey = studyService.getStudyKey(1)
		assertTrue(openClinicaService.hasStudyData(studyKey))

		Mockito.when(studyService.hasEditableData(Mockito.any(Editable.class))).thenReturn(Boolean.FALSE)
		
		String studyKey2 = studyService.getStudyKey(2)
		
		assertFalse(openClinicaService.hasStudyData(studyKey2))
		
		Mockito.verify(studyService, Mockito.atLeastOnce()).hasEditableData(Mockito.any(Editable.class))
	}

	@Test public void testExportDataShouldReturnsCorrectNumberOfMessages() {

		def messages = openClinicaService.exportOpenClinicaStudyData()
		
		assertEquals 4, messages.size()
		
		Mockito.verify(client).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atLeast(2)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
	}

	@Test public void testExportDataShouldReturnMessageOnEmptyInstanceData() {

		Mockito.when(dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)).thenReturn([])
		
		def messages = openClinicaService.exportOpenClinicaStudyData()
		
		assertEquals("No data items found to export.", messages.get(""))
		
		Mockito.verify(client, Mockito.atLeast(0)).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atLeast(0)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
	}
	
	@Test public void testExportDataShouldReturnFailureMessageOnErraticExport() {

		def messages = openClinicaService.exportOpenClinicaStudyData()
		assertEquals("Fail: Incorrect FormData OID", messages.get("key1"))
		
		Mockito.verify(client).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atLeast(0)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
	}
	
	@Test public void testExportDataShouldSetFormDataWithOpenclinicaExportBitFlag() {
		
		openClinicaService.exportOpenClinicaStudyData()
		
		
		formDataList.each {
			
			assertTrue "Should be marked as Exported with openclinica export bit flag", it.isExported(ExportConstants.EXPORT_BIT_OPENCLINICA)
		}
		
		Mockito.verify(client).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atMost(2)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
		
	}
	
	@Test public void testGetStudySubjectEventsDoesNotReturnNull() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		assertNotNull "Should never return null on valid studyOID", studySubjectEvents
	}
	
	@Test public void testGetStudysubjectEventReturnsCorrectNumberOfStudySubjectEvents() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		assertEquals 77, studySubjectEvents.size()
	}
	
	@Test public void testGetStudysubjectEventReturnsStudySubjectEventsWithEvents() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		studySubjectEvents.each {
			
			assertTrue "StudySubjectEvent should have at least one event definition", it.getEvents().size() > 0
		}
	}
	
	@Test public void testGetStudysubjectEventReturnsStudySubjectEventsWithEventsHavingFormOIDs() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		studySubjectEvents.each {
			
			it.getEvents().each { event ->
				
				assertTrue "StudySubject event definition should have at least one formOID", event.getFormOIDs().size() > 0
			}
		}
	}
	
	@Test public void testGetStudysubjectEventReturnsStudySubjectEventsWithEventsHavingStartDate() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		studySubjectEvents.each {
			
			it.getEvents().each { event ->
				
				assertNotNull "StudySubject event definition should have at least one formOID", event.startDate
			}
		}
	}
	
	@Test public void testGetStudysubjectEventReturnsStudySubjectEventsWithEventsHavingEndDate() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		studySubjectEvents.each {
			
			it.getEvents().each { event ->
				
				assertNotNull "StudySubject event definition should have at least one formOID", event.endDate
			}
		}
	}
	
	@Test public void testImportOpenClinicaStudyDoesNotReturnNull() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertNotNull "Should never return null on valid studyOID", study
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithCorrectName() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "Test Study", study.getName()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithCorrectStudyKey() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "Test-OID", study.getStudyKey()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithCorrectNumberOfForms() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals 4, study.getForms().size()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithFormHavingCorrectName() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "MSA1: Mother Screening Assessment 1 - 3", study.getForms()[0].getName()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithFormHavingCorrectName1() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "MSA1: Mother Screening Assessment 1 - 2", study.getForms()[1].getName()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithFormHavingCorrectName2() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "MSA2: Mother Screening Assessment 2 - 2", study.getForms()[2].getName()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithFormHavingCorrectName3() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		assertEquals "MSA2: Mother Screening Assessment 2 - 1", study.getForms()[3].getName()
	}
	
	@Test public void testImportOpenClinicaStudyReturnsStudyWithFormHavingFormVersions() {
		
		def study = openClinicaService.importOpenClinicaStudy("oid")
		
		study.getForms().each {
			
			assertTrue "Forms must have versions", it.getVersions().size() > 0
		}
	}
}
