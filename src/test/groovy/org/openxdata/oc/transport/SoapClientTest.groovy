package org.openxdata.oc.transport

import groovyx.net.ws.WSClient

import org.junit.Test


class SoapClientTest extends GroovyTestCase {

	@Test void testShouldShowGroovySOAPWorking() {
		def proxy = new WSClient('http://www.w3schools.com/webservices/tempconvert.asmx?WSDL', this.class.classLoader)
		proxy.initialize()
		def response = proxy.CelsiusToFahrenheit (3)

		assertNotNull(response)
		assertEquals("37.4", response)
	}
	
	@Test void testShouldReturnNullStudies(){
		def proxy = new SoapClient('oc url')
		List<String> studies = proxy.getStudies()
		assertNull(studies)
	}
}
