package org.openxdata.oc.transport.factory


/**
 * Builds a connection for accessing OpenClinca web services end-point.
 *
 */
public class ConnectionFactory {

	def url
	def host
	
	def getCRFConnection() {
		
		url = new URL("${host}/ws/crf/v1")
		return url.openConnection()
	}
	
	public HttpURLConnection getStudyConnection(){

		url = new URL("${host}/ws/study/v1")
		return url.openConnection()
	}

	public HttpURLConnection getEventConnection() {
		url = new URL("${host}/ws/event/v1")
		return url.openConnection()
	}
}
