package org.openxdata.oc.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openxdata.oc.Fixtures;
import org.openxdata.oc.data.TestData;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.StudyManagerService;

public class OCSubmissionContextTest {

	@Mock
	private OpenClinicaService ocService;
	@Mock
	private StudyManagerService studyManagerService;
	private static List<StudySubject> studySubjectsObjects = TestData.getStudySubjectsObjects();
	private static StudyDef oXDStudy = Fixtures.getOXDStudy();
	private OCSubmissionContext instance;

	public OCSubmissionContextTest() {
	}

	@BeforeClass
	public static void initTestData() {
		studySubjectsObjects = TestData.getStudySubjectsObjects();
		oXDStudy = Fixtures.getOXDStudy();
	}

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		instance = new OCSubmissionContext(null, null, (byte) 1, null, null, null, studyManagerService, ocService);

	}

	@Test
	public void testAvailableWorkitemsReturnsStudyEventsAsWorkitems() {
		when(studyManagerService.getStudyByName("Default Study")).thenReturn(new ArrayList<StudyDef>() {
			
			private static final long serialVersionUID = 1L; 
			{
				add(oXDStudy);
			}
		});

		when(ocService.getStudySubjectEvents("S_DEFAULTS1")).thenReturn(studySubjectsObjects);

		List<?> result = instance.availableWorkitems();

		assertThat("A list of workitems were expected but none were returned", result.isEmpty(), is(false));

	}

	@Test
	public void testAvailableWorkitemReturnEmptListOfWorkitemsIfNoOcStudyAvailable() {
		
		when(ocService.getStudySubjectEvents("S_DEFAULTS1")).thenReturn(studySubjectsObjects);
		when(studyManagerService.getStudyByName("Default Study")).thenReturn(Collections.<StudyDef> emptyList());
		List<Object[]> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyByName("Default Study")).thenThrow(
				new RuntimeException("Deliberate Exception"));
		List<Object[]> availableWorkitems1 = instance.availableWorkitems();
		
		assertThat("Workitems are expected to be empty", availableWorkitems1.isEmpty(), is(true));

	}
}
