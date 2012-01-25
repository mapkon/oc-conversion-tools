package org.openxdata.oc.transport.soap

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.transport.proxy.ListAllWebServiceProxy


class SoapRequestPropertiesBuilderTest extends GroovyTestCase {
	
	def listAllProxy 
	def getMetaDataProxy
	def parser = new XmlSlurper() 
	
	@Before public void setUp(){
		listAllProxy = new ListAllWebServiceProxy(username:"uname", hashedPassword:"pass", connectionFactory:null)
	}
	
	@Test void testGetHeaderShouldReturnValidHeader(){
		
		def header = listAllProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'Header', headerXml.name()
	}
	
	@Test void testGetHeaderShouldReturnValidSecurityElement(){
		
		def header = listAllProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'Security', headerXml.children()[0].name()
	}
	
	@Test void testGetHeaderShouldReturnValidUsername(){
		
		def header = listAllProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'uname', headerXml.depthFirst().find{it.name().equals('Username')}.text()
	}
	
	@Test void testGetHeaderShouldReturnValidPassword(){
		
		def header = listAllProxy.getHeader()
		def headerXml = parser.parseText(header)
		
		assertEquals 'pass', headerXml.depthFirst().find{it.name().equals('Password')}.text()
	}
	
	@Test void testListAllSoapRequestEnvelopeShouldReturnValidEnvelope(){
		
		def envelope = listAllProxy.getSoapEnvelope()
		def envelopeXML = parser.parseText(envelope)
		
		assertEquals 'Envelope', envelopeXML.name()
	}
	
	@Test void testListAllSoapRequestEnvelopeShouldReturnValidEnvelopeWithHeaderElement(){
		
		def envelope = listAllProxy.getSoapEnvelope()
		def envelopeXML = parser.parseText(envelope)
		
		assertEquals 'Header', envelopeXML.children()[0].name()
	}

}
