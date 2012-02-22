package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class UnAvailableExceptionTest {

	@Test void testUnAvailableExceptionRendersCorrectErrorMessage(){

		def ex = new UnAvailableException("The server might be unreacheable. Check that OpenClinica Web Services is running.")
		def actual = 'The server might be unreacheable. Check that OpenClinica Web Services is running.'
		
		assertEquals actual, ex.getLocalizedMessage()
	}
}
