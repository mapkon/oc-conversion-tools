package org.openxdata.oc.servlet;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.openxdata.oc.authentication.AuthenticationProvider;
import org.openxdata.oc.data.TestData;
import org.openxdata.oc.model.OpenClinicaUser;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.proto.model.OxdWorkitem;
import org.openxdata.server.admin.model.FormData;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.Role;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.exception.UserNotFoundException;
import org.openxdata.server.service.AuthenticationService;
import org.openxdata.server.service.FormDownloadService;
import org.openxdata.server.service.RoleService;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.service.UserService;
import org.openxdata.xform.StudyImporter;

public class OCSubmissionContextTest {

	private Properties props;
	private StudyDef convertedStudy;

	@Mock private RoleService roleService;

	@Mock
	private OpenClinicaService openclinicaService;

	@Mock
	private StudyManagerService studyManagerService;

	@Mock
	private UserService userService;

	@Mock
	private FormDownloadService formService;
	private static List<StudySubject> studySubjects;

	@Mock private AuthenticationProvider authProvider;
	@Mock private AuthenticationService authenticationService;

	private OCSubmissionContext instance;

	@Before
	public void setUp() throws UserNotFoundException {

		studySubjects = TestData.getStudySubjectsAsList();

		StudyImporter importer = new StudyImporter(TestData.getConvertedXform());

		// Extract study
		convertedStudy = (StudyDef) importer.extractStudy();

		MockitoAnnotations.initMocks(this);

		props = new Properties();
		props.setProperty("studyOID", "Test Study");

		instance = new OCSubmissionContext();
		instance.init();

		instance.setProps(props);
		instance.setStudyManagerService(studyManagerService);
		instance.setFormService(formService);
		instance.setUserService(userService);
		instance.setAuthenticationProvider(authProvider);
		instance.setOpenClinicaService(openclinicaService);

		authProvider.setUserService(userService);
		authProvider.setRoleService(roleService);
		authProvider.setOpenclinicaService(openclinicaService);
		authProvider.setStudyService(studyManagerService);

		Map<Integer, String> mappedStudyNames = new HashMap<Integer, String>();
		mappedStudyNames.put(convertedStudy.getId(), convertedStudy.getName());

		when(userService.findUserByUsername("foo")).thenReturn(createUser());
		when(authProvider.authenticate("foo", "password")).thenReturn(createUser());
		when(userService.saveUser(Mockito.any(User.class))).thenReturn(createUser());
		when(roleService.getRolesByName(Mockito.anyString())).thenReturn(createRoles());
		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(mappedStudyNames);
		when(openclinicaService.getUserDetails(Mockito.anyString())).thenReturn(createOpenclinicaUser());

	}

	private List<Role> createRoles() {

		List<Role> roles = new ArrayList<Role>();

		roles.add(new Role("Role_Mobile_User"));

		return roles;
	}

	private OpenClinicaUser createOpenclinicaUser() {

		return new OpenClinicaUser(TestData.getFindUserResponse());
	}

	@Test
	public void testAvailableWorkitemsReturnsStudyEventsAsWorkitems() {

		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(convertedStudy);

		when(openclinicaService.getStudySubjectEvents()).thenReturn(studySubjects);

		List<?> result = instance.availableWorkitems();

		assertThat("A list of workitems were expected but none were returned", result.isEmpty(), is(false));

	}

	@Test
	public void testAvailableWorkitemsReturnsEmptyListOfWorkitemsIfEventsDontMatchStudy() {
		final StudyDef dummyStudy = new StudyDef(0, getStudyName());
		dummyStudy.addForm(new FormDef(0, "FunnyForm", dummyStudy));
		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(dummyStudy);

		when(openclinicaService.getStudySubjectEvents()).thenReturn(studySubjects);

		List<?> result = instance.availableWorkitems();

		assertThat("Workitems list Should be empty", result.isEmpty(), is(true));
		assertThat("Orphaned Events Should not be Empty", instance.getOrphanedEvents().isEmpty(), is(false));
	}

	@Test
	public void testAvailableWorkitemReturnEmptListOfWorkitemsIfNoOcStudyAvailable() {
		when(openclinicaService.getStudySubjectEvents()).thenReturn(studySubjects);
		when(studyManagerService.getStudyByName(getStudyName())).thenReturn(Collections.<StudyDef> emptyList());
		List<OxdWorkitem> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		when(studyManagerService.getStudyByKey(getStudyName())).thenThrow(new RuntimeException("Deliberate Exception"));
		List<OxdWorkitem> availableWorkitems1 = instance.availableWorkitems();

		assertThat("Workitems are expected to be empty", availableWorkitems1.isEmpty(), is(true));

	}

