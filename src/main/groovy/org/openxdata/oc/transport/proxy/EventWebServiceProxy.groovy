package org.openxdata.oc.transport.proxy

import groovy.util.logging.Log

import org.openxdata.oc.transport.HttpTransportHandler;
import org.openxdata.oc.transport.soap.SoapRequestProperties

@Log
class EventWebServiceProxy extends SoapRequestProperties {

	def getSoapEnvelope(def studyOID) {
		""" <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/event/v1" xmlns:bean="http://openclinica.org/ws/beans">
			     ${getHeader()}
			   <soapenv:Body>
			      <v1:findEventsByStudyOidRequest>
         			<v1:studyOid>${studyOID}</v1:studyOid>
				  </v1:findEventsByStudyOidRequest>
			   </soapenv:Body>
			</soapenv:Envelope>"""
	}
	
	def findEventsByStudyOID(def studyOID) {
		log.info("Fetching events for Openclinica study with ID: ${studyOID}")
		
		def envelope = getSoapEnvelope(studyOID)
		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getEventConnection())
		
		def events = response.depthFirst().events[0]
		
		return events
	}
}
