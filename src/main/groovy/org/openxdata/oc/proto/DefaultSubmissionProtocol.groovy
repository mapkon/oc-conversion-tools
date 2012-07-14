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

	def createODMInstanceData(def openXdataInstanceData) {

		def xml
		instanceDataXml = new XmlSlurper().parseText(openXdataInstanceData)

		log.info("Creating OpenClinica Instance Data for Form: " + instanceDataXml.@formKey)

		def subjectKey = getSubjectKey()

		def itemGroupOIDS = getItemGroupOIDS()

		xml = new StreamingMarkupBuilder().bind {

			ODM(Description:instanceDataXml.@Description, formKey:instanceDataXml.@formKey, name:instanceDataXml.@name) {

				ClinicalData (StudyOID:instanceDataXml.@StudyOID, MetaDataVersion:instanceDataXml.@MetaDataVersionOID) {

					SubjectData(SubjectKey:subjectKey) {

						StudyEventData(StudyEventOID:instanceDataXml.@StudyEventOID){

							FormData(FormOID:instanceDataXml.@formKey) {

								def currentRepeat

								itemGroupOIDS.each { itemGroupOID ->

									if(transformUtil.isRepeat(instanceDataXml, oid)) {

										// Extract nodes for current repeat group
										def nodes = instanceDataXml.children().findAll { it.name().equals(itemGroupOID) }

										nodes.eachWithIndex { node, idx ->

											// Make sure we don't iterate over the same repeat twice.
											if(!currentRepeat.equals(itemGroupOID)) {

												ItemGroupData(ItemGroupOID:itemGroupOID, ItemGroupRepeatKey:idx + 1, TransactionType:"Insert" ) {

													node.children().each { itemData ->

														def data = processMultipleSelectValues(itemData.text().trim())
														ItemData (ItemOID:itemData.name(), Value:"$data"){
														}
													}

													// Set current repeat
													currentRepeat = itemGroupOID
												}
											}
										}
									}
									else {

										def itemDataNodes = getItemGroupDataNodes(itemGroupOID)
										ItemGroupData(ItemGroupOID:itemGroupOID, TransactionType:"Insert" ) {

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

		return instanceDataXml.depthFirst().find { it.name().equals("subjectkey")}.text()
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

		def matchingItemNodes = instanceDataXml.children().findAll { it.@ItemGroupOID == itemGroupOID}

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

	public boolean isRepeat(def item) {

		if(item instanceof String) {

			def node = instanceDataXml.children().find { it.name().is(item) }

			if(node.children() != null)
				return node.children().size() > 0

		}else {
			return item.children().size() > 0
		}
	}

	def processMultipleSelectValues(def xml) {

		return xml.replaceAll("(?<=\\d) (?=\\d)", ",")
	}
}
