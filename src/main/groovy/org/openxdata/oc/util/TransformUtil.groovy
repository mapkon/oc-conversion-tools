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

	public String getStudyName(def xformText) {
		
		if(xformText instanceof String) {
			def xml = new XmlSlurper().parseText(xformText)
			return xml.@name.text()
			
		}	
		else {
			return xformText.@name.text()
		}	
	}
}
