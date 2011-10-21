package org.openxdata.oc

import groovy.inspect.TextNode
import groovy.util.logging.Log
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Log
public class Transform {
	
	private static Transform INSTANCE = new Transform()
	
	private Transform() {}
	
	/**
	 * Returns an instance of the transformer.
	 * @return transformer.
	 */
	public static Transform getTransformer() {
		return INSTANCE
	}

	/**
	 * Transforms an ODM file to openxdata xform.
	 * 
	 * @param odm ODM file to convert.
	 * @param subjectKeys Subject Keys to insert into form.
	 * 
	 * @return Valid OpenXdata form.
	 */
	String transformODM(def odm, def subjectKeys){
		
		log.info("Starting transformation of file...")
		
		def xslt = loadFile("transform-v0.1.xsl")
		
		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")
		
		def doc = new XmlParser().parseText(xml)
				
		// Add subjects keys 
		insertSubjectKeys(doc, subjectKeys)
		
		// parse measurement unit special tags
		parseMeasurementUnits(doc)
		
		// making the xform into a string
		serialiseXform(doc)

		log.info("<< Successfully transformed file. >>")
		return XmlUtil.asString(doc)
	}
	
	private def insertSubjectKeys(def doc, def subjectKeys) {

		def subjectKeyGroup = doc.breadthFirst().group.findAll {it.@id.equals('1')}
		if(subjectKeys.size > 0){
			log.info("Inserting " + subjectKeys.size() + " Subject Keys into converted Study: " + doc.@name +".")
			subjectKeyGroup.each {
				def selectNode = new Node(it, "select1", [bind:"subjectKeyBind"])
				subjectKeys.each { key ->
					Node itemNode = new Node(selectNode, "item", [id:key])
					addItemElements(itemNode, key)
				}
			}
		}
		else{
			log.info("No Subjects Attached to the Study: " + doc.@name + "." + " Adding Input Node to the Form.")
			subjectKeyGroup.each {
				def inputNode = new Node(it, "input", [bind:"subjectKeyBind"])
			}
		}

		log.info("Done processing subjects.")
	}
	
	private def addItemElements(def node, def value){
		new Node(node, "label", value)
		new Node (node, "value", value)
	}

	private parseMeasurementUnits(Node doc) {
		
		log.info("Parsing Measurement units.")
		
		doc.breadthFirst().hint.each {
			def text = it.text()
			text = text.replace("<SUP>", "^")
			text = text.replace("</SUP>", "")
			def parent = it.parent()
			parent.remove(it)
			new Node(parent, "hint", text)
		}
		
		log.info("Parsing Measurement Units successful.")
	}
	
	private serialiseXform(Node doc) {
		
		log.info("Transforming the xform tag to string as required by openxdata.")
		doc.form.version.xform.each {
			def s = ""
			it.children().each {s += XmlUtil.asString(it) }
			def text = new TextNode("""<?xml version="1.0" encoding="UTF-8"?>"""+s)
			it.remove(it.children())
			it.children().add(text)
		}
		
		log.info("Transforming Xform tag to String successful.")
	}

	private String loadFile(def file) {
		def lines = getClass().getResourceAsStream(file).readLines()
		def stringBuilder = new StringBuilder()
		lines.each {
			stringBuilder.append(it);
		}
		return stringBuilder.toString()
	}
}