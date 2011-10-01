package org.openxdata.oc

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import java.util.Collection


@Log
public class ODMBuilder {

	public String buildODM(Collection<String> instanceData) {
		
		log.info("Starting building of ODM file")
		
		def odm = """<ODM></ODM>"""
		def parser = new XmlParser()
		def odmXml = parser.parseText(odm)
		instanceData.each {
			def instanceXml = parser.parseText(it)
			addSubjectData(odmXml, instanceXml)
		}
		
		log.info("<<Successfully built ODM file.>>")
		return XmlUtil.asString(odmXml)
	}

	def addSubjectData(def xml, def instanceNode) {
		
		log.info("Adding subject Data.")
		
		def studyOID = instanceNode.ClinicalData.@StudyOID[0]
		def metadataVersion = instanceNode.ClinicalData.@MetaDataVersionOID[0]
		def clinicalData = xml.ClinicalData.find {it.@StudyOID == studyOID && it.@MetaDataVersionOID == metadataVersion}
		if (clinicalData == null) {
			clinicalData = new Node(xml, "ClinicalData", ['StudyOID':studyOID, 'MetaDataVersion':metadataVersion])
		}
		clinicalData.append(instanceNode.ClinicalData.SubjectData)
	}
}
