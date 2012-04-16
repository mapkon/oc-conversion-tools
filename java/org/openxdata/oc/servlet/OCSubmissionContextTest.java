/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.servlet;

import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import org.openxdata.oc.model.StudySubject;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.openxdata.oc.Fixtures;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.StudyManagerService;
import static org.junit.Assert.*;
import org.openxdata.oc.TestData;
import static org.mockito.Mockito.*;

/**
 *
 * @author kay
 */
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

	/**
	 * Test of availableWorkitems method, of class OCSubmissionContext.
	 */
	@Test
	public void testAvailableWorkitems() {
		System.out.println("availableWorkitems");
		when(studyManagerService.getStudyByName("Default Study")).thenReturn(new ArrayList<StudyDef>() {
			{
				add(oXDStudy);
			}
		});

		when(ocService.getStudySubjectEvents("S_DEFAULTS1")).thenReturn(studySubjectsObjects);

		List result = instance.availableWorkitems();

		System.out.println("Got " + result.size() + " Workitems");

		assertFalse("A list of workitems were expected but none were returned", result.isEmpty());

	}

	@Test
	public void testAvailableWorkitemReturnEmptListOfWorkitemsIfNoOcStudyAvailable() {
		when(ocService.getStudySubjectEvents("S_DEFAULTS1")).thenReturn(studySubjectsObjects);
		when(studyManagerService.getStudyByName("Default Study")).thenReturn(Collections.EMPTY_LIST);
		List<Object[]> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyByName("Default Study")).thenThrow(
				new RuntimeException("Deliberate Exception"));
		List<Object[]> availableWorkitems1 = instance.availableWorkitems();
		assertTrue("Workitems are expected to be empty", availableWorkitems1.isEmpty());

	}

}
