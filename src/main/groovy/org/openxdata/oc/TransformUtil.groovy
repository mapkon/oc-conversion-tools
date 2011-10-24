package org.openxdata.oc

import org.codehaus.groovy.reflection.ReflectionUtils


/**
 * Encapsulates utilities needed by the Transformation class to perform its duties well. 
 * The reason of separating them into a different class is to avoid the trap of having big classes and also to separate concerns.
 *
 */
public class TransformUtil {

	public def loadFile(def fileName){
		if(fileName){
			return new File(getClass().getResource(fileName).getFile())
		}
		else{
			throw new IllegalArgumentException("File Name cannot null or empty.")
		}
	}

	public def loadFileContents(def fileName){
		
		def file = loadFile(fileName)

		def builder = new StringBuilder()
		file.eachLine{ builder.append(it) }

		return builder.toString()
	}
}
