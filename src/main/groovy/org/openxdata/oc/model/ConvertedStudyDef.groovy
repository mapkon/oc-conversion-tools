package org.openxdata.oc.model

class ConvertedStudyDef {
	
	def id
	def name
	def description
	def forms
	
	def convertedXformXml
	
	public def ConvertedStudyDef(def convertedXformXml){
		this.convertedXformXml = convertedXformXml
		
		initId()
		initName()
		initDescription()
		
		initForms()
	}
	
	def initId(){
		this.id = parsedXml.@studyKey.text()
	}
	
	def initName(){
		this.name = parsedXml.@name.text()
	}
	
	def initDescription(){
		this.description = parsedXml.@description.text()
	}
	
	def initForms(){
		this.forms = parsedXml.children()
	}
	
	public def getFormVersion(def form){
		return form.children()
	}
}
