package org.openxdata.oc

import groovy.inspect.TextNode
import groovy.util.logging.Log
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.openxdata.oc.model.ConvertedStudyDef


@Log
public class Transform {
	
	def util
	private static def INSTANCE = new Transform()
	
	private Transform() {
		util = new TransformUtil()
	}
	
	/**
	 * Returns an instance of the transformer.
	 * @return transformer.
	 */
	public static Transform getTransformer() {
		return INSTANCE
	}

	/**
	 * Converts an ODM file to openxdata xform.
	 * 
	 * @param odm ODM file to convert.
	 * @param subjectKeys Subject Keys to insert into form.
	 * 
	 * @return Valid OpenXdata form.
	 */
	def ConvertODMToXform(def odm){
		
		log.info("Starting transformation of file...")
		
		def xslt = util.loadFileContents("transform-v0.1.xsl")
		
		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")
		
		def doc = new XmlSlurper().parseText(xml)
			
		def convertedStudyDef = new ConvertedStudyDef(doc)
		
		log.info("<< Successfully transformed file. >>")
		
		return convertedStudyDef
	}
}