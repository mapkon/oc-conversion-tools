package org.openxdata.oc.proto

import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class SubmissionProtocol {

	def instanceDataXml
	def createOpenClinicaInstanceData(def openXdataInstanceData) {

		def xml
		instanceDataXml = new XmlSlurper().parseText(openXdataInstanceData)

		def formOIDS = getFormOIDS()
		def itemGroupOIDS = getItemGroupOIDS()

		instanceDataXml.each {

			xml = new StreamingMarkupBuilder().bind{

				ODM(Description:instanceDataXml.@Description, formKey:instanceDataXml.@formKey, name:instanceDataXml.@name) {

					ClinicalData (StudyOID:instanceDataXml.@StudyOID, MetaDataVersionOID:instanceDataXml.@MetaDataVersionOID) {

						SubjectData(SubjectKey:instanceDataXml.@SubjectKey) {

							StudyEventData(StudyEventOID:instanceDataXml.@StudyEventOID){

								formOIDS.each { formOID ->

									FormData(FormOID:formOID) {

										def tempOID
										def itemNodes = instanceDataXml.children()

										itemNodes.each {

											def itemFormOID = it.@FormOID.toString()
											def itemGroupOID = it.@ItemGroupOID
											if(itemFormOID.equals(formOID.toString())) {

												if(!tempOID.equals(itemFormOID)) {
													ItemGroupData (ItemGroupOID:itemGroupOID) {

														def itemDataNodes = getItemGroupItemDataNodes(itemGroupOID)
														itemDataNodes.each {
															
															ItemData (ItemOID:it.name(), value:"$it"){
															}
														}
													}
													tempOID = itemFormOID
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

		println XmlUtil.serialize(xml)
		return xml.toString()
	}

	def getItemGroupItemDataNodes(def itemGroupOID) {

		def itemNodes = [] 
		def itemGroups = instanceDataXml.children().findAll { it.@ItemGroupOID == itemGroupOID}
		
		itemGroups.each {
			if(it.children().size() > 0) {
				it.children().each { item ->
					itemNodes.add(item)
				}
			}
			else if(it.children().size() == 0){
				itemNodes.add(it)
			}
		}
		return itemNodes
	}

	def getFormOIDS() {

		def formOIDS = []as Set
		instanceDataXml.children().each {

			def childNodes = it.children()
			if (childNodes.size() > 0) {
				childNodes.each {
					formOIDS.add(it.@FormOID.toString())
				}
			}
			else {
				formOIDS.add(it.@FormOID.toString())
			}
		}

		return formOIDS
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
				itemGroupOIDS.add(it.@ItemGroupOID.toString())
			}
		}

		return itemGroupOIDS
	}
}
