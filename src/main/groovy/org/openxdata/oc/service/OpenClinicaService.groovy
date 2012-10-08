package org.openxdata.oc.service

import org.openxdata.oc.model.OpenClinicaUser
import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

/**
 * Provide an interface to OpenClinica web service endpoint
 * <p>
 * The methods in this interface assume that the user who is retrieving the events has access to perform web services operations in OpenClinica.
 *
 */
public interface OpenClinicaService {
	
	/**
	 * Checks if a given study for the specified studyKey has data collected for it.
	 * 
	 * @param studyKey study key for the study to check again
	 * 
	 * @return True if and only if the study has some data collected for it.
	 */
	Boolean hasStudyData(String studyKey)

	/**
	 * Exports a given FormData to OpenClinica. This method assumes that the user is not null.
	 * <p> 
	 * It should typically be used for direct submissions from the mobile.
	 * 
	 * @param user User who submitted the data.
	 * 
	 * @param formData Submitted FormData instance.
	 * 
	 * @return String indicating the status of the export. Anything apart from "Success" indicates a failed export.
	 */
	String exportFormData(User user, FormData formData)

	/**
	 * Retrieves all the events for all the subjects defined in a given study that the current user has access to.
	 * 
	 * @return List of StudySubjects and their corresponding events.
	 */
	List<StudySubject> getStudySubjectEvents()
	
	/**
	 * Retrieves user name, hashed password and studies user has access to for the specified user name. This method implicitly assumes that the 
	 * username exists in OpenClinica and takes no action on the OpenClinica side if the user does not exist.
	 * <p>
	 * After this call, the user should exist in the OpenXData database if they did not already exist.
	 * 
	 * @param username Username for the User we retrieving details for.
	 * @return OpenClinica User with the details.
	 */
	OpenClinicaUser getUserDetails(def username)

	/**
	 * Exports collected data for a converted OpenClinica study residing in the OpenXdata db.
	 * <p>
	 * This method should be used for batch export of data. Prefer exportFormData(User,FormData) for individual FormData export.
	 * 
	 * @return Map with Form Ids as keys for the success notifications.
	 */
	HashMap<String, String> exportOpenClinicaStudyData()

	/**
	 * Imports an OpenClinica study into OpenXdata. The study is converted from the OpenClinica ODM standard to OpenXdata XForms standard.
	 * 
	 * @param oid OpenClinica Study Identifier for the study to be imported.
	 * 
	 * @return Converted OpenXdata Study definition
	 * 
	 * @throws UnexpectedException During transformation from ODM standard to XForm.
	 */
	StudyDef importOpenClinicaStudy(String oid) throws UnexpectedException
	
	void setFormService(FormService formService)
	
	void setStudyService(StudyManagerService studyService)

	void setDataExportService(DataExportService dataExportService)
	
}
