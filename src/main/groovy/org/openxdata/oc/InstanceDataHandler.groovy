package org.openxdata.oc

import groovy.util.logging.Log
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.proto.DefaultSubmissionProtocol
import org.openxdata.server.admin.model.FormData

@Log
class InstanceDataHandler {

	def submissionProtocol = new DefaultSubmissionProtocol()

	List<String> processInstanceData(List<FormData> instanceData){

		if(instanceData.isEmpty())
			throw new ImportException('Cannot process empty instance data.')

		def odmInstanceData = []

		instanceData.each {

			def xml = cleanXml(it.getData())

			def ocData = submissionProtocol.createODMInstanceData(xml)

			odmInstanceData.add(ocData)

			log.info("Processing converted instance data: ${XmlUtil.serialize(ocData)}")
		}

		return odmInstanceData
	}

	def cleanXml(def xml) {

		def oxdInstanceData = new XmlSlurper().parseText(xml)

		oxdInstanceData.children().each {
			String name = it.name()
			if(name.endsWith("_HEADER")) {
				it.replaceNode{}
			}
		}

		def newXml = new StreamingMarkupBuilder().bind {  mkp.yield oxdInstanceData }.toString()

		return newXml
	}
}
