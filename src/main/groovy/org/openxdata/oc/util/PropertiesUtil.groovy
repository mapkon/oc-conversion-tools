package org.openxdata.oc.util

class PropertiesUtil {

	def props = new Properties()
	
	def loadProperties(def fileName) {
		if(fileName){
			
			new File(fileName).withInputStream { stream ->
				props.load(stream)
			}
			
			return props
		}else {
			throw new IllegalArgumentException('Properties File Name cannot be Null or empty!')
		}
	}
	
	def getOCProperty(def propName) {
		return props.getAt(propName)
	}
}
