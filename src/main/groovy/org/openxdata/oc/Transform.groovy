package org.openxdata.oc

// require(url='http://xml.apache.org/xalan-j/', jar='serializer.jar')
// require(url='http://xml.apache.org/xalan-j/', jar='xalan_270.jar')
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Log
class Transform {

	public String transformODM(def odm){
		
		log.info("Starting transformation of file")
		
		def xslt = loadFile("/org/openxdata/oc/transform-v0.1.xsl");
		
		def factory = TransformerFactory.newInstance()
		def transformer = factory.newTransformer(new StreamSource(new StringReader(xslt)))
		def byteArray = new ByteArrayOutputStream()
		transformer.transform(new StreamSource(new StringReader(odm)), new StreamResult(byteArray))
		def xml = byteArray.toString("UTF-8")
		def doc = new XmlParser().parseText(xml)

		doc.form.version.xform.each {
			def s = ""
			it.children().each {s += XmlUtil.asString(it) }
			def text = new TextNode("""<?xml version="1.0" encoding="UTF-8"?>"""+s)
			it.remove(it.children())
			it.children().add(text)
		}

		return XmlUtil.asString(doc);
	}

	private String loadFile(def file) {
		def xslt = new File(getClass().getResource(file).getFile()).text
		return xslt
	}
}