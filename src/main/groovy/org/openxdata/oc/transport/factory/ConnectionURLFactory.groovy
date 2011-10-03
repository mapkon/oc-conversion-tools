package org.openxdata.oc.transport.factory

/**
 * Builds a connection for accessing openclinca web services.
 *
 */
public class ConnectionURLFactory {

	def host
	
	/**
	 * Constructs valid endpoints to openclinica web services given a host name.
	 * @param host Host name to connect to.
	 */
	public ConnectionURLFactory(String host){
		this.host = host
	}
	
	/**
	 * Gets the connection for accessing the study endpoint in openclinica web services.
	 * @return URL for connecting to the study endpoint.
	 */
	public HttpURLConnection getStudyConnection(){
		URL url = new URL(host + "/ws/study/v1")
		return url.openConnection()
	}

	/**
	* Gets the connection for accessing the study subject endpoint in openclinica web services.
	* @return URL for connecting to the study subject endpoint.
	*/
	public HttpURLConnection getStudySubjectConnectionURL(){
		URL url = new URL(host + "/ws/studySubject/v1")
		return url.openConnection()
	}
}
