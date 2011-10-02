package org.openxdata.oc.exception

class UnAvailableException extends Exception {
	
	def UnAvailableException(def message){
		super(message)
	}
	
	def UnAvailableException(def message, def exception){
		super(message, exception)
	}
}
