package org.openxdata.oc.transport.factory

/**
 * Builds a connection for accessing OpenClinca web services end-point.
 *
 */
public class ConnectionURLFactory {

	def host
	
	/**
	 * Constructs valid end-point to OpenClinca web services given a host name.
	 * @param host Host name to connect to.
	 */
	public ConnectionURLFactory(String host){
		this.host = host
	}
	
	/**
	 * Gets the connection for accessing the study end-point in OpenClinca web services.
	 * @return URL for connecting to the study end-point.
	 */
	public HttpURLConnection getStudyConnection(){
		URL url = new URL(host + "/ws/study/v1")
		return url.openConnection()
	}

	/**
	* Gets the connection for accessing the study subject end-point in OpenClinca web services.
	* @return URL for connecting to the study subject end-point.
	*/
	public HttpURLConnection getStudySubjectConnection(){
		URL url = new URL(host + "/ws/studySubject/v1")
		return url.openConnection()
	}
}
