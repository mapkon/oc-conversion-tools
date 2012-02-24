package org.openxdata.oc.util


class PropertiesUtil {

	def props = new Properties()
	
	Properties loadProperties(def fileName) {
		if(fileName){
			
			def stream = this.getClass().getClassLoader().getResourceAsStream(fileName)
			props.load(stream)
			return props
		}else {
			throw new IllegalArgumentException('File Name cannot be Null or Empty!')
		}
	}
	
	def getOCProperty(def propName) {
		return props.getProperty(propName)
	}
}
