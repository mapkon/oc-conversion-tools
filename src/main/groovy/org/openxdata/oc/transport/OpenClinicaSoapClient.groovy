package org.openxdata.oc.transport

import org.openxdata.oc.model.OpenclinicaStudy

public interface OpenClinicaSoapClient {

	/**
	 * Returns a List of names of existing studies.
	 * 
	 * @return List of Studies.
	 */
	List<OpenclinicaStudy> listAll()

	String getMetadata(String studyOID)
	
	String getOpenxdataForm(String openclinicaStudyOID, Collection<String> subjectKeys)

	def importData(Collection<String> instanceData)
	
	Collection<String> getSubjectKeys(String studyOID)
}
