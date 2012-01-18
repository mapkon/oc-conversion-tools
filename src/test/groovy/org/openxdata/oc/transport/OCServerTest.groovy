package org.openxdata.oc.transport;

import java.util.List

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl


//@Ignore("Not intended to be run during standard test-build because it is dependent on an existing openclinica installation.")
public class OCServerTest extends GroovyTestCase {

	def client
	final def oid = 'S_DEFAULTS1'

	@Before public void setUp(){
		def factory = new ConnectionFactory()
		client = new OpenClinicaSoapClientImpl(factory)
	}

	@Test public void testListAllDoesNotReturnNull() {
		def studies = client.listAll()

		assertNotNull studies
	}

	@Test public void testListAllReturns1Study() {
		def studies = client.listAll()

		assertEquals 1, studies.size()
	}

	@Test public void testGetOpenXdataFormDoesNotReturnNull() {

		def convertedXform = client.getOpenxdataForm(oid)

		assertNotNull convertedXform
	}

	@Test public void testGetOpenXdataFormReturnsValidStudyRoot() {

		def convertedXform = client.getOpenxdataForm(oid)

		assertEquals 'study', convertedXform.getAt(0).name()

	}

	@Test public void testGetOpenXdataFormReturnsValidStudyWithFormElement() {

		def convertedXform = client.getOpenxdataForm(oid)

		def form = convertedXform.form[0]

		assertEquals 'form', form.name()

	}

	@Test public void testGetOpenXdataFormReturnsValidStudyWithVersionElement() {

		def convertedXform = client.getOpenxdataForm(oid)

		def version = convertedXform.form.version[0]

		assertEquals 'version', version.name()

	}

	@Test public void testGetOpenXdataFormReturnsValidStudyWithXformElement() {

		System.gc()
		def convertedXform = client.getOpenxdataForm(oid)

		def xform = convertedXform.form.version.xform[0]

		assertEquals 'xform', xform.name()

	}

	@Test public void testGetOpenXdataFormReturnsValidStudyWithXformsElement() {

		def convertedXform = client.getOpenxdataForm(oid)

		def xforms = convertedXform.form.version.xform.xforms[0]

		assertEquals 'xforms', xforms.name()

	}
	
	@Test public void testGetSubjectsDoesNotReturnNull() {
		def subjects = client.getSubjectKeys("default-study")

		assertNotNull subjects
	}

	@Test public void testGetSubjectsReturnsCorrectNumberOfSubjects() {
		def subjects = client.getSubjectKeys("default-study")

		assertEquals 82, subjects.size()
	}
}
