package org.openxdata.oc.transport.soap.proxy

import groovy.util.logging.Log

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
	
	HashMap<String, String> importData(def instanceData){

		log.info("Starting import to Openclinca.")

		def messages = [:]
		def importXml = new ODMInstanceDataDefinition().appendInstanceData(instanceData)

		importXml.each {
			
			envelope = getSoapEnvelope(it)

			def transportHandler = new HttpTransportHandler(envelope:envelope)
			def response = transportHandler.sendRequest(connectionFactory.getStudyConnection())

			def message = getImportWebServiceResponse(response)
			
			def xml = new XmlSlurper().parseText(it)
			
			def formKey = xml.@formKey.text()
			
			messages.put(formKey, message)
			
		}
		
		return messages
	}
	
	private def getImportWebServiceResponse(response) {
		
		def result = response.depthFirst().result[0].text()
		
		if(result == "Success"){
			log.info("Data successfully exported to OpenClinica.")
		}
		else{
			
			result = result + ":" + " ${response.depthFirst().error[0].text() }"
			log.info("Data Export to OpenClinica Failed with Error: ${result}")
		}
		
		return result
	}
}
