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
	
	def initOID(){
		this.OID = parsedOdmXml.Study.@OID.text()
	}
	
	def initName(){
		this.name = parsedOdmXml.Study.GlobalVariables.StudyName.text()
	}
	
	def initDescription(){
		this.description = parsedOdmXml.Study.GlobalVariables.StudyDescription.text().trim()
	}
	
	def initStudyEventDefs(){
		this.studyEventDefs = parsedOdmXml.Study.MetaDataVersion.StudyEventDef
	}
}
