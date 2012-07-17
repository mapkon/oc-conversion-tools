package org.openxdata.oc.proto

import org.openxdata.oc.util.TransformUtil

import groovy.util.logging.Log
import groovy.xml.StreamingMarkupBuilder

@Log
class DefaultSubmissionProtocol {

	def transformUtil
	def instanceDataXml

	public DefaultSubmissionProtocol() {

		transformUtil = new TransformUtil()
	}

	/**
	 * Creates instance data that conforms to the ODM specification. 
	 * 
	 * <p>
	 * 
	 * <b>Note:</b>
	 * <p>
	 * 
	 * This method assumes that the data (including repeating items) are unique or else it generates duplicate data.
	 * 
	 * @param instanceData the instance data to process
	 * 
	 * @return correctly formatted instance data conforming to the ODM specification.
	 */
	def createODMInstanceData(def instanceData) {

		def xml
		instanceDataXml = new XmlSlurper().parseText(instanceData)

		log.info("Creating OpenClinica Instance Data for Form: " + instanceDataXml.@formKey)

		def subjectKey = getSubjectKey()

		def itemGroupOIDS = getItemGroupOIDS()

		xml = new StreamingMarkupBuilder().bind {

			ODM(Description:instanceDataXml.@Description, formKey:instanceDataXml.@formKey, name:instanceDataXml.@name) {

				ClinicalData (StudyOID:instanceDataXml.@StudyOID, MetaDataVersion:instanceDataXml.@MetaDataVersionOID) {

					SubjectData(SubjectKey:subjectKey) {

						StudyEventData(StudyEventOID:instanceDataXml.@StudyEventOID){

							FormData(FormOID:instanceDataXml.@formKey) {

								def processedRepeats = []

								itemGroupOIDS.each { oid ->

									if(transformUtil.isRepeat(instanceDataXml, oid)) {

										// Use repeat only once since we return all nodes for a given repeat.
										if(!processedRepeats.contains(oid)) {

											// Extract nodes for this repeat
											def repeats = instanceDataXml.depthFirst().findAll {
												it.name().equals(oid)
											}

											repeats.eachWithIndex {repeatOID, id ->

												// Extract nodes for current repeat group (taking into account the repeat key)
												def nodes = instanceDataXml.children().findAll { it.name().equals(oid) && it.@repeatKey.equals(id) }

												nodes.eachWithIndex { node, idx ->

													ItemGroupData(ItemGroupOID:oid, ItemGroupRepeatKey:id + 1, TransactionType:"Insert" ) {

														node.children().each { itemData ->

															def data = processMultipleSelectValues(itemData.text().trim())
															ItemData (ItemOID:itemData.name(), Value:"$data"){
															}
														}
													}
												}
											}
										}

										// Set current repeat so we dont iterate twice
										processedRepeats.add(oid)
									}
									else {

										def itemDataNodes = getItemGroupDataNodes(oid)
										ItemGroupData(ItemGroupOID:oid, TransactionType:"Insert" ) {

											itemDataNodes.each { itemData ->

												def data = processMultipleSelectValues(itemData.text().trim())
												ItemData (ItemOID:itemData.name(), Value:"$data"){
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		log.info("Successfully converted from oxd-instance data to odm-instance data")

		return xml.toString()
	}

	private def getSubjectKey() {

		return instanceDataXml.depthFirst().find {
			it.name().equals("subjectkey")
		}.text()
	}

	private def getItemGroupDataNodes(String itemGroupOID) {

		def itemNodes = []

		instanceDataXml.children().each {

			if(transformUtil.isRepeat(instanceDataXml, it)) {

				it.children().each { item ->
					if(item.@ItemGroupOID.equals(itemGroupOID)) {
						itemNodes.add(item)
					}
				}
			}
		}

		def matchingItemNodes = instanceDataXml.children().findAll { it.@ItemGroupOID == itemGroupOID }

		matchingItemNodes.each {
			if(it.children().size() == 0){
				itemNodes.add(it)
			}
		}
		return itemNodes
	}

	private def getItemGroupOIDS() {

		def itemGroupOIDS = []

		instanceDataXml.children().each {

			if(transformUtil.isRepeat(instanceDataXml, it)) {
				itemGroupOIDS.add(it.name())
			}
			else {
				if(!it.name().equals("subjectkey")) {
					if(!itemGroupOIDS.contains(it.@ItemGroupOID.toString()))
						itemGroupOIDS.add(it.@ItemGroupOID.toString())
				}
			}
		}

		return itemGroupOIDS
	}

	def processMultipleSelectValues(def data) {

		// Replaces any space preceded by digit(s) AND followed by digit(s) with a comma.
		return data.replaceAll("(?<=\\d) (?=\\d)", ",")
	}
}
