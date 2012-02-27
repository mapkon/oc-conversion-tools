package org.openxdata.oc.transport

import groovy.util.logging.Log

import org.openxdata.oc.exception.UnAvailableException

@Log
class HttpTransportHandler {

	def envelope
	def connection

	def sendRequest(def conn) {

		log.info("Sending request to: ${conn.getURL()}")

		this.connection = conn

		def is = readStream()

		return parseSoapXml(is.text)
	}

	private def readStream() {

		try{
			return establishConnection()
		}catch (def ex){
			log.info("Error Processing connection to: ${connection.getURL()}. Exception: ${ex.getMessage()}")
			throw new UnAvailableException("Error Processing connection to: ${connection.getURL()} with exception: ${ex.getMessage()}")
		}
	}

	private def establishConnection() {

		setConnectionProperties()

		writeToOutputStream()
		def inputStream = connection.getInputStream()

		return inputStream
	}

	private setConnectionProperties() {

		def bytes = envelope.getBytes()

		connection.setRequestMethod('POST')
		connection.setDoOutput(true)

		connection.setRequestProperty('Content-Length', bytes.length.toString())
		connection.setRequestProperty('Content-Type', 'text/xml')
	}

	private def writeToOutputStream() {
		def bytes = envelope.getBytes()
		def outputStream = connection.getOutputStream()
		outputStream.write(bytes)
	}

	private def parseSoapXml(def response){

		def validXml

		if(response.startsWith("--") && response.endsWith("--")){
			validXml = cleanXML(response)
		}
		else{
			validXml = response
		}

		return new XmlParser().parseText(validXml)
	}

	private String cleanXML(def response) {
		
		def validXml
		
		log.info("Parsing returned XML to remove characters not allowed in prolong.")

		def beginIndex = response.indexOf("""<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">""")
		def endIndex = response.indexOf("</SOAP-ENV:Envelope>")

		validXml = response.substring(beginIndex, endIndex)
		validXml = buildSoapResponseXML(validXml)
		return validXml
	}

	private def buildSoapResponseXML(def validXML) {
		"${validXML}</SOAP-ENV:Envelope>"
	}
}
