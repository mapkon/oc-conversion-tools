package org.openxdata.oc.util

import org.openxdata.oc.model.BindSet
import groovy.util.logging.Log

@Log
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
