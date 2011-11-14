package org.openxdata.oc.exception;

import static org.junit.Assert.*

import org.junit.Test

class ImportExceptionTest {

	@Test void testImportExceptionThrowsExceptionOnOKErrorCode(){
		def ex = new ImportException(ErrorCode.OK)
		try{
			ex.errorMessage()
		}catch(def exception){
			assertEquals 'Should never branch to this case.', exception.getMessage()
		}
	}

	@Test void testImportExceptionRendersCorrectEmptyInstanceErrorMessage(){

		def ex = new ImportException(ErrorCode.EMPTY_INSTANCE_DATA)

		def actual = 'Cannot process empty instance data.'
		assertEquals actual, ex.errorMessage()
	}

	@Test void testImportExceptionRendersCorrectImportError(){

		def ex = new ImportException(ErrorCode.IMPORT_ERROR)

		def actual = 'The Import to OpenClinica didnot complete successfully. Check logs for more information.'
		assertEquals actual, ex.errorMessage()
	}
}
