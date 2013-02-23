package org.openxdata.oc.util

import javax.servlet.ServletContext;
import groovy.util.logging.Log

@Log
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
	
	Properties loadOpenClinicaProperties(ServletContext servletContext) {
		
		InputStream propFileStream = servletContext?.getResourceAsStream("openclinica.properties");
		if(propFileStream){
			log.info("loading properties file from Web Context...")
			props.load(propFileStream)
		}else{
			// We assume the absence of an external properties file in openxdata/WEB-INF
			log.info("loading properties file from classpath...")
			props = loadProperties('META-INF/openclinica.properties')
		}
		
		return props
	}
}
