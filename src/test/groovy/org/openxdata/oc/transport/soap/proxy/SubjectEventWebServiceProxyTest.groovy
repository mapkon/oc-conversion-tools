package org.openxdata.oc.transport.soap.proxy

import static org.hamcrest.Matchers.*
import static org.junit.Assert.*

import org.gmock.WithGMock
import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.transport.factory.ConnectionFactory

@WithGMock
class SubjectEventWebServiceProxyTest {

	def subjectEventWebserviceProxy

	@Before public void setUp() {

		def factory = setUpConnectionFactoryMock()

		subjectEventWebserviceProxy = new SubjectEventWebServiceProxy(username:'uname', hashedPassword:'pass', connectionFactory:factory)
	}

	@Test void testSubjectEventWebServiceProxyHasCorrectEndPoint() {

		def envelope = subjectEventWebserviceProxy.getSoapEnvelope()

		def envelopeXml = new XmlSlurper().parseText(envelope)

		def namespaceList = envelopeXml.'**'.collect{ it.namespaceURI() }.unique()

		assertEquals 'http://openclinica.org/ws/event/v1', namespaceList[2].toString()
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestDoesNotReturnNullOnValidOID() {

		play {
			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			assertNotNull "Should never return null on valid studyOID", response
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithStudySubjectsNodes() {

		play {
			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			assertEquals "Root should be StudySubjects", "studySubjects", response.name().localPart
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithCorrectNumberOfStudySubjects() {

		play {
			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			assertEquals 76, response.children().size()
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectNodesHaveSubjectOID() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				assertNotNull "SubjectOID cannot be null or empty", it.studySubjectOID.text()
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectNodeHavingCorrectSubjectOID() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			assertEquals "SS_000A", response.children()[0].studySubjectOID.text()
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectNodeHavingCorrectSubjectOID2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			assertEquals "SS_20100200_897", response.children()[75].studySubjectOID.text()
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNode() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {
				assertEquals "Each subject must have events defined for them.", "events", it.children()[1].name().localPart
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithEventDefinitionOID() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {
				assertNotNull "Events must have eventDefinitionOID", it.children()[1].children()[0].text()
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectEventDefinitionOID() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def eventOID = response.children()[0].events.event.eventDefinitionOID.text()

			assertEquals "Must have correct Event OID", "SE_SC2", eventOID
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectEventDefinitionOID2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def eventOID = response.children()[75].events.event.eventDefinitionOID.text()

			assertEquals "Must have correct Event OID", "SE_SC1", eventOID
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventsHavingEventName() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				def eventName = it.events.event.eventName.text()

				assertNotNull "Must have eventName", eventName
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectEventName() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def eventName = response.children()[0].events.event.eventName.text()

			assertEquals "Must have correct Event Name", "SC2", eventName
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectEventName2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def eventName = response.children()[75].events.event.eventName.text()

			assertEquals "Must have correct Event Name", "SC1", eventName
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventsHavingOrdinal() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				def ordinal = it.events.event.ordinal.text()

				assertNotNull "Must have ordinal", ordinal
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectOrdinal() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def ordinal = response.children()[0].events.event.ordinal.text()

			assertEquals "Must have correct ordinal", "1", ordinal
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithCorrectOrdinal2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def ordinal = response.children()[75].events.event.ordinal.text()

			assertEquals "Must have correct Ordinal", "1", ordinal
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventsHavingStartDate() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				def startDate = it.events.event.startDate.text()

				assertNotNull "Must have startDate", startDate
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithStartDate() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def startDate = response.children()[0].events.event.startDate.text()

			assertEquals "Must have correct startDate", "2010-09-28 00:00", startDate
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectHavingEventsNodeWithStartDate2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def startDate = response.children()[75].events.event.startDate.text()

			assertEquals "Must have correct startDate", "2010-09-21 00:00", startDate
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventHavingFormOIDS() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				def formOIDs = it.events.event.formOIDs[0]

				assertEquals "Must have formOIDs", "formOIDs", formOIDs.name().localPart
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventHavingFormOIDSHavingOIDS() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			response.children().each {

				def formOIDs = it.events.event.formOIDs[0]

				assertTrue "Must have children in formOIDs", formOIDs.children().size() > 0
			}
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventHavingFormOIDSHavingCorrectNumberOfOIDS() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def formOIDs = response.children()[0].events.event.formOIDs[0]

			assertEquals "Must have correct number of OIDS", 1, formOIDs.children().size()
		}
	}

	@Test void testFindStudySubjectEventsByStudyOIDRequestReturnsResponseWithSubjectEventHavingFormOIDSHavingCorrectNumberOfOIDS2() {

		play {

			def response = subjectEventWebserviceProxy.findStudySubjectEventsByStudyOidRequest("oid")

			def formOIDs = response.children()[75].events.event.formOIDs[0]

			assertEquals "Must have correct number of OIDS", 3, formOIDs.children().size()
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
		connection.getInputStream().returns(new ByteArrayInputStream(TestData.getSubjectEvents().getBytes()))

		def factory = mock(ConnectionFactory.class)
		factory.getEventConnection().returns(connection)
		return factory
	}
}
