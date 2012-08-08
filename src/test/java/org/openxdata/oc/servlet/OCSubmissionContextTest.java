package org.openxdata.oc.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openxdata.oc.data.TestData;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.xform.StudyImporter;

public class OCSubmissionContextTest {

	private Properties props;
	private StudyDef convertedStudy;
	private OCSubmissionContext instance;

	@Mock
	private OpenClinicaService ocService;

	@Mock
	private StudyManagerService studyManagerService;
	private static List<StudySubject> studySubjects;

	@Before
	public void setUp() {

		studySubjects = TestData.getStudySubjectsAsList();

		StudyImporter importer = new StudyImporter(TestData.getConvertedXform());

		// Extract study
		convertedStudy = (StudyDef) importer.extractStudy();

		MockitoAnnotations.initMocks(this);

		props = new Properties();
		props.setProperty("studyOID", "Test Study");

		instance = new OCSubmissionContext(null, null, null, null, null, ocService, props);

		instance.setStudyManagerService(studyManagerService);

		Map<Integer, String> mappedStudyNames = new HashMap<Integer, String>();
		mappedStudyNames.put(convertedStudy.getId(), convertedStudy.getName());
		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(mappedStudyNames);

	}

	@Test
	public void testAvailableWorkitemsReturnsStudyEventsAsWorkitems() {

		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(convertedStudy);

		when(ocService.getStudySubjectEvents()).thenReturn(studySubjects);

		List<?> result = instance.availableWorkitems();

		assertThat("A list of workitems were expected but none were returned", result.isEmpty(), is(false));

	}

	@Test
	public void testAvailableWorkitemsReturnsEmptyListOfWorkitemsIfEventsDontMatchStudy() {
		final StudyDef dummyStudy = new StudyDef(0, getStudyName());
		dummyStudy.addForm(new FormDef(0, "FunnyForm", dummyStudy));
		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(dummyStudy);

		when(ocService.getStudySubjectEvents()).thenReturn(studySubjects);

		List<?> result = instance.availableWorkitems();

		assertThat("Workitems list Should be empty", result.isEmpty(), is(true));
		assertThat("Orphaned Events Should not be Empty", instance.getOrphanedEvents().isEmpty(), is(false));
	}

	@Test
	public void testAvailableWorkitemReturnEmptListOfWorkitemsIfNoOcStudyAvailable() {
		when(ocService.getStudySubjectEvents()).thenReturn(studySubjects);
		when(studyManagerService.getStudyByName(getStudyName())).thenReturn(Collections.<StudyDef> emptyList());
		List<Object[]> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyByKey(getStudyName())).thenThrow(new RuntimeException("Deliberate Exception"));
		List<Object[]> availableWorkitems1 = instance.availableWorkitems();

		assertThat("Workitems are expected to be empty", availableWorkitems1.isEmpty(), is(true));

	}

	@Test
	public void testAvailableWorkitemReturnEmptListIfOCStudyPropertyIsNullOrEmpty() {
		props.setProperty("studyOID", "");
		List<Object[]> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		props = new Properties();
		instance.setProps(props);
		List<Object[]> availableWorkitems2 = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems2.isEmpty());

	}

	@Test
	public void testStudySubjectToWorkItemConversion() {
		StudySubject studySubject = null;
		for (StudySubject tmpStudySubj : studySubjects) {
			if (tmpStudySubj.getSubjectOID().toString().equals("TEST_SUBJECT_EVENT"))
				studySubject = tmpStudySubj;
		}

		List<Object[]> workitems = instance.studySubjectToWorkItems(studySubject, convertedStudy);

		assertThat("The number of workitems are supposed to be 2", workitems.size(), is(2));

		for (Object[] objects : workitems) {
			if (objects[0].equals("TEST_SUBJECT_EVENT-TEST_EVENT1_OID"))
				assertThat("The number of form References Should be 2 ", ((List<?>) objects[2]).size(), is(2));
			else if (objects[0].equals("TEST_SUBJECT_EVENT-TEST_EVENT2_OID"))
				assertThat("The number of form References Should be 3 ", ((List<?>) objects[2]).size(), is(3));
			else
				fail("None of the expected Workitems were found");
		}

	}

	@Test
	public void testAvailableWorkitemReturnsEmptListIfOCStudyAccessIsProhibited() {

		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(convertedStudy);

		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(null);
		List<Object[]> availableWorkitems = instance.availableWorkitems();
		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(Collections.<Integer, String> emptyMap());
		availableWorkitems = instance.availableWorkitems();
		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Integer.SIZE, "Blah Blah");
		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(map);
		availableWorkitems = instance.availableWorkitems();
		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());
	}

	private String getStudyName() {
		return props.getProperty("studyOID");
	}

}
