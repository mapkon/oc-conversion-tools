package org.openxdata.oc.transport

import groovyx.net.ws.WSClient


class SoapClient {
	
	def response
	SoapClient(def host){
		proxy = new WSClient(host, this.class.classLoader)
		proxy.initialize()
	}

	def getProxy(){
		return proxy
	}
}
