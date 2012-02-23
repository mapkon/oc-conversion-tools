package org.openxdata.oc

import groovy.inspect.TextNode
import groovy.util.logging.Log
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.openxdata.oc.exception.TransformationException
import org.openxdata.oc.util.TransformUtil


@Log
public class Transform {
	
	def util
	private static def INSTANCE = new Transform()
	
	private Transform() {
		util = new TransformUtil()
	}
	
	public static Transform getTransformer() {
		return INSTANCE
	}

	def ConvertODMToXform(def odm){

		try{
			
			def xform = transformODMToXform(odm)
			
			return xform
			
		}catch(def ex){
		
			log.info(ex.getMessage())
			throw new TransformationException(ex.getMessage())
		}
	}

	private def transformODMToXform(odm) {
		
		log.info("Starting transformation of file...")
		
		def xslt = util.loadFileContents("META-INF/transform-v0.1.xsl")

		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")

		def doc = new XmlSlurper().parseText(xml)

		parseMeasurementUnits(doc)
		serializeXformNode(doc)
		
		log.info("Transformation complete. Returning...")
		
		return doc
	}
	
	def parseMeasurementUnits(def doc){
		log.info("Parsing Measurement units...")

		def parsedMeasurementUnits = [:]
		def hintNodes = doc.depthFirst().findAll{it.name().equals('hint')}
		
		hintNodes.each {
			
			def originalText = it.text()
			def parsedText = replaceHintNodeText(it)
			
			parsedMeasurementUnits[originalText] = parsedText
		}
	}
	
	private replaceHintNodeText(def hintNode) {
		def text = hintNode.text()
		text = text.replace("<SUP>", "^")
		text = text.replace("</SUP>", "")

		hintNode.replaceBody(text)

		return text
	}

	def serializeXformNode(def doc){
		log.info("Transforming the xform tag to string...")

		doc.form.version.xform.each { xform ->
			def xformText = ""
			xform.children().each { xformText += XmlUtil.asString(it) }
			def textNode = new TextNode("""${xformText}""")
			xform.replaceBody(textNode)
		}
	}
}