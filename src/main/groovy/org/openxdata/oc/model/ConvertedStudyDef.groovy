package org.openxdata.oc.model

class ConvertedStudyDef {
	
	def id
	def name
	def description
	
	def forms
	def bindings
	
	def parsedXml
	
	public def initializeProperties(def xmlStream){
		this.parsedXml = new XmlSlurper().parseText(xmlStream)
		
		initId()
		initName()
		initDescription()
		
		initForms()
		//initBindings()
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
	
	def initBindings(){
		def xforms = parsedXml.form.version.xform.text()
		def xformsText = xforms.replaceFirst('''<?xml version="1.0" encoding="UTF-8" standalone="no"?>''', "")
		println xformsText
		def xformNode = new XmlSlurper().parseText(xformsText).declareNamespace(ns:'http://www.w3.org/2002/xforms')
		
		this.bindings = parsedXml.form.version.xform.collect{it}
	}
	
	public def getFormVersion(def form){
		return form.children()
	}
}
