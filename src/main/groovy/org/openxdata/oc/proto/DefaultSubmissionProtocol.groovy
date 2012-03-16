package org.openxdata.oc.proto

import groovy.util.logging.Log
import groovy.xml.StreamingMarkupBuilder

@Log
class DefaultSubmissionProtocol {

	def instanceDataXml

	def createOpenClinicaInstanceData(def openXdataInstanceData) {

		def xml
		instanceDataXml = new XmlSlurper().parseText(openXdataInstanceData)

		def subjectKey = getSubjectKey()

		def itemGroupOIDS = getItemGroupOIDS()

		xml = new StreamingMarkupBuilder().bind {

			ODM(Description:instanceDataXml.@Description, formKey:instanceDataXml.@formKey, name:instanceDataXml.@name) {

				ClinicalData (StudyOID:instanceDataXml.@StudyOID, MetaDataVersion:instanceDataXml.@MetaDataVersionOID) {

					SubjectData(SubjectKey:subjectKey) {

						StudyEventData(StudyEventOID:instanceDataXml.@StudyEventOID){

							FormData(FormOID:instanceDataXml.@formKey) {

								itemGroupOIDS.eachWithIndex { itemGroupOID, idx ->

									def itemDataNodes = getItemGroupItemDataNodes(itemGroupOID)
									def node = instanceDataXml.children().find { it.name().equals(itemGroupOID) }

									if(isRepeat(node)) {

										ItemGroupData(ItemGroupOID:itemGroupOID, ItemGroupRepeatKey:idx, TransactionType:"Insert" ) {

											itemDataNodes.each { itemData ->

												ItemData (ItemOID:itemData.name(), Value:"$itemData"){
												}
											}
										}
									}
									else {
										ItemGroupData(ItemGroupOID:itemGroupOID, TransactionType:"Insert" ) {

											itemDataNodes.each { itemData ->

												ItemData (ItemOID:itemData.name(), Value:"$itemData"){
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

		return instanceDataXml.depthFirst().find { it.name().equals("SubjectKey")}.text()

	}

	private def getItemGroupItemDataNodes(String itemGroupOID) {

		def itemNodes = []

		instanceDataXml.children().each {

			if(isRepeat(it)) {

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

		def itemGroupOIDS = []as Set

		instanceDataXml.children().each {

			if(isRepeat(it)) {

				it.children().each { item ->
					itemGroupOIDS.add(item.@ItemGroupOID.toString())
				}
			}
			else {
				if(!it.name().equals("SubjectKey")) {
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
}
