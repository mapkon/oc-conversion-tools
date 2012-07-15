package org.openxdata.oc.util

import groovy.util.logging.Log
import groovy.xml.StreamingMarkupBuilder

@Log
public class TransformUtil {

	public String loadFileContents(def fileName) {

		if(fileName){
			log.info("Loading file: ${fileName}")
			def stream = this.getClass().getClassLoader().getResource(fileName)

			return stream.text
		}else{
			throw new IllegalArgumentException('File name cannot be null or empty.')
		}
	}

	public boolean isRepeat(def xml, def item) {

		if(item instanceof String) {

			def node = xml.children().find { it.name().is(item) }

			if(node != null && node.children() != null && node.children().size() > 0)
				return true
		}else {
			return item.children().size() > 0
		}
	}

	def isolateRepeats(input) {

		log.info("Checking xml for repeats")

		def updatedXml
		def xml = new XmlSlurper().parseText(input)

		xml.children().each {

			if(isRepeat("", it)) {

				log.info("Data contains repeats. Proceeding to isolate")

				def name = it.name()

				def repeats = xml.depthFirst().findAll{it.name().equals(name)}

				repeats.eachWithIndex {node, idx ->

					node.@repeatKey = "${idx}"

					// get the modified XML and check that it worked
					def outputBuilder = new StreamingMarkupBuilder()

					updatedXml = outputBuilder.bind{ mkp.yield xml }
				}
			}
		}

		return updatedXml.toString()
	}
}
