package org.openxdata.oc.model

import groovy.inspect.TextNode
import groovy.util.logging.Log
import groovy.util.slurpersupport.Node
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil
import groovy.xml.streamingmarkupsupport.StreamingMarkupWriter

@Log
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
	
	private replaceHintNodeText(def hintNode) {
		def text = hintNode.text()
		text = text.replace("<SUP>", "^")
		text = text.replace("</SUP>", "")

		hintNode.replaceBody(text)
		
		return text
	}
	
	private def getSubjectKeyGroupNode(){
		return getNodeList('group').findAll{it.name() == 'group' && it.@id == '1'}
	}
	
	private appendSubjectKeySelectNodes(def subjectKeys) {

		log.info("Appending " + subjectKeys.size() + " Subject Keys into converted Study: " + convertedXformXml.@name +" by adding <select1> nodes.")
		def subjectGroup = getSubjectKeyGroupNode()

		def xml = new StreamingMarkupBuilder().bind {
			mkp.xmlDeclaration()
			mkp.declareNamespace(xf:'xmlns:xf="http://www.w3.org/2002/xforms')
			xf.select1(bind:'subjectKeyBind'){
				subjectKeys.each{ nodeValue ->
					xf.item(id:nodeValue){
						xf.label(nodeValue)
						xf.value(nodeValue)
					}
				}
			}
		}

		def newGroupNode = new XmlSlurper().parseText(xml.toString())
		subjectGroup.each { it.appendNode(newGroupNode) }
	}
	
	private appendSubjectKeyInputNode() {
		log.info("No Subjects Attached to the Study: " + convertedXformXml.@name + "." + " Adding Input Node to the Form.")
		
		def subjectGroup = getSubjectKeyGroupNode()
		def xml = new StreamingMarkupBuilder().bind {
			mkp.xmlDeclaration()
			mkp.declareNamespace(xf:'xmlns:xf="http://www.w3.org/2002/xforms')
			xf.input(bind:'subjectKeyBind')
		}

		def newGroupNode = new XmlSlurper().parseText(xml.toString())
		subjectGroup.each { it.appendNode(newGroupNode) }
	}
	
	def appendSubjectKeyNode(def subjectKeys){

		log.info("Processing subjects to determine whether to append <input> node or <select1> node.")
		if(subjectKeys.size() > 0){
			appendSubjectKeySelectNodes(subjectKeys)
		}
		else{
			appendSubjectKeyInputNode()
		}

		log.info("Done processing subjects.")
	}
	
	def parseMeasurementUnits(){
		log.info("Parsing Measurement units.")

		def parsedMeasurementUnits = [:]
		def hintNodes = getNodeList("hint")
		
		hintNodes.each {
			
			def originalText = it.text()
			def parsedText = replaceHintNodeText(it)
			
			parsedMeasurementUnits[originalText] = parsedText
		}
		
		log.info("Parsing Measurement Units successful.")
		return parsedMeasurementUnits
	}

	def serializeXformNode(){
		log.info("Transforming the xform tag to string as required by openxdata.")

		def xformNodeText
		convertedXformXml.form.version.xform.each {
			it.children().each { xformNodeText += XmlUtil.asString(it) }
			def textNode = new TextNode("""<?xml version="1.0" encoding="UTF-8"?>"""+ xformNodeText)
			it.replaceBody(textNode)
		}
		
		log.info("Transforming Xform tag to String successful.")
		return xformNodeText
	}

	def getFormVersion(def form){
		return form.children()
	}
	
	def getNodeList(def tagName){
		return convertedXformXml.breadthFirst().findAll{it.name().equals(tagName)}
	}
}
