package org.openxdata.oc.transport.soap.proxy

import groovy.util.logging.Log

import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties

@Log
class SubjectEventWebServiceProxy extends SoapRequestProperties{

	@Override
	public def getSoapEnvelope(def studyOID) {
		""" <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/event/v1" xmlns:bean="http://openclinica.org/ws/beans">
			     ${getHeader()}
			   <soapenv:Body>
			      <v1:findStudySubjectEventsByStudyOidRequest>
			         <v1:studyOid>${studyOID}</v1:studyOid>
			      </v1:findStudySubjectEventsByStudyOidRequest>
			   </soapenv:Body>
			</soapenv:Envelope>"""
	}
	
	def findStudySubjectEventsByStudyOIDRequest(def studyOID) {
		
		log.info("Fetching subject events for Openclinica study with ID: ${studyOID}")
		
		def envelope = getSoapEnvelope(studyOID)
		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getEventConnection())
		
		def events = response.depthFirst().studySubjects[0]
		
		return events
	}
}
