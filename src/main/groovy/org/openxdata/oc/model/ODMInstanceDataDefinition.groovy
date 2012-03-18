package org.openxdata.oc.model

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.proto.DefaultSubmissionProtocol

@Log
class ODMInstanceDataDefinition {
	
	def submissionProtocol = new DefaultSubmissionProtocol()
	
	def appendInstanceData(def instanceData){

		if(instanceData.isEmpty())
			throw new ImportException('Cannot process empty instance data.')
					
		def odmInstanceData

		instanceData.each { 
			
			odmInstanceData = submissionProtocol.createOpenClinicaInstanceData(it)
			log.info("Processing converted instance data: ${XmlUtil.serialize(odmInstanceData)}")
		}

		return odmInstanceData
	}
}
