package org.openxdata.oc.transport

import org.openxdata.oc.model.ConvertedOpenclinicaStudy

/**
 * Defines methods that represent endpoints OpenClinica Web services. Note that this is not a web service in itself but
 * rather tries to map the methods to resemble the endpoints defined in the web services.
 *
 */
public interface OpenClinicaSoapClient {

	List<ConvertedOpenclinicaStudy> listAll()

	def findAllCRFS(def studyOID)
		
	def getOpenxdataForm(def studyIdentifier)

	Collection<String> getSubjectKeys(def studyIdentifier)

	def importData(Collection<String> instanceData)
		
}
