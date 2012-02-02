package org.openxdata.oc.transport.proxy

import groovy.util.logging.Log
import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties


@Log
class CRFMetaDataVersionProxy extends SoapRequestProperties {

	def envelope
	def studyOID

	def getSoapEnvelope(def studyOID) {
		"""<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/crf/v1">
					   ${getHeader()}
					   <soapenv:Body>
						  <v1:findAllCrfsRequest>
							 <v1:studyoid>${studyOID}</v1:studyoid>
						  </v1:findAllCrfsRequest>
					   </soapenv:Body>
					</soapenv:Envelope>"""
	}
	
	def findAllCRFS(def studyOID) {

		log.info("Fetching CRF- Metadata for Openclinica study with ID: ${studyOID}")

		this.studyOID = studyOID
		envelope = getSoapEnvelope(studyOID)

		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getCRFConnection())

		def odmElement = response.depthFirst().odm[0].children()[0]
		return odmElement
	}
}
