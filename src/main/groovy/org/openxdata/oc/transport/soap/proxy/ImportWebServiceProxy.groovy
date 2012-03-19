package org.openxdata.oc.transport.soap.proxy

import groovy.util.logging.Log

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.model.ODMInstanceDataDefinition
import org.openxdata.oc.transport.HttpTransportHandler
import org.openxdata.oc.transport.soap.SoapRequestProperties

@Log
class ImportWebServiceProxy extends SoapRequestProperties {

	def envelope

	def getSoapEnvelope(def importXml){
		envelope = """<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:v1="http://openclinica.org/ws/data/v1">
							${getHeader()}
						<soapenv:Body>
							<v1:importRequest>${importXml}</v1:importRequest>
						</soapenv:Body>
					 </soapenv:Envelope>"""
	}
	
	def importData(def instanceData){

		log.info("Starting import to Openclinca.")

		def importXml = new ODMInstanceDataDefinition().appendInstanceData(instanceData)

		envelope = getSoapEnvelope(importXml)

		def transportHandler = new HttpTransportHandler(envelope:envelope)
		def response = transportHandler.sendRequest(connectionFactory.getStudyConnection())

		return getImportMessage(response)
	}
	
	private def getImportMessage(response) {
		
		def result = response.depthFirst().result[0].text()
		if(result == "Success"){
			log.info("Data successfully exported to OpenClinica.")
			return result
		}
		else{
			
			log.info("Data Export to OpenClinica Failed with Error: ${result}")
			throw new ImportException('The Import to OpenClinica didnot complete successfully. Check logs for more information.')
		}
	}
}