	@Test
	public void testAvailableWorkitemReturnEmptListIfOCStudyPropertyIsNullOrEmpty() {
		props.setProperty("studyOID", "");
		List<OxdWorkitem> availableWorkitems = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems.isEmpty());

		props = new Properties();
		instance.setProps(props);
		List<OxdWorkitem> availableWorkitems2 = instance.availableWorkitems();

		assertTrue("Workitems are expected to be empty", availableWorkitems2.isEmpty());

	}

	@Test
	public void testStudySubjectToWorkItemConversion() {
		StudySubject studySubject = null;
		for (StudySubject tmpStudySubj : studySubjects) {
			if (tmpStudySubj.getSubjectOID().toString().equals("TEST_SUBJECT_EVENT"))
				studySubject = tmpStudySubj;
		}

		List<OxdWorkitem> workitems = instance.studySubjectToWorkItems(studySubject, convertedStudy);

		assertThat("The number of workitems are supposed to be 2", workitems.size(), is(2));

		for (OxdWorkitem objects : workitems) {
			if (objects.getWorkitemName().equals("TEST_SUBJECT_EVENT-TEST_EVENT1_OID"))
				assertThat("The number of form References Should be 2 ", objects.getWorkitemForms().size(), is(2));
			else if (objects.getWorkitemName().equals("TEST_SUBJECT_EVENT-TEST_EVENT2_OID"))
				assertThat("The number of form References Should be 3 ", objects.getWorkitemForms().size(), is(3));
			else
				fail("None of the expected Workitems were found");
		}

	}

	@Test
	public void testAvailableWorkitemReturnsEmptListIfOCStudyAccessIsProhibited() {

		when(studyManagerService.getStudyByKey(getStudyName())).thenReturn(convertedStudy);

		when(studyManagerService.getStudyNamesForCurrentUser()).thenReturn(null);
		List<OxdWorkitem> availableWorkitems = instance.availableWorkitems();
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

	@Test
	public void testSetUploadResultExportsData() {
		FormData formData = new FormData();
		formData.setId(1);
		when(formService.saveFormData(Mockito.anyString(), Mockito.any(User.class), Mockito.any(Date.class)))
				.thenReturn(formData);
		when(openclinicaService.exportFormData(formData)).thenReturn("Success");

		instance.setUploadResult("<ANY_XML/>");

		verify(openclinicaService, atLeastOnce()).exportFormData(formData);

	}

	@Test
	public void testSetUploadResultReturnsAnIntStringIfExportIsSuccessful() {
		FormData formData = new FormData();
		formData.setId(1);
		when(formService.saveFormData(Mockito.anyString(), Mockito.any(User.class), Mockito.any(Date.class)))
				.thenReturn(formData);
		when(openclinicaService.exportFormData(formData)).thenReturn("Success");

		String result = instance.setUploadResult("<ANY_XML/>");
		assertEquals("1", result);

	}

	@Test
	public void testSetUploadResultThrowsExceptionWhenExportFails() {
		FormData formData = new FormData();
		formData.setId(1);
		when(formService.saveFormData(Mockito.anyString(), Mockito.any(User.class), Mockito.any(Date.class)))
				.thenReturn(formData);
		when(openclinicaService.exportFormData(formData)).thenReturn("This is an error message");

		try {
			instance.setUploadResult("<ANY_XML/>");
			fail("An Exception Was Expected");
		} catch (Exception e) {
		}

	}

	@Test public void testAuthenticatePassesWhenUserExistsInOpenXData() {

		boolean authenticated = instance.authenticate("foo", "password");

		assertTrue ("Should authenticate valid OXD user", authenticated);
	}

	@Test public void testAuthenticatePassesWhenUserDoesExistsInOpenXDataButIsFetchedFromOC() throws UserNotFoundException {

		when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null);
		when(studyManagerService.getStudyByKey(Mockito.anyString())).thenReturn(convertedStudy);

		boolean authenticated = instance.authenticate("foo", "password");

		assertTrue ("Should authenticate valid OXD user", authenticated);
	}

	@Test public void testAuthenticateFailsWhenOXDPasswordIsWrong() throws UserNotFoundException {

		when(authProvider.authenticate("foo", "password")).thenReturn(null);

		boolean authenticated = instance.authenticate("foo", "password");

		assertFalse ("Should authenticate valid OXD user", authenticated);
	}

	private User createUser() {

		User user = new User("foo");
		user.setPassword("password");

		return user;
	}
}
