package org.openxdata.oc.transport.soap.proxy

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.soap.proxy.EventWebServiceProxy;


@WithGMock
class EventWebServiceProxyTest extends GroovyTestCase {


	def eventWebServiceProxy

	@Before public void setUp() {

		def factory = setUpConnectionFactoryMock()
		eventWebServiceProxy = new EventWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:factory)
	}

	@Test void testEventWebServiceHasCorrectEndPoint() {

		def envelope = eventWebServiceProxy.getSoapEnvelope()

		def envelopeXml = new XmlSlurper().parseText(envelope)

		def actual = 'http://openclinica.org/ws/event/v1'
		def namespaceList = envelopeXml.'**'.collect { it.namespaceURI() }.unique()

		assertEquals actual, namespaceList[2].toString()
	}

	@Test void testFindEventsByStudyOIDDoesNotReturnNullGivenValidStudyOID() {

		play {
			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			assertNotNull "The Response from the events web service cannot be null", response
		}
	}

	@Test void testFindEventByStudyOIDReturnsResponseHavingEventsNode() {

		play {

			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			assertEquals "Root should be events", "events", response.name().localPart
		}
	}

	@Test void testFindEventByStudyOIDReturnsCorrectNumberOfEventsAttachedToStudy() {
		play {

			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			assertEquals "The events should 71", 71, response.children().size()
		}
	}

	@Test void testFindEventsByStudyOIDReturnsEventsWithEventOID() {
		play {

			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			def events = response.children()

			events.each { assertNotNull it.eventDefinitionOID }
		}
	}

	@Test void testFindEventsByStudyOIDReturnsEventsWithFormOID() {
		play {

			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			def events = response.children()

			events.each { assertNotNull it.formOID }
		}
	}
	
	@Test void testFindEventsByStudyOIDReturnsEventsWithSubjectOID() {
		play {

			def response = eventWebServiceProxy.findEventsByStudyOID('OID')
			def events = response.children()

			events.each { assertNotNull it.studySubjectOIDs }
		}
	}

	private def setUpConnectionFactoryMock() {

		def connection = mock(HttpURLConnection.class)
		connection.setRequestMethod('POST')
		connection.setRequestProperty('Content-Type', 'text/xml')
		connection.setRequestProperty('Content-Length', is(instanceOf(String.class)))
		connection.setDoOutput(true)
		connection.getURL().returns('mock url')

		def outputStream = new ByteArrayOutputStream()
		connection.getOutputStream().returns(outputStream)
		connection.getInputStream().returns(new ByteArrayInputStream(TestData.eventProxyResponse.getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getEventConnection().returns(connection)

		return factory
	}
}
