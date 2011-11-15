package org.openxdata.oc.transport.proxy

import groovy.util.logging.Log

import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties

@Log
class StudyMetaDataWebServiceProxy extends SoapRequestProperties {

	def envelope
	def identifier
	def connectionFactory
	
	def getSoapEnvelope() {
		return getEnvelope(identifier)
	}

	def getMetaData(String identifier){
		
		log.info("Fetching Metadata for Openclinica study with ID: ${identifier}")
		
		envelope = getEnvelope(identifier)
		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getStudyConnection())
			
		def odmElement = response.depthFirst().odm[0].children()[0]
		return odmElement
	}

	private def getEnvelope(def identifier){
		envelope = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/study/v1" xmlns:bean="http://openclinica.org/ws/beans">
							${getHeader()}
						<soapenv:Body>
						<v1:getMetadataRequest>
						   <v1:studyMetadata>
							  <bean:identifier>${identifier}</bean:identifier>
						   </v1:studyMetadata>
						</v1:getMetadataRequest>
					 </soapenv:Body>
					</soapenv:Envelope>"""
	}
}
