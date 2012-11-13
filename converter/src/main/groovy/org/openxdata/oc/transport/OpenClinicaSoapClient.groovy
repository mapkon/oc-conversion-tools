package org.openxdata.oc.transport

import org.openxdata.oc.model.OpenClinicaUser
import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.User


/**
 * Defines methods that represent endpoints OpenClinica Web services. Note that this is not a web service in itself but
 * rather tries to map the methods to endpoints defined in OpenClinica web services.
 * <p>
 * This should be used in tandem with the application specific services to consume what OpenClinica exposes through the web services.
 *<p>
 * The methods in this interface assume that the user who is attempting to perform web service operations has the correct permissions to do so in OpenClinica.
 */
public interface OpenClinicaSoapClient {

	/**
	 * Extracts all CRFs for a given study from OpenClinica
	 * 
	 * @param studyOID Identifier for study we are extracting CRFs for.
	 * 
	 * @return ODM XML representation of all the CRFs in a given OpenClinica study.
	 */
	def findAllCRFS(def studyOID)
		
	/**
	 * Extracts an OpenXData Forms from ODM XML
	 * 
	 * @param studyIdentifier Identifier for OpenClinica study we are extracting forms from.
	 * 
	 * @return Fully compact OpenXdata Xform XML
	 */
	def getOpenxdataForm(def studyIdentifier)

	/**
	 * Exports the given OpenXdata instance data to OpenClinica.
	 * <p>
	 * The misnomer arises because we are programming against the interfaces that OpenClinica exposes as part of the CDSIC spec and not the operation being performed.
	 * <p>
	 * This method assumes that the instance data has a studyKey corresponding to an existing study in OpenClinica.
	 * 
	 * @param user User who submitted the data
	 * @param instanceData OpenXdata instance data to export to OpenClinica.
	 * @return Map of FormIds and Strings showing status of each FormData export
	 */
	HashMap<String, String> importData(User user, List<FormData> instanceData)
	
	/**
	 * Retrieves all the events for the subjects in a given study that the user has access to.
	 * <p> 
	 * By user we mean the user initiating the web service operation.
	 * 
	 * @param studyOID OID of the study we are retrieving events from.
	 * @return ODM XML of the events.
	 */
	List<StudySubject> findStudySubjectEventsByStudyOID(def studyOID)
	
	/**
	 * Retrieves partial details for a given OpenClinica user.
	 * <p>
	 * This method assumes that the username exists in OpenClinica.
	 * 
	 * @param username User name of the user we retrieving details for.
	 * @return OpenClinica User with the details.
	 */
	OpenClinicaUser getUserDetails(def username)
}
