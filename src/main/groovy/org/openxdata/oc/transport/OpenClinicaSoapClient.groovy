package org.openxdata.oc.transport

import org.openxdata.oc.model.OpenClinicaUser;
import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.FormData


/**
 * Defines methods that represent endpoints OpenClinica Web services. Note that this is not a web service in itself but
 * rather tries to map the methods to resemble the endpoints defined in the web services.
 *
 */
public interface OpenClinicaSoapClient {

	def findAllCRFS(def studyOID)
		
	def getOpenxdataForm(def studyIdentifier)

	HashMap<String, String> importData(List<FormData> instanceData)
	
	List<StudySubject> findStudySubjectEventsByStudyOID(def studyOID)
	
	OpenClinicaUser getUserDetails(def username)
}
