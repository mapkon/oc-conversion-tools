package org.openxdata.oc.transport

import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.factory.ConnectionFactory;

/**
 * Defines methods that represent endpoints OpenClinica Web services. Note that this is not a web service in itself but
 * rather tries to map the methods to resemble the endpoints defined in the web services.
 *
 */
public interface OpenClinicaSoapClient {

	/**
	 * Fetches all available studies from an openclinica web service. 
	 * The returned list depends on the authenticated user and the sutdies mapped to them.
	 */
	List<ConvertedOpenclinicaStudy> listAll()

	/**
	* Gets Metadata for the specified Study Identifier. The metadata is encapsulated in an ODM file.
	*
	* @param studyOID the study identifier for which to retrieve meta data for.
	*
	* @return An XML stream of the ODM file.
	*/
	String getMetadata(String studyOID)
	
	/**
	* Retrieves ODM file for given study identifier and transforms it to an object mimicking openxdata xform definition.
	*
	* @param studyOID Study identifier for which to get metadata for.
	*
	* @return Converted Object mimicking the OpenXData Xform Definition.
	*/
	def getOpenxdataForm(String openclinicaStudyOID)

	/**
	* Retrieves subject keys for a given study identifier.
	*
	* @param studyOID The Study identifier to retrieve subjects for.
	*
	* @return List of subjects assigned to the study.
	*/
	Collection<String> getSubjectKeys(String studyOID)

	/**
	* Imports given data into openclinica.
	*
	* @param instanceData the instanceData collected in openxdata that is to be exported to openclinica.
	*
	*/
	def importData(Collection<String> instanceData)
	
	void setConnectionFactory(ConnectionFactory connectionFactory)
	
}
