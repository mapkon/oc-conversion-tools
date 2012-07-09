package org.openxdata.oc.model


class Event {

	private def eventXml
	
	def ordinal
	def endDate
	def startDate
	def eventName
	def repeating = false
	def eventDefinitionOID

	def Event(def eventXml) {

		ordinal = eventXml.ordinal.text()
		endDate = eventXml.endDate.text()
		eventName = eventXml.eventName.text()
		startDate = eventXml.startDate.text()
		eventDefinitionOID = eventXml.eventDefinitionOID.text()
		repeating = Boolean.parseBoolean(eventXml.repeating.text())
		
		this.eventXml = eventXml
	}
	
	List<String> getFormOIDs() {
		
		def formOIDs = []
		
		def oids = eventXml.formOIDs.formOID.each {
			formOIDs.add(it.text())
		}
		
		return formOIDs
	}
}
