package org.openxdata.oc.transport.factory

import org.openxdata.oc.util.PropertiesUtil


/**
 * Builds a connection for accessing OpenClinca web services end-point.
 *
 */
public class ConnectionFactory {

	def url
	def host
	
	def ConnectionFactory() {
		
		def props = new PropertiesUtil().loadProperties('openclinica.properties')
		host = props.getAt('host')
	}
	
	/**
	 * Gets the connection for accessing the study end-point in OpenClinca web services.
	 * @return URL for connecting to the study end-point.
	 */
	public HttpURLConnection getStudyConnection(){

		url = new URL("${host}/ws/study/v1")
		return url.openConnection()
	}

	/**
	* Gets the connection for accessing the study subject end-point in OpenClinca web services.
	* @return URL for connecting to the study subject end-point.
	*/
	public HttpURLConnection getStudySubjectConnection(){
		
		url = new URL("${host}/ws/studySubject/v1")
		return url.openConnection()
	}
}
