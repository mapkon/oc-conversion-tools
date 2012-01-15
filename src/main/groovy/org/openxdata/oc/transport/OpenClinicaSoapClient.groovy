package org.openxdata.oc.transport

import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.factory.ConnectionFactory;

/**
 * Defines methods that represent endpoints OpenClinica Web services. Note that this is not a web service in itself but
 * rather tries to map the methods to resemble the endpoints defined in the web services.
 *
 */
public interface OpenClinicaSoapClient {

	List<ConvertedOpenclinicaStudy> listAll()

	def findAllCRFS(def studyOID)
	
	def getMetadata(def studyIdentifier)
	
	def getOpenxdataForm(String studyIdentifier)

	Collection<String> getSubjectKeys(String studyIdentifier)

	def importData(Collection<String> instanceData)
		
}
