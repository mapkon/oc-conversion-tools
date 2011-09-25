package org.openxdata.oc

import groovy.xml.XmlUtil

import java.util.Collection

class ODMBuilder {

	public String buildODM(Collection<String> instanceData) {
		def odm = """<ODM></ODM>"""
		def parser = new XmlParser()
		def odmXml = parser.parseText(odm)
		instanceData.each {
			def instanceXml = parser.parseText(it)
			addSubjectData(odmXml, instanceXml)
		}
		return XmlUtil.serialize(odmXml)
	}

	def addSubjectData(def xml, def instanceNode) {
		def studyOID = instanceNode.ClinicalData.@StudyOID[0]
		def metadataVersion = instanceNode.ClinicalData.@MetaDataVersionOID[0]
		def clinicalData = xml.ClinicalData.find {it.@StudyOID == studyOID && it.@MetaDataVersionOID == metadataVersion}
		if (clinicalData == null) {
			clinicalData = new Node(xml, "ClinicalData", ['StudyOID':studyOID, 'MetaDataVersion':metadataVersion])
		}
		clinicalData.append(instanceNode.ClinicalData.SubjectData)
	}
}
