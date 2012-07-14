package org.openxdata.oc.util

import groovy.util.logging.Log

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
}
