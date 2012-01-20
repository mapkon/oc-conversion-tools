package org.openxdata.oc

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

import org.openxdata.oc.exception.ErrorCode
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.model.ConvertedStudyDef
import org.openxdata.oc.util.TransformUtil;


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

		log.info("Starting transformation of file...")

		try{
			return transformODMToXform(odm)
		}catch(def ex){
			log.info("Incomplete Transformation due to: ${ex.getMessage()}")
			throw new ImportException(ErrorCode.XML_PARSE_EXCEPTION)
		}
	}

	private def transformODMToXform(odm) {
		
		def xslt = util.loadFileContents("/org/openxdata/oc/transform-v0.1.xsl")

		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")

		def doc = new XmlSlurper().parseText(xml)

		def convertedStudyDef = new ConvertedStudyDef(doc)
		
		return convertedStudyDef
	}
}