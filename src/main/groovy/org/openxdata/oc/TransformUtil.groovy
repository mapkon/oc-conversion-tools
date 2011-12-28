package org.openxdata.oc

import org.openxdata.oc.model.BindSet


/**
 * Encapsulates utilities needed by the Transformation class to perform its duties well. 
 * The reason of separating them into a different class is to avoid the trap of having big classes by separating concerns.
 *
 */
public class TransformUtil {

	public def loadFileContents(def fileName){
		if(fileName){
			def stream = getClass().getResourceAsStream(fileName)
			return stream.text
		}else{
			throw new IllegalArgumentException('File name cannot be null or empty.')
		}
	}

	public def hasDuplicateBindings(def docWithDuplicateBindings) {

		if (getDuplicateBindings(docWithDuplicateBindings).isEmpty())
			return false

		return true
	}

	public def getDuplicateBindings(def docWithDuplicateBindings) {

		def bindings = docWithDuplicateBindings.breadthFirst().bind.findAll{it.@id}.'@id'
		def set = new BindSet<String>()
		
		bindings.each {
			set.add(it)
		}

		return set.duplicatedBindings
	}
}
