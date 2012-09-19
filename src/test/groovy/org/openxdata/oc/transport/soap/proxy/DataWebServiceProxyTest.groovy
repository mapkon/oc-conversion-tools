package org.openxdata.oc.transport.soap.proxy

import static org.hamcrest.Matchers.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.model.OpenClinicaUser
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class DataWebServiceProxyTest extends GroovyTestCase {

	def dataProxy

	@Before public void setUp() {
	
		def connectionFactory = setUpConnectionFactoryMock(TestData.userDetailsResponse)
		dataProxy = new DataWebServiceProxy(connectionFactory:connectionFactory)
	}
	
	@Test void testThatDataWebServiceHasCorrectPath(){

		def xml = new XmlSlurper().parseText(dataProxy.getSoapEnvelope())

		def actual = 'http://openclinica.org/ws/data/v1'
		def namespaceList = xml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}

	@Test void testGetUserDetailsShouldFailOnEmptyUsernameParameter() {

		shouldFail(ImportException) {
			def user = dataProxy.getUserDetails("")
		}
	}
	
	@Test void testGetUserDetailsShouldNotReturnNullOnValidUsername(){
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertNotNull "User Details Response should not be null", user
		}
	}
	
	@Test void testThatGetUserDetailsReturnsOpenClinicaUserObject(){
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertEquals "Response should be OpenClinicaUser object", OpenClinicaUser.class, user.class
		}
	}
	
	@Test void testThatGetUserDetailsReturnsUserWithUsername(){
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertNotNull "Username cannot be null", user.username
		}
	}
	
	@Test void testThatGetUserDetailsReturnsUserWithCorrectUsername() {
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertEquals "Username should be foo", "foo", user.username
		}
	}
	
	@Test void testThatGetUserDetailsReturnsUserWithHashedPassword() {
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertNotNull "Hashed Password cannot be null", user.hashedPassword
		}
	}
	
	@Test void testThatGetUserDetailsReturnsUserWithCorrectHashedPassword() {
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertEquals "Created User should have correct hashed password", "hash LoL", user.hashedPassword
		}
	}
	
	@Test void testThatGetUserDetailsReturnsUserWhoCannotAccessWebServices() {
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertFalse "User is not authorized to use web services", user.canUseWebServices
		}
	}
	@Test void testThatGetUserDetailsReturnsWithWithAccessToTwoStudies() {
		
		play {
			
			def user = dataProxy.getUserDetails("username")
			
			assertEquals "User should have access to two studies", 2, user.getAllowedStudies().size()
		}
	}

	private def setUpConnectionFactoryMock(returnXml) {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod("POST").atMostOnce()
		connection.setRequestProperty("Content-Type", "text/xml").atMostOnce()
		connection.setRequestProperty("Content-Length", is(instanceOf(String.class))).atMostOnce()
		connection.setDoOutput(true).atMostOnce()
		connection.getURL().returns("mock url").atMostOnce()

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream).atMostOnce()
		connection.getInputStream().returns(new ByteArrayInputStream(returnXml.getBytes())).atMostOnce()

		def factory = mock(ConnectionFactory.class)
		factory.getStudyConnection().returns(connection).atMostOnce()

		return factory
	}
}
