package org.openxdata.oc.authentication

import groovy.util.logging.Log

import org.openxdata.oc.util.PropertiesUtil
import org.openxdata.server.admin.model.StudyDefHeader
import org.openxdata.server.admin.model.User
import org.openxdata.server.admin.model.exception.UserNotFoundException

@Log
class AuthenticationProvider {

	private def username
	private def password

	def userService
	def roleService
	def studyService
	def openclinicaService
	def authenticationService

	public User authenticate(String username, String password) {

		log.info("Attempting to authenticate user: ${username}")

		this.username = username
		this.password = password

		def user = null
		
		user = authenticateViaOpenXData()

		if(user){
			return user
		} else {

			user = authenticateViaOpenClinica()
		}
		
		return user
	}

	private def authenticateViaOpenXData() {

		log.info("Attempting to authenticate user: ${username} using openXdata authentication mechanism")

		def existingUser = getOXDUser() 

		if(existingUser) {
			
			log.info("User: ${username} exists in openXdata db. Validating credentials...")
			return authenticationService.authenticate(username, password)
			
		} else {
		
			log.info("User: ${username} doesnot exist in openXdata...")
			return null
		}
	}

	private def getOXDUser() {
		
		try {
			
			return userService.findUserByUsername(username)
			
		} catch (UserNotFoundException ex) {
			return null
		}
	}
	
	private def authenticateViaOpenClinica() {

		log.info("Attempting to fetch user: ${username} from openclinica...")
		
		def user
		def openclinicaUser = openclinicaService.getUserDetails(username)

		if(openclinicaUser) {

			user = createOXDUserFromOpenClinicaUserDetails(openclinicaUser)
		}
		
		return user
	}

	private def authenticateAgainstOpenXData() {

		def props = getProperties()

		log.info("Authenticating in openXdata with username ${props.get("oxdUserName")} to access openXdata services.")

		def username = props.get("oxdUserName")
		def password = props.get("oxdPassword")

		return authenticationService.authenticate(username, password)

	}

	private def createOXDUserFromOpenClinicaUserDetails(def openclinicaUser) {

		log.info("Creating openXdata user with name: ${username} from openclinica user")

		// Authenticate in order to access openXdata services
		User creator = authenticateAgainstOpenXData()

		def user = new User()

		user.setCreator(creator)
		user.setDateCreated(new Date())
		user.setClearTextPassword(password)
		user.setName(openclinicaUser.username)
		user.setFirstName(openclinicaUser.username)

		// Keep the openclinica hashed password in the secret answer field for later use.
		user.setSecretAnswer(openclinicaUser.hashedPassword)

		log.info("Adding mobile role to user: ${username} to enable them use mobile")

		def mobileRole = roleService.getRolesByName("Role_Mobile_User")
		user.addRole(mobileRole.get(0))

		// Create user study map
		createUserStudyMap(user)

		return user
	}

	private def createUserStudyMap(def user) {
		
		log.info("Mapping study with key: ${getStudyKey()} to user: ${username}");
		
		def studyKey = getStudyKey();
		
		def study = studyService.getStudyByKey(studyKey);

		def studyDefHeader = new StudyDefHeader(study.getId(), study.getName())
		def mappedStudies = new ArrayList<StudyDefHeader>();
		
		mappedStudies.add(studyDefHeader)
		
		user = userService.saveUser(user);
		
		studyService.saveMappedUserStudyNames(user.getId(), mappedStudies, new ArrayList<StudyDefHeader>())
	}
	
	private def getProperties () {

		def props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')
	}

	private def getStudyKey() {

		def props = getProperties().get("studyOID", "")
	}
}
