package org.openxdata.oc.transport

import org.openxdata.oc.model.OpenclinicaStudy

public interface OpenClinicaSoapClient {

	/**
	 * Returns a List of names of existing studies.
	 * 
	 * @return List of Studies.
	 */
	List<OpenclinicaStudy> listAll()

	def getMetadata(def studyOID)

	Node sendRequest(String envelope)

	def importData(Collection<String> instanceData)
}
