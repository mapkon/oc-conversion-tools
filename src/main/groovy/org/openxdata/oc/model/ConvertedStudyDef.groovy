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
	
	private def initId(){
		this.id = convertedXformXml.@studyKey.text()
	}
	
	private def initName(){
		this.name = convertedXformXml.@name.text()
	}
	
	private def initDescription(){
		this.description = convertedXformXml.@description.text()
	}
	
	private def initForms(){
		this.forms = convertedXformXml.children()
	}
	
	public def getFormVersion(def form){
		return form.children()
	}
}
