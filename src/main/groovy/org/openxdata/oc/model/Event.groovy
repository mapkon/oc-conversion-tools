package org.openxdata.oc.model

class Event {

	def ordinal
	def formOID
	def eventDefinitionOID

	def eventXml

	def Event(def eventXml) {

		ordinal = eventXml.ordinal.text()
		formOID = eventXml.formOID.text()
		eventDefinitionOID = eventXml.eventDefinitionOID.text()

		this.eventXml = eventXml
	}

	def getSubjectKeys() {

		def subjectKeys = []

		def keys = eventXml.studySubjectOIDs.toString()

		subjectKeys = keys.split()

		return subjectKeys
	}
}
