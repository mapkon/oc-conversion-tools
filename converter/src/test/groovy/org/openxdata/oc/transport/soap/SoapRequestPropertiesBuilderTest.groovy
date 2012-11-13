package org.openxdata.oc.transport.soap

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.soap.proxy.CRFMetaDataVersionProxy


class SoapRequestPropertiesBuilderTest extends GroovyTestCase {
	
	def headerNode
	
	@Before public void setUp(){
		def crfMetaDataProxy = new CRFMetaDataVersionProxy(username:"uname", hashedPassword:"pass", connectionFactory:null)
		
		def header = crfMetaDataProxy.getHeader()
		headerNode = new XmlSlurper().parseText(header)		
	}
	
	@Test void testGetHeaderShouldReturnValidHeader(){
		
		assertEquals 'Header', headerNode.name()
	}
	
	@Test void testGetHeaderShouldReturnValidSecurityElement(){
		
		assertEquals 'Security', headerNode.children()[0].name()
	}
	
	@Test void testGetHeaderShouldReturnValidUsername(){
		
		assertEquals 'uname', headerNode.depthFirst().find{it.name().equals('Username')}.text()
	}
	
	@Test void testGetHeaderShouldReturnValidPassword(){
		
		assertEquals 'pass', headerNode.depthFirst().find{it.name().equals('Password')}.text()
	}

}
