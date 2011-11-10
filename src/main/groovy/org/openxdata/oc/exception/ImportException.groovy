package org.openxdata.oc.exception

public class ImportException extends Exception {

	def ImportException(def message){
		super(message)
	}

	def errorCode
	def ImportException(ErrorCode errorCode){
		this.errorCode = errorCode
	}

	def errorMessage(){
		switch(errorCode){
			case ErrorCode.OK:
				throw new Exception('Should never branch to this case.')
			case ErrorCode.XML_PARSE_EXCEPTION:
				return 'The returned XML might be malformed.'
			case ErrorCode.EMPTY_INSTANCE_DATA:
				return 'Cannot process empty instance data.'
			case ErrorCode.IMPORT_ERROR:
				return 'The Import to OpenClinica didnot complete successfully. Check logs for more information.'
		}
	}
}
