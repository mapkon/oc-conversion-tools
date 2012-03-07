package org.openxdata.oc.transport.soap

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.soap.proxy.CRFMetaDataVersionProxy


class SoapRequestPropertiesBuilderTest extends GroovyTestCase {
	
	def crfMetaDataProxy 
	def parser = new XmlSlurper() 
	
	@Before public void setUp(){
		crfMetaDataProxy = new CRFMetaDataVersionProxy(username:"uname", hashedPassword:"pass", connectionFactory:null)
	}
	
	@Test void testGetHeaderShouldReturnValidHeader(){
		
		def header = crfMetaDataProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'Header', headerXml.name()
	}
	
	@Test void testGetHeaderShouldReturnValidSecurityElement(){
		
		def header = crfMetaDataProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'Security', headerXml.children()[0].name()
	}
	
	@Test void testGetHeaderShouldReturnValidUsername(){
		
		def header = crfMetaDataProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'uname', headerXml.depthFirst().find{it.name().equals('Username')}.text()
	}
	
	@Test void testGetHeaderShouldReturnValidPassword(){
		
		def header = crfMetaDataProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'pass', headerXml.depthFirst().find{it.name().equals('Password')}.text()
	}

}
