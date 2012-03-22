package org.openxdata.oc.model


class Event {

	private def eventXml
	
	private def ordinal
	private def eventName
	private def eventDefinitionOID
	
	private def startDate
	private def endDate

	def Event(def eventXml) {

		eventName = eventXml.eventName.text()
		ordinal = eventXml.ordinal.text()
		eventDefinitionOID = eventXml.eventDefinitionOID.text()
		startDate = eventXml.startDate.text()
		endDate = eventXml.endDate.text()

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
