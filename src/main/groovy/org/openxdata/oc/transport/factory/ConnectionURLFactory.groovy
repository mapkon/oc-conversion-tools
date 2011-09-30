package org.openxdata.oc.transport.factory

public class ConnectionURLFactory {

	public HttpURLConnection getStudyConnection(){
		URL url = new URL("http://localhost:8080/OpenClinica-ws-3.1.1/ws/study/v1")
		return url.openConnection()
	}

	public HttpURLConnection getStudySubjectConnectionURL(){
		URL url = new URL("http://localhost:8080/OpenClinica-ws-3.1.1/ws/studySubject/v1")
		return url.openConnection()
	}
}
