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
	
	private appendSubjectKeySelectNodes(def subjectKeys) {

		log.info("Inserting " + subjectKeys.size() + " Subject Keys into converted Study: " + convertedXformXml.@name +".")
		def subjectGroup = convertedXformXml.'**'.findAll{it.name() == 'group' && it.@id == '1'}

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
	
	def appendSubjectKeyNode(def subjectKeys){

		if(subjectKeys.size() > 0){
			appendSubjectKeySelectNodes(subjectKeys)
		}
		else{
			log.info("No Subjects Attached to the Study: " + convertedXformXml.@name + "." + " Adding Input Node to the Form.")
			subjectKeyGroup.each {
				def inputNode = new Node(it, "input", [bind:"subjectKeyBind"])
			}
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
