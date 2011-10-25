package org.openxdata.oc



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

	public def hasDuplicateBindings(def doc) {

		def bindings = doc.breadthFirst().bind.findAll{it.'@id'}.'@id'
		if (getSimilarBindings(bindings).isEmpty())
			return false

		return true
	}

	public def getSimilarBindings(def bindings) {

		final def duplicatedBindings = new ArrayList<String>()
		def set = new HashSet<String>() {
			@Override
			public boolean add(String e) {
				if (contains(e)) {
					duplicatedBindings.add(e)
				}
				return super.add(e)
			}
		}
		
		for (String bind : bindings) {
			set.add(bind)
		}

		println duplicatedBindings.size()
		return duplicatedBindings
	}
}
