package org.openxdata.oc.model

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ErrorCode
import org.openxdata.oc.exception.ImportException

@Log
class ODMDefinition {
	
	def instanceData
	
	def appendInstanceData(def instanceData){

		if(instanceData.isEmpty())
			throw new ImportException(ErrorCode.EMPTY_INSTANCE_DATA)
					
		this.instanceData = instanceData

		def odm = """<ODM></ODM>"""
		def odmXml = new XmlParser().parseText(odm)

		instanceData.each {
			
			log.info("Processing instance data: " + it)
			def instanceXml = new XmlParser().parseText(it)
			addSubjectData(odmXml, instanceXml)
		}

		log.info("<<Successfully appended instance data ODM file.>>")
		
		return XmlUtil.asString(odmXml)
	}
	
	private def addSubjectData(def xml, def instanceNode) {
		
		log.info("Adding subject Data.")
		
		def studyOID = instanceNode.ClinicalData.@StudyOID[0]
		def metadataVersion = instanceNode.ClinicalData.@MetaDataVersionOID[0]
		def clinicalData = xml.ClinicalData.find {it.@StudyOID == studyOID && it.@MetaDataVersionOID == metadataVersion}
		if (clinicalData == null) {
			clinicalData = new Node(xml, "ClinicalData", ['StudyOID':studyOID, 'MetaDataVersion':metadataVersion])
		}
		
		clinicalData.append(instanceNode.ClinicalData.SubjectData)
		
		log.info("<<Successfully added subject data ODM file.>>")
	}
}
