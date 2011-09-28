package org.openxdata.oc.transport.factory

import java.net.HttpURLConnection;

public class ConnectionFactoryIpml implements ConnectionFactory {

	public HttpURLConnection getConnection() {
		URL url = new URL("http://localhost:8080/OpenClinica-ws-3.1.1/ws/study/v1")
		return url.openConnection()
	}

}
