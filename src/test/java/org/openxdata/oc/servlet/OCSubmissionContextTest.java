package org.openxdata.oc.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openxdata.oc.data.TestData;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.xform.StudyImporter;

public class OCSubmissionContextTest {

	@Mock
	private OpenClinicaService ocService;
	@Mock
	private StudyManagerService studyManagerService;
	private static List<StudySubject> studySubjectsObjects = TestData.getStudySubjectsObjects();
	private static StudyDef oXDStudy;
	private OCSubmissionContext instance;
	private Properties props;

	@BeforeClass
	public static void initTestData() {
		studySubjectsObjects = TestData.getStudySubjectsObjects();
		StudyImporter importer = new StudyImporter(TestData.getConvertedXform());
		oXDStudy = (StudyDef) importer.extractStudy();
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		props = new Properties();
		props.setProperty("studyOID", "Test Study");
		instance = new OCSubmissionContext(null, null, null, null, null, ocService, props);
		instance.setStudyManagerService(studyManagerService);

	}

	@Test
	public void testAvailableWorkitemsReturnsStudyEventsAsWorkitems() {
		when(studyManagerService.getStudyByName(getStudyName())).thenReturn(new ArrayList<StudyDef>() {

			private static final long serialVersionUID = 1L;
			{
				add(oXDStudy);
			}
		});

		when(ocService.getStudySubjectEvents(Mockito.anyString())).thenReturn(studySubjectsObjects);

		List<?> result = instance.availableWorkitems();

		assertThat("A list of workitems were expected but none were returned", result.isEmpty(), is(false));

	}

	@Test
	public void testAvailableWorkitemsReturnsEmptyListOfWorkitemsIfEventsDontMatchStudy() {
		final StudyDef dummyStudy = new StudyDef(0, getStudyName());
		dummyStudy.addForm(new FormDef(0, "FunnyForm", dummyStudy));
		when(studyManagerService.getStudyByName(getStudyName())).thenReturn(new ArrayList<StudyDef>() {

			private static final long serialVersionUID = 1L;
			{
				add(dummyStudy);
			}
		});

		when(ocService.getStudySubjectEvents(Mockito.anyString())).thenReturn(studySubjectsObjects);

		List<?> result = instance.availableWorkitems();

		assertThat("Workitems list Should be empty", result.isEmpty(), is(true));
		assertThat("Orphaned Events Should not be Empty", instance.getOrphanedEvents().isEmpty(), is(false));
	}

	@Test
	public void testAvailableWorkitemReturnEmptListOfWorkitemsIfNoOcStudyAvailable() {
		when(ocService.getStudySubjectEvents(getStudyName())).thenReturn(studySubjectsObjects);
		when(studyManagerService.getStudyByName(getStudyName())).thenReturn(Collections.<StudyDef> emptyList());
		List<Object[]> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyByName(getStudyName()))
				.thenThrow(new RuntimeException("Deliberate Exception"));
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
	public void testConverstionStudySubjectToWorkItem() {
		StudySubject studySubject = null;
		for (StudySubject tmpStudySubj : studySubjectsObjects) {
			if (tmpStudySubj.getSubjectOID().toString().equals("TEST_SUBJECT_EVENT"))
				studySubject = tmpStudySubj;
		}
		List<Object[]> workitems = instance.studySubjectToWorkItems(studySubject, oXDStudy);

		assertThat("The number of workitems are supposed to be 2", workitems.size(), is(2));

		for (Object[] objects : workitems) {
			if (objects[0].equals("TEST_SUBJECT_EVENT-TEST_EVENT1_OID"))
				assertThat("The number of form References Should be 2 ", ((List<?>) objects[2]).size(), is(2));
			else if (objects[0].equals("TEST_SUBJECT_EVENT-TEST_EVENT2_OID"))
				assertThat("The number of form References Should be 3 ", ((List<?>) objects[2]).size(), is(3));
			else
				Assert.fail("None of the expected Workitems were found");
		}

	}

	private String getStudyName() {
		return props.getProperty("studyOID");
	}

}
