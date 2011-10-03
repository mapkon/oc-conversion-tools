package org.openxdata.oc.transport

import org.openxdata.oc.model.OpenclinicaStudy

public interface OpenClinicaSoapClient {

	/**
	 * Fetches all available studies from an openclinica web service.
	 */
	List<OpenclinicaStudy> listAll()

	/**
	* Gets Metadata for the specified Study Identifier. The metadata is encapsulated in an ODM file.
	*
	* @param studyOID the study identifier for which to retrieve meta data for.
	*
	* @return An XML stream of the ODM file.
	*/
	String getMetadata(String studyOID)
	
	/**
	* Retrieves ODM file for given study identifier, transforms it to an openxdata xform and inserts subject keys.
	*
	* @param studyOID Study identifier for which to get metadata for.
	* @param subjectKeys The subject keys to insert into the form.
	*
	* @return A valid openxdata xform.
	*/
	String getOpenxdataForm(String openclinicaStudyOID, Collection<String> subjectKeys)

	/**
	* Retrieves subject keys for a given study identifier.
	*
	* @param studyOID The Study identifier to retrieve subjects for.
	*
	* @return List of subjects assigned to the study.
	*/
	Collection<String> getSubjectKeys(String studyOID)

	/**
	* Exports given data to openclinica.
	*
	* @param instanceData the instanceData collected in openxdata that is to be exported to openclinica.
	*
	*/
	def importData(Collection<String> instanceData)
	
}
