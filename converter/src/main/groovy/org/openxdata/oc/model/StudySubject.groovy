package org.openxdata.oc.model

class StudySubject {

	private def subjectXml
	
	def subjectOID
	
	def StudySubject(def subjectXml) {
		
		this.subjectXml = subjectXml
		
		subjectOID = subjectXml.studySubjectOID.text()
		
	}
	
	List<Event> getEvents() {
		
		def subjectEvents = []
		
		def eventNode = subjectXml.events
		
		eventNode.event.each {
			
			def event = new Event(it)
			subjectEvents.add(event)
		}
		
		return subjectEvents
	}
}
