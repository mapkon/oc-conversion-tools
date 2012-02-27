package org.openxdata.oc.model

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.proto.SubmissionProtocol;

@Log
class ODMInstanceDataDefinition {
	
	def instanceData
	
	def appendInstanceData(def instanceData){

		if(instanceData.isEmpty())
			throw new ImportException('Cannot process empty instance data.')
					
		this.instanceData = instanceData

		def ocInstanceData

		instanceData.each {
			
			def convertedInstanceData = new SubmissionProtocol().createOpenClinicaInstanceData(it)
			
			log.info("Processing converted instance data: \n ${convertedInstanceData}")
			
			def instanceXml = new XmlParser().parseText(convertedInstanceData)
			ocInstanceData = addSubjectData(instanceXml)
		}

		log.info("<<Successfully appended instance data to ODM file.>>")
		
		return XmlUtil.asString(ocInstanceData)
	}
	
	private def addSubjectData(def instanceNode) {
		
		log.info("Adding subject Data.")
		
		def odmTag = new XmlParser().parseText("""<ODM></ODM>""")
		
		def studyOID = instanceNode.ClinicalData.@StudyOID[0]
		def metadataVersion = instanceNode.ClinicalData.@MetaDataVersionOID[0]
		def clinicalDataNode = odmTag.ClinicalData.find {it.@StudyOID == studyOID && it.@MetaDataVersionOID == metadataVersion}
		if (clinicalDataNode == null) {
			clinicalDataNode = new Node(odmTag, "ClinicalData", ['StudyOID':studyOID, 'MetaDataVersion':metadataVersion])
		}
		
		clinicalDataNode.append(instanceNode.ClinicalData.SubjectData)
		
		return clinicalDataNode
		
		log.info("<<Successfully added subject data ODM file.>>")
	}
}
