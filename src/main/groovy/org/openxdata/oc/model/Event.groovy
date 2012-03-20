package org.openxdata.oc.model


class Event {

	private def eventXml
	
	private def ordinal
	private def eventName
	private def eventDefinitionOID

	def Event(def eventXml) {

		eventName = eventXml.eventName.text()
		ordinal = eventXml.ordinal.text()
		eventDefinitionOID = eventXml.eventDefinitionOID.text()

		this.eventXml = eventXml
	}
	
	def getFormOIDs() {
		
		def formOIDs = []
		
		def oids = eventXml.formOID.text()
		
		formOIDs = oids.split()
		
		return formOIDs
	}
}
