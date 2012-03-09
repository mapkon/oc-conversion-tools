package org.openxdata.oc.proto

import groovy.xml.StreamingMarkupBuilder

class SubmissionProtocol {

	def instanceDataXml
	
	def createOpenClinicaInstanceData(def openXdataInstanceData) {

		def xml
		instanceDataXml = new XmlSlurper().parseText(openXdataInstanceData)

		def subjectKey = getSubjectKey()

		// TODO How does openclinica handle repeat data?
		def itemGroupOIDS = getItemGroupOIDS()

		instanceDataXml.each {

			xml = new StreamingMarkupBuilder().bind{

				ODM(Description:instanceDataXml.@Description, formKey:instanceDataXml.@formKey, name:instanceDataXml.@name) {

					ClinicalData (StudyOID:instanceDataXml.@StudyOID, MetaDataVersionOID:instanceDataXml.@MetaDataVersionOID) {

						SubjectData(SubjectKey:subjectKey) {

							StudyEventData(StudyEventOID:instanceDataXml.@StudyEventOID){

								FormData(FormOID:instanceDataXml.@formKey) {

									itemGroupOIDS.each { itemGroupOID ->
										
										ItemGroupData(ItemGroupOID:itemGroupOID) {

											def itemDataNodes = getItemGroupItemDataNodes(itemGroupOID)
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

		return xml.toString()
	}

	def getSubjectKey() {
		
		def subjectDataNode = instanceDataXml.depthFirst().find { it.name().equals("SubjectKey")}
		return subjectDataNode.text()

	}

	def getItemGroupItemDataNodes(String itemGroupOID) {

		def itemNodes = []

		instanceDataXml.children().each {

			// Assumption is that this is an instance definition for a repeat question
			if(it.@ItemGroupOID == "") {
				if(it.children().size() > 0) {
					it.children().each { item ->
						if(item.@ItemGroupOID.equals(itemGroupOID)) {
							itemNodes.add(item)
						}
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

	def getItemGroupOIDS() {

		def itemGroupOIDS = []as Set
		instanceDataXml.children().each {
			def childNodes = it.children()
			if(childNodes.size() > 0) {
				childNodes.each {
					itemGroupOIDS.add(it.@ItemGroupOID.toString())
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
}
