package org.openxdata.oc.transport.factory

public class ConnectionURLFactory {

	public HttpURLConnection getStudyConnection(String host){
		URL url = new URL(host + "/ws/study/v1")
		return url.openConnection()
	}

	public HttpURLConnection getStudySubjectConnectionURL(String host){
		URL url = new URL(host + "/ws/studySubject/v1")
		return url.openConnection()
	}
}
