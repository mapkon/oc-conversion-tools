package org.openxdata.oc

import groovy.inspect.TextNode
import groovy.util.logging.Log
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import org.openxdata.oc.exception.MalformedStudyDefinitionException

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
		
		def xslt = loadFile("transform-v0.1.xsl");
		
		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")
		def doc = new XmlParser().parseText(xml)
		
		// Add subjects keys 
		injectSubjectKeys(doc, subjectKeys)
		
		// parse measurement unit special tags
		parseMeasurementUnits(doc)
		
		// making the xform into a string
		serialiseXform(doc)

		log.info("<< Successfully transformed file. >>")
		return XmlUtil.asString(doc);
	}
	
	private def injectSubjectKeys(def doc, def subjectKeys) {
		log.info("Injecting " + subjectKeys.size() + " Subject Keys into converted Study " + doc.@name +".")

		def subjectKeyGroups = doc.breadthFirst().group.findAll {it.@id.equals('1')}
		if (subjectKeyGroups != null){
			subjectKeyGroups.each {
				it.select1.each { selectGroup ->
					subjectKeys.each { key ->
						Node itemNode = new Node(selectGroup, "item", [id:key])
						addItemElements(itemNode, key)
					}
				}
			}
		}
		else{
			throw new MalformedStudyDefinitionException("Study does not have questions defined for it.")
		}
		
		log.info("Injecting subjects successful.")
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