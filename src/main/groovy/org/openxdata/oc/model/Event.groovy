package org.openxdata.oc.model

class Event {

class Event {

	private def eventXml
	
	private def ordinal
	private def eventName
	private def eventDefinitionOID

	def Event(def eventXml) {

		name = eventXml.eventName.text()
		ordinal = eventXml.ordinal.text()
		eventDefinitionOID = eventXml.eventDefinitionOID.text()

		this.eventXml = eventXml
	}

	def getSubjectKeys() {

		def subjectKeys = []

		def keys = eventXml.studySubjectOIDs.text()

		subjectKeys = keys.split()

		return subjectKeys
	}
	
	def getFormOIDs() {
		
		def formOIDs = []
		
		def oids = eventXml.formOID.text()
		
		formOIDs = oids.split()
		
		return formOIDs
	}
}
