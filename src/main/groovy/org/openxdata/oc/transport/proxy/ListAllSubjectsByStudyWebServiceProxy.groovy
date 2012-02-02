package org.openxdata.oc.transport.proxy

import groovy.util.logging.Log
import groovy.xml.Namespace

import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties

@Log
class ListAllSubjectsByStudyWebServiceProxy extends SoapRequestProperties {

	def envelope
	def identifier

	def getSoapEnvelope(def studyOID){
		envelope = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/studySubject/v1" xmlns:bean="http://openclinica.org/ws/beans">
							${getHeader()}
						<soapenv:Body>
						  <v1:listAllByStudyRequest>
							 <bean:studyRef>
								<bean:identifier>${studyOID}</bean:identifier>
							 </bean:studyRef>
						  </v1:listAllByStudyRequest>
					   </soapenv:Body>
					</soapenv:Envelope>"""
	}
	
	def listAllByStudy(def identifier){

		log.info("Fetching subject keys for Openclinica study with ID: ${identifier}")

		envelope = getSoapEnvelope(identifier)

		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getStudySubjectConnection())

		def subjectKeys = extractSubjectKeys(response)

		log.info("Found : " + subjectKeys.size() + " Subjects attached to Study with Identifier: ${identifier}")
		return subjectKeys
	}

	private def extractSubjectKeys(def response){

		def subjectKeys = []
		def ns = new Namespace("http://openclinica.org/ws/beans", "ns2")
		def subjects = response.depthFirst()[ns.subject].each {
			subjectKeys.add(it[ns.uniqueIdentifier].text())
		}

		return subjectKeys
	}
}
