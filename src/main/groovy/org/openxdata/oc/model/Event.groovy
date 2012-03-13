package org.openxdata.oc.model

class Event {

	def ordinal
	def eventDefinitionOID

	def eventXml

	def Event(def eventXml) {

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
