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
		
		def props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')
		host = props.getAt('host')
	}
	
	def getCRFConnection() {
		
		url = new URL("${host}/ws/crf/v1")
		return url.openConnection()
	}
	
	public HttpURLConnection getStudyConnection(){

		url = new URL("${host}/ws/study/v1")
		return url.openConnection()
	}

	public HttpURLConnection getStudySubjectConnection(){
		
		url = new URL("${host}/ws/studySubject/v1")
		return url.openConnection()
	}
}
