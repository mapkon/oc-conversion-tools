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

		return new XmlParser().parseText(is.text)
	}

	private def readStream() {

		try{
			return establishConnection()
		}catch (def ex){
			log.info("Error Processing connection to: ${connection.getURL()} ${ex.getMessage()}")
			throw new UnAvailableException("Error Processing connection to: ${connection.getURL()} ${ex.getMessage()}")
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
}
