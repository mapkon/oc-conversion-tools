package org.openxdata.oc.authentication

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import org.openxdata.oc.data.TestData
import org.openxdata.oc.model.OpenClinicaUser
import org.openxdata.oc.service.OpenClinicaService
import org.openxdata.server.admin.model.Role
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User
import org.openxdata.server.service.AuthenticationService
import org.openxdata.server.service.RoleService
import org.openxdata.server.service.StudyManagerService
import org.openxdata.server.service.UserService

@RunWith(MockitoJUnitRunner.class)
class AuthenticationProviderTest extends GroovyTestCase {

	@Mock RoleService roleService
	@Mock UserService userService
	@Mock StudyManagerService studyService
	@Mock OpenClinicaService openclinicaService
	@Mock AuthenticationService authenticationService

	@InjectMocks def authProvider = new AuthenticationProvider()

	def createUser() {

		def user = new User()

		user.setName("username")
		user.setPassword("some hashed password called LoL")
		user.setSalt("think of a random salt that you cannot crack")

		return user
	}

	def createStudy () {

		def study = new StudyDef()
		study.setStudyKey("S_DEFAULTS1")

		return study
	}

	def createOpenClinicaUser() {

		def user = new OpenClinicaUser(TestData.findUserResponse)
	}

	def createOXDUserFromOCUser() {

		def user = new User()
		def xUser = createOpenClinicaUser()

		user.setName(xUser.username)
		user.setClearTextPassword("password")
		user.setSecretAnswer(xUser.hashedPassword)

		// Add Role
		user.addRole(new Role("Role_Mobile_User"))

		// Add Study
		def mappedStudy = new HashSet<StudyDef>()
		mappedStudy.add(createStudy())

		user.setMappedStudies(mappedStudy)

		return user
	}

	@Before void setUp() {

		def roles = new ArrayList<Role>()
		roles.add(new Role("Role_Mobile_User"))

		Mockito.when(roleService.getRolesByName(Mockito.anyString())).thenReturn(roles)
		Mockito.when(studyService.getStudyByKey(Mockito.anyString())).thenReturn(createStudy())
		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(createUser())
		Mockito.when(userService.saveUser(Mockito.isA(User.class))).thenReturn(createOXDUserFromOCUser())
		Mockito.when(openclinicaService.getUserDetails(Mockito.anyString())).thenReturn(createOpenClinicaUser())
		Mockito.when(authenticationService.authenticate(Mockito.anyString(), Mockito.anyString())).thenReturn(createUser())
	}

	@Test void testThatAuthenticateReturnsUserGivenValidOXDUserCredentials() {

		def user = authProvider.authenticate("username","password")

		assertNotNull "User should not be null on valid credentials", user
	}

	@Test void testThatAuthenticateReturnValidUserWithCorrectUsernameGivenValidUserCredentials() {

		def user = authProvider.authenticate("username","password")

		assertEquals "Username of returned user should equal username param", "username", user.getName()
	}

	@Test void testThatAuthenticateReturnsValidUserWithCorrectPasswordGivenValidOXDUserCredentials() {

		def user = authProvider.authenticate("username", "password")

		assertEquals "Hashed Password of returned user should be equal to some hashed password called LoL", "some hashed password called LoL", user.getPassword()
	}

	@Test void testThatAuthenticateReturnsValidUserWithCorrectSaltGivenValidOXDUserCredentials() {

		def user = authProvider.authenticate("username", "password")

		assertEquals "Salt of returned user should be equal to think of a random salt that you cannot crack", "think of a random salt that you cannot crack", user.getSalt()
	}

	@Test void testThatAuthenticateReturnsUserWhenNoUserIsFoundInOpenXData() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username","password")

		assertNotNull "User should not be null", user
	}

	@Test void testThatAuthenticateReturnsOpenClinicaUserWithCorrectUsername() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		assertEquals "Username should be equal to foo", "foo", user.getName()
	}

	@Test void testThatAuthenticateReturnsOpenClinicaUserWithCorrectClearTextPassword() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		assertEquals "User password should be equal to user provided password", "password", user.getClearTextPassword()
	}

	@Test void testThatAuthenticateReturnsOpenClinicaUserWithCorrectSecretAnswer(){

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		assertEquals "Secret Answer should be equal to hashed password", "hash LoL", user.getSecretAnswer()
	}

	@Test void testThatCreatedUserHasMobileRole() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		user.getRoles().each {

			assertEquals "Created user should access the mobile", "Role_Mobile_User", it.getName()
		}
	}

	@Test void testThatCreatedUserHasAccessToAtleastOneStudy() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		assertEquals "User should be mapped to at least one study", 1, user.getMappedStudies().size()
	}

	@Test void testThatCreatedUserHasAccessToStudyInPropertiesFile() {

		Mockito.when(userService.findUserByUsername(Mockito.anyString())).thenReturn(null)

		def user = authProvider.authenticate("username", "password")

		user.getMappedStudies().each {

			assertEquals "Study key should be equal to study key in openclinica", "S_DEFAULTS1", it.getStudyKey()
		}
	}
}
