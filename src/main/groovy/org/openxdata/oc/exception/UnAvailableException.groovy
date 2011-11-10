package org.openxdata.oc.exception

class UnAvailableException extends Exception {
	
	def UnAvailableException(def message){
		super(message)
	}
	
	def errorCode
	def UnAvailableException(ErrorCode errorCode){
		this.errorCode = errorCode
	}
	
	def errorMessage(){
		switch(errorCode){
			case ErrorCode.OK:
				throw new Exception('Should never branch to this case.')
			case ErrorCode.SERVER_UNAVAILABLE:
				return 'The server might be unreacheable. Check that OpenClinica Web Services is running.'
		}
	}
}
