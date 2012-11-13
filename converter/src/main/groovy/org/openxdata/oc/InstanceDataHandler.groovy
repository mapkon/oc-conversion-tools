package org.openxdata.oc

import groovy.util.logging.Log
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.proto.DefaultSubmissionProtocol
import org.openxdata.oc.util.TransformUtil
import org.openxdata.server.admin.model.FormData

@Log
class InstanceDataHandler {

	def transformUtil = new TransformUtil()
	def submissionProtocol = new DefaultSubmissionProtocol()

	/**
	 * Process a given list of {@link FormData} items. 
	 * 
	 * <p>
	 * 
	 * <b>Note:</b>
	 * 
	 * <p>
	 * This method does a couple of other routines:
	 * 
	 * <ul>
	 * <li>Cleaning the XML to remove the ItemHeader/SubHeader visual elements that were added during conversion)
	 * <li>Isolating repeat groups so that they are unique by add a {@code repeatkey} attribute to xform repeating elements.
	 * </ul>
	 * 
	 * @param instanceDataList List of {@link FormData} items with valid data as xml
	 * 
	 * @return List of processed ODM instance data elements conforming to the ODM spec.
	 */
	List<String> processInstanceData(List<FormData> instanceDataList){

		if(instanceDataList.isEmpty())
			throw new ImportException('Cannot process empty instance data.')

		def odmInstanceData = []

		for(FormData data : instanceDataList) {

			try {
				def xml = cleanXml(data.getData())

				def ocData = submissionProtocol.createODMInstanceData(xml)

				odmInstanceData.add(ocData)

				log.info("Processing converted instance data: ${XmlUtil.serialize(ocData)}")
			} catch(Exception ex) {

				log.info("Error occurred while trying to process instance data with id: ${data.getId()} for export: ${ex.getLocalizedMessage()}")
			}
		}

		return odmInstanceData
	}

	def cleanXml(def input) {

		// Isolate repeats if any
		def xml = transformUtil.isolateRepeats(input)

		def instanceData = new XmlSlurper().parseText(xml)

		instanceData.children().each {
			String name = it.name()
			if(name.endsWith("_HEADER")) {
				it.replaceNode{}
			}
		}

		return new StreamingMarkupBuilder().bind {  mkp.yield instanceData }.toString()
	}
}
