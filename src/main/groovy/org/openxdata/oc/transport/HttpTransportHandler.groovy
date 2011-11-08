package org.openxdata.oc.transport

import groovy.util.Node
import groovy.util.logging.Log

import java.io.InputStream;
import java.net.HttpURLConnection

import org.openxdata.oc.exception.UnAvailableException

@Log
class HttpTransportHandler {

	def envelope
	def connection

	def sendRequest(def conn) {

		log.info('Sending request to: ' + conn.getURL())

		this.connection = conn

		def is = readStream()

		return parseSoapXml(is.text)
	}

	private def readStream() {

		try{
			return establishConnection()
		}catch (Exception ex){
			log.info(ex.getMessage())
			log.info('Error Processing connection to: ' + connection.getURL())
			throw new UnAvailableException('Connection Failed', ex)
		}
	}

	private def establishConnection() {

		setConnectionProperties()

		def bytes = envelope.getBytes()

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

		def validXML

		if(response.startsWith("--") && response.endsWith("--")){
			log.info("Parsing returned XML to remove characters not allowed in prolong.")

			def beginIndex = response.indexOf("""<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">""")
			def endIndex = response.indexOf("</SOAP-ENV:Envelope>")

			validXML = response.substring(beginIndex, endIndex)
			validXML = buildSoapResponseXML(validXML)
		}
		else{
			validXML = response
		}

		return new XmlParser().parseText(validXML)
	}

	private def buildSoapResponseXML(def validXML) {
		def builder = new StringBuilder()
		builder.append(validXML)
		builder.append("</SOAP-ENV:Envelope>")

		return builder.toString()
	}
}
