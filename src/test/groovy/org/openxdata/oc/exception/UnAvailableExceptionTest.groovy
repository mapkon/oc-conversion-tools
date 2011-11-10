package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class UnAvailableExceptionTest {

	@Test void testUnAvailableExceptionRendersCorrectMessage(){

		def ex = new UnAvailableException(ErrorCode.OK)

		try{
			ex.errorMessage()
		}catch(def exception){
			assertEquals 'Should never branch to this case.', exception.getMessage()
		}
	}

	@Test void testUnAvailableExceptionRendersCorrectErrorMessage(){

		def ex = new UnAvailableException(ErrorCode.SERVER_UNAVAILABLE)
		def actual = 'The server might be unreacheable. Check that OpenClinica Web Services is running.'
		
		assertEquals actual, ex.errorMessage()
	}
}
