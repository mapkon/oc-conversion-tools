package org.openxdata.oc.util

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.gmock.WithGMock
import javax.servlet.ServletContext;

@WithGMock
class PropertiesUtilTest extends GroovyTestCase {

	def props
	def util = new PropertiesUtil()
	ServletContext servletContext
	def servletCtxInputStream

	@Before void setUp() {
		props = util.loadProperties('META-INF/openclinica.properties')
		servletCtxInputStream = this.getClass().getClassLoader().getResourceAsStream('META-INF/openclinica.properties')
		servletContext = mock(ServletContext)
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
		def msg = shouldFail(IllegalArgumentException) {
			util.loadProperties('')
		}
		
		assertEquals 'File Name cannot be Null or Empty!', msg
	}

	@Test void testLoadPropertiesReturnsHostProperty() {

		def host = props.getAt('host')
		assertNotNull host
	}

	@Test void testLoadPropertiesReturnsValidURL() {

		def host = props.host
		assertTrue host.matches(/^http(?:s)?:\/{2}[-.\dA-z]+\/[-\w\/]+/)
	}

	@Test void testLoadPropertiesReturnsValidHostProperty() {

		def host = props.getAt('host')
		assertEquals 'http://158.37.6.164/OpenClinica-ws-SNAPSHOT', host
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
		assertNotNull 'http://158.37.6.165/OpenClinica-ws-SNAPSHOT', host
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

	@Test void testOCPropertyFileIsLoadedFromWebAppFolderIfItsPresent(){
		servletContext.getResourceAsStream('openclinica.properties').returns(servletCtxInputStream)
		play{
			checkOpenlincaPropertiesNotEmpty(servletContext)
		}

	}

	@Test void testOCPropertyFileIsLoadedFromClasspathIfItsNotPresentInWebAppFolder(){
		servletContext.getResourceAsStream('openclinica.properties').returns(null)
		play{
			checkOpenlincaPropertiesNotEmpty(servletContext)
		}
	}

	@Test void testOCPropertyFileIsLoadedFromClasspathIfServletContextIsNull(){
		checkOpenlincaPropertiesNotEmpty(null)
	}

	@Test void checkOpenlincaPropertiesNotEmpty(def servletContext){
		
		util = new PropertiesUtil()
		def returnedProps = util.loadOpenClinicaProperties(servletContext)
		assertFalse 'Properties should not be empty', returnedProps.isEmpty()
	}
}
