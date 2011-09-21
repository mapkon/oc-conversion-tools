package org.openxdata.oc

import groovy.xml.XmlUtil;

class ODMBuilder {

	String buildODM(Collection<String> instanceData) {
		def odm = """<ODM><ClinicalData/></ODM>"""
		def parser = new XmlParser()
		def odmXml = parser.parseText(odm)
		instanceData.each {
			def instanceXml = parser.parseText(it)
			odmXml.ClinicalData.find { true }.children().add(instanceXml)
		}

		return XmlUtil.serialize(odmXml)
	}
}
