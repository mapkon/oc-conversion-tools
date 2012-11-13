package org.openxdata.oc.model

import org.junit.Before
import org.junit.Test
import org.openxdata.oc.data.TestData

class OpenClinicaUserTest extends GroovyTestCase {

	def user

	@Before public void setUp() {

		user = new OpenClinicaUser(TestData.getFindUserResponse())
	}

	@Test void testThatOpenClinicaUserHasUsername() {

		assertNotNull "OpenClinicaUser must have username", user.username
	}

	@Test void testThatCreatedOpenClinicaUserHasCorrectUsername() {

		assertEquals "Username should be foo ", "foo", user.username
	}

	@Test void testThatOpenClinicaUserHasHashedPassword() {

		assertNotNull "OpenClinicaUser must have hashed password", user.hashedPassword
	}

	@Test void testThatCreatedOpenClinicaUserHasCorrectHashedPassword() {

		assertEquals "Hased Password should be hash LoL", "hash LoL", user.hashedPassword
	}

	@Test void testThatOpenClinicaUserHasCanUserWebServicesProperty() {

		assertNotNull "OpenClinica must have canUseWebServices", user.canUseWebServices
	}

	@Test void testThatCreatedOpenClinicaUserHasCorrectCanUseWebServices() {

		assertFalse "This user cannot access web services", user.canUseWebServices
	}

	@Test void testThatCreateOpenClinicaUserHasAccessto2Studies() {

		assertEquals "User should have access to two studies", 2, user.getAllowedStudies().size()
	}
}
