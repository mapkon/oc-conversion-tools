package org.openxdata.oc.model

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.proto.DefaultSubmissionProtocol
import org.openxdata.server.admin.model.FormData

@Log
class ODMInstanceDataDefinition {
	
	def submissionProtocol = new DefaultSubmissionProtocol()
	
	List<String> processInstanceData(List<FormData> instanceData){

		if(instanceData.isEmpty())
			throw new ImportException('Cannot process empty instance data.')
					
		def odmInstanceData = []

		instanceData.each {
			
			def ocData = submissionProtocol.createOpenClinicaInstanceData(it.getData())
			
			odmInstanceData.add(ocData)
			
			log.info("Processing converted instance data: ${XmlUtil.serialize(ocData)}")
		}

		return odmInstanceData
	}
}
