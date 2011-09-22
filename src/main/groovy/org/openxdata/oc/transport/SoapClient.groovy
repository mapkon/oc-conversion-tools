package org.openxdata.oc.transport

import groovyx.net.ws.WSClient


class SoapClient {

	def host
	def proxy

	SoapClient(def host){
		this.host = host
	}

	def getStudies() {
		proxy = new WSClient(host, this.class.classLoader)
		proxy.initialize()

		def studies = new ArrayList<String>()
		proxy.listAll().each {
			studies.add(it)
		}

		return studies
	}
}
