package org.openxdata.oc.util

public class TransformUtil {

	public def loadFileContents(def fileName){
		if(fileName){
			def stream = getClass().getResourceAsStream("../${fileName}")
			return stream.text
		}else{
			throw new IllegalArgumentException('File name cannot be null or empty.')
		}
	}

}
