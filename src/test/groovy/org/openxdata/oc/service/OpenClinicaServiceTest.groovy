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

		Mockito.when(studyService.getStudies()).thenReturn(createStudyList())
		Mockito.when(studyService.getStudyKey(Mockito.anyInt())).thenReturn("key")
		Mockito.when(studyService.getStudyByKey(Mockito.anyString())).thenReturn(createStudy())
		Mockito.when(studyService.hasEditableData(Mockito.any(Editable.class))).thenReturn(Boolean.TRUE)
		Mockito.when(client.findStudySubjectEventsByStudyOID(Mockito.anyString())).thenReturn(createStudySubjectEvents())
		Mockito.when(client.importData(Mockito.anyCollection())).thenReturn("Success")
		
		Mockito.when(dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)).thenReturn(formDataList)

	}
	
	private void createFormDataList() {

		FormData formData = new FormData()
		formData.setId(1)
		FormData formData2 = new FormData()
		formData2.setId(2)

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

	@Test public void testExportDataShouldReturnSuccessMessage() {

		String message = openClinicaService.exportOpenClinicaStudyData()
		
		assertEquals("Success", message)
		
		Mockito.verify(client).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atLeast(2)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
	}

	@Test public void testExportDataShouldReturnMessageOnEmptyInstanceData() {

		Mockito.when(dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)).thenReturn([])
		
		String message = openClinicaService.exportOpenClinicaStudyData()
		
		assertEquals("No data items found to export.", message)
		
		Mockito.verify(client, Mockito.atLeast(0)).importData(Mockito.anyList())
		Mockito.verify(dataExportService, Mockito.atLeastOnce()).getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)
		Mockito.verify(dataExportService, Mockito.atLeast(0)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
	}
	
	@Test public void testExportDataShouldShouldFailOnEmptyInstanceDataWithMessage() {

		Mockito.when(client.importData(Mockito.anyCollection())).thenReturn("Fail")
		
		String message = openClinicaService.exportOpenClinicaStudyData()
		assertEquals("Fail", message)
		
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
		Mockito.verify(dataExportService, Mockito.atLeast(2)).setFormDataExported(Mockito.any(FormData.class), Mockito.anyInt())
		
	}
	
	@Test public void testGetStudySubjectEventsDoesNotReturnNull() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		assertNotNull "Should never return null on valid studyOID", studySubjectEvents
	}
	
	@Test public void testGetStudysubjectEventReturnsCorrectNumberOfStudySubjectEvents() {
		
		def studySubjectEvents = openClinicaService.getStudySubjectEvents("oid")
		
		assertEquals 76, studySubjectEvents.size()
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
}
