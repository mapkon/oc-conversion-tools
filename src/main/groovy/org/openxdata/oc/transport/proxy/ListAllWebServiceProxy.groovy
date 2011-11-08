package org.openxdata.oc.transport.proxy

import groovy.util.logging.Log

import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties


@Log
class ListAllWebServiceProxy extends SoapRequestProperties {

	def connectionFactory

	def getSoapEnvelope() {
		"""<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/study/v1"> 
				${getHeader()}
			<soapenv:Body>
		   <v1:listAllRequest>?</v1:listAllRequest>
		  </soapenv:Body>
	     </soapenv:Envelope>"""
	}

	public def listAll() {

		log.info('Fetching all available studies...')
		def envelope = getSoapEnvelope()

		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getStudyConnection())

		return extractStudies(response)
	}

	private def extractStudies(def response){
		log.info('Extracting studies from response.')
		
		def studies = []
		response.depthFirst().study.each {
			def study = new ConvertedOpenclinicaStudy()
			study.OID = it.oid.text()
			study.name = it.name.text()
			study.identifier = it.identifier.text()
			studies.add(study)
		}
		return studies
	}
}
