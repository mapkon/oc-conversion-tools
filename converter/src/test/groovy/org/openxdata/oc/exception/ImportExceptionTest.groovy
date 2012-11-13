package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class ImportExceptionTest {

	@Test void testImportExceptionRendersCorrectEmptyInstanceErrorMessage(){

		def ex = new ImportException('Cannot process empty instance data.')

		def actual = 'Cannot process empty instance data.'
		assertEquals actual, ex.getLocalizedMessage()
	}

	@Test void testImportExceptionRendersCorrectImportError(){

		def ex = new ImportException('The Import to OpenClinica didnot complete successfully. Check logs for more information.')

		def actual = 'The Import to OpenClinica didnot complete successfully. Check logs for more information.'
		assertEquals actual, ex.getLocalizedMessage()
	}
}
