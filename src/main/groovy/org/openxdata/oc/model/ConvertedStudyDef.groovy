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
		this.forms = convertedXformXml.form
	}
	
	def getSubjectKeyGroupNode(){
		return convertedXformXml.depthFirst().find{it.'@id'.equals('1')}
	}
	
	def parseMeasurementUnits(){
		log.info("Parsing Measurement units.")

		def parsedMeasurementUnits = [:]
		def hintNodes = convertedXformXml.depthFirst().findAll{it.name().equals('hint')}
		
		hintNodes.each {
			
			def originalText = it.text()
			def parsedText = replaceHintNodeText(it)
			
			parsedMeasurementUnits[originalText] = parsedText
		}
		
		log.info("Parsing Measurement Units successful.")
		return parsedMeasurementUnits
	}
	
	private replaceHintNodeText(def hintNode) {
		def text = hintNode.text()
		text = text.replace("<SUP>", "^")
		text = text.replace("</SUP>", "")

		hintNode.replaceBody(text)

		return text
	}

	def serializeXformNode(){
		log.info("Transforming the xform tag to string as required by openxdata.")

		def xformNodeText
		convertedXformXml.form.version.xform.each {
			it.children().each { xformNodeText += XmlUtil.asString(it) }
			def textNode = new TextNode("""<?xml version="1.0" encoding="UTF-8"?>${xformNodeText}""")
			it.replaceBody(textNode)
		}
		
		log.info("Transforming Xform tag to String successful.")
		return xformNodeText
	}

	def getFormVersion(def form){
		return form.children()
	}
}
