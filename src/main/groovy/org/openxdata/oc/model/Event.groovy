package org.openxdata.oc.model

class Event {

	def ordinal
	def formOID
	def eventDefinitionOID

	def eventXml

	def Event(def eventXml) {

		ordinal = eventXml.ordinal
		formOID = eventXml.formOID
		eventDefinitionOID = eventXml.eventDefinitionOID

		this.eventXml = eventXml
	}

	def getSubjectKeys() {

		def subjectKeys = []

		def keys = eventXml.studySubjectOIDs.toString()

		subjectKeys = keys.split()

		return subjectKeys
	}
}
