package org.openxdata.oc.util

public class TransformUtil {

	public def loadFileContents(def fileName){
		if(fileName){
			log.info("==== Loading Transform file: "+fileName+" ====")
			def stream = getClass().getResourceAsStream("${fileName}")
			log.info("==== Loaded Transform file: "+fileName +" =====")
			return stream.text
		}else{
			throw new IllegalArgumentException('File name cannot be null or empty.')
		}
	}

}
