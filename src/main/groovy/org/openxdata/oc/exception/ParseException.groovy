package org.openxdata.oc.exception

class ParseException extends Exception {

	def errorCode
	def ParseException(ErrorCode errorCode){
		this.errorCode = errorCode
	}

	def errorMessage(){
		switch(errorCode){
			case ErrorCode.OK:
				throw new Exception('Should never branch to this case.')
			case ErrorCode.XML_PARSE_EXCEPTION:
				return 'The returned XML might be malformed.'
		}
	}
}
