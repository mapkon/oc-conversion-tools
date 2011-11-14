package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class ParseExceptionTest {

	@Test void testImportExceptionThrowsExceptionOnOKErrorCode(){
		def ex = new ParseException(ErrorCode.OK)
		try{
			ex.errorMessage()
		}catch(def exception){
			assertEquals 'Should never branch to this case.', exception.getMessage()
		}
	}

	@Test void testImportExceptionRendersCorrectXMLParseErrorMessage(){

		def ex = new ParseException(ErrorCode.XML_PARSE_EXCEPTION)

		def actual = 'The returned XML might be malformed.'
		assertEquals actual, ex.errorMessage()
	}
}
