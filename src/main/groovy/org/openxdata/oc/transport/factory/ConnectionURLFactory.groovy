package org.openxdata.oc.transport.factory

public class ConnectionURLFactory {

	def host
	public ConnectionURLFactory(String host){
		this.host = host
	}
	
	public HttpURLConnection getStudyConnection(){
		URL url = new URL(host + "/ws/study/v1")
		return url.openConnection()
	}

	public HttpURLConnection getStudySubjectConnectionURL(){
		URL url = new URL(host + "/ws/studySubject/v1")
		return url.openConnection()
	}
}
