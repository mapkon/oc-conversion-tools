package org.openxdata.oc.authentication

import groovy.util.logging.Log

import org.openxdata.oc.util.PropertiesUtil
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User

@Log
class AuthenticationProvider {

	def username
	def password

	def userService
	def roleService
	def studyService
	def openclinicaService
	def authenticationService

	public User authenticate(String username, String password) {

		log.info("Attempting to authenticate user: ${username}")

		this.username = username
		this.password = password

		def user = authenticateViaOpenXData()

		if(user){
			return user
		} else {

			def openclinicaUser = authenticateViaOpenClinica()
		}
	}

	private def authenticateViaOpenXData() {

		log.info("Attempting to authenticate user: ${username} using openXdata authentication mechanism")

		def user = userService.findUserByUsername(username)

		if(user != null) {
			
			log.info("User: ${username} exists in db. Validating credentials...")
			return authenticationService.authenticate(username, password)
			
		} else {
		
			log.info("User: ${username} doesnot exist in openXdata...")
			return null
		}
	}

	private def authenticateViaOpenClinica() {

		log.info("Attempting to fetch user: ${username} from openclinica...")
		
		def user
		def openclinicaUser = openclinicaService.getUserDetails(username)

		if(openclinicaUser) {

			user = createOXDUserFromOpenClinicaUserDetails(openclinicaUser)

			return user
		}
	}

	private def createOXDUserFromOpenClinicaUserDetails(def openclinicaUser) {

		log.info("Creating openXdata user with name: ${username} from openclinica user")
		
		def user = new User()

		user.setClearTextPassword(password)
		user.setName(openclinicaUser.username)

		// Keep the openclinica hashed password in the secret answer field for later use.
		user.setSecretAnswer(openclinicaUser.hashedPassword)

		log.info("Adding mobile role to user: ${username} to enable them use mobile")
		def mobileRole = roleService.getRolesByName("Role_Mobile_User")
		user.addRole(mobileRole)

		log.info("Mapping study with key: ${getStudyKey()} to user: ${username}")
		def studyKey = getStudyKey()
		def study = studyService.getStudyByKey(studyKey)
		def mappedStudy = new HashSet<StudyDef>()

		user.setMappedStudies(mappedStudy)

		return userService.saveUser(user)
	}

	private def getStudyKey() {

		def props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')

		return props.get("studyOID", "")
	}
}
