package org.openxdata.oc.transport

import groovyx.net.ws.WSClient

import org.junit.Test


class SoapClientTest extends GroovyTestCase {

	def host = 'http://www.w3schools.com/webservices/tempconvert.asmx?WSDL'
	@Test void testShouldShowGroovySOAPWorking() {
		def proxy = new WSClient(host, this.class.classLoader)
		proxy.initialize()
		def response = proxy.CelsiusToFahrenheit (3)

		assertNotNull(response)
		assertEquals("37.4", response)
	}
}
