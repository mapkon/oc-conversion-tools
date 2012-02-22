package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class TransformationExceptionTest {

	@Test void testImportExceptionRendersCorrectXMLParseErrorMessage(){

		def ex = new TransformationException('The returned XML might be malformed.')

		def actual = 'The returned XML might be malformed.'
		assertEquals actual, ex.getLocalizedMessage()
	}
}
