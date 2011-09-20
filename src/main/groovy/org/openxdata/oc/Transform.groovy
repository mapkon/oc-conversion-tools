package org.openxdata.oc

import org.junit.Test
import org.openxdata.oc.convert.XSLTCompiler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xml.internal.security.Init;

import groovy.inspect.TextNode;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil;

class Transform {

	public String transformODM(def odm){
		
		Init.init();
		
		def xslt = loadFile("/org/openxdata/oc/transform-v0.1.xsl");
		def compiler = new XSLTCompiler(xslt);
		def doc = new XmlParser().parseText(compiler.transform(odm));

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
		def resource = new File(getClass().getResource(file).getFile()).text
		return resource
	}
}