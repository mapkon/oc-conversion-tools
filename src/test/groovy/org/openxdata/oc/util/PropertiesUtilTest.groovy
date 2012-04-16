package org.openxdata.oc.util

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class PropertiesUtilTest extends GroovyTestCase {

	def props
	def util = new PropertiesUtil()

	@Before void setUp() {
		props = util.loadProperties('META-INF/openclinica.properties')
	}

	@Test void testLoadPropertiesDoesNotReturnNull() {

		assertNotNull props
	}

	@Test void testLoadPropertiesThrowsExceptionOnNullFileName() {
		shouldFail(IllegalArgumentException){
			def util = new PropertiesUtil()
			util.loadProperties(null)
		}
	}

	@Test void testLoadPropertiesThrowsExceptionOnNullFileNameWithCorrectMessage() {
		try {
			def util = new PropertiesUtil()
			util.loadProperties(null)
		}catch(def ex) {
			assertEquals 'File Name cannot be Null or Empty!', ex.getMessage()
		}
	}

	@Test void testLoadPropertiesReturnsHostProperty() {

		def host = props.getAt('host')
		assertNotNull host
	}

	@Test void testLoadPropertiesReturnsValidHostProperty() {

		def host = props.getAt('host')
		assertEquals 'http://10.10.3.217:8080/OpenClinica-ws-SNAPSHOT', host
	}

	@Test void testLoadPropertiesReturnsUserNameProperty() {

		def username = props.getAt('username')
		assertNotNull username
	}

	@Test void testLoadPropertiesReturnsValidUserNameProperty() {

		def username = props.getAt('username')
		assertEquals 'MarkG', username
	}

	@Test void testLoadPropertiesReturnsPasswordProperty() {

		def password = props.getAt('password')
		assertNotNull password
	}

	@Test void testLoadPropertiesReturnsValidPasswordProperty() {

		def password = props.getAt('password')
		assertEquals 'b9a60a9d91a96ee522d0c942e5b88dfba25b0a12', password
	}

	@Test void testGetOCPropertyReturnsHostProperty() {
		def host = util.getOCProperty('host')
		assertNotNull host
	}

	@Test void testGetOCPropertyReturnsValidHost() {
		def host = util.getOCProperty('host')
		assertNotNull 'http://10.10.3.217:8080/OpenClinica-ws-SNAPSHOT', host
	}

	@Test void testGetOCPropertyReturnsUserNameProperty() {
		def username = util.getOCProperty('username')
		assertNotNull username
	}

	@Test void testGetOCPropertyReturnsValidUsername() {
		def username = util.getOCProperty('username')
		assertNotNull 'MarkG', username
	}

	@Test void testGetOCPropertyReturnsPasswordProperty() {
		def username = util.getOCProperty('username')
		assertNotNull username
	}

	@Test void testGetOCPropertyReturnsValidPassword() {
		def password = util.getOCProperty('password')
		assertNotNull 'password', password
	}
}
