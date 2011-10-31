package org.openxdata.oc.model

class ODMDefinition {

	def OID
	def name
	def description
	def studyEventDefs
	
	def parsedOdmXml
	
	public def initializeProperties(def odmXml){
		parsedOdmXml = new XmlSlurper().parseText(odmXml)
		
		initOID()
		initName()
		initDescription()
		initStudyEventDefs()
	}
	
	private def initOID(){
		this.OID = parsedOdmXml.Study.@OID.text()
	}
	
	private def initName(){
		this.name = parsedOdmXml.Study.GlobalVariables.StudyName.text()
	}
	
	private def initDescription(){
		this.description = parsedOdmXml.Study.GlobalVariables.StudyDescription.text().trim()
	}
	
	private def initStudyEventDefs(){
		this.studyEventDefs = parsedOdmXml.Study.MetaDataVersion.StudyEventDef
	}
}
