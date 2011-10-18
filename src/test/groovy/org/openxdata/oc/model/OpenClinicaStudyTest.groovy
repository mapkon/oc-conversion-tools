package org.openxdata.oc.model

import org.junit.Test

class OpenClinicaStudyTest extends GroovyTestCase {

	@Test
	void testEquals(){
		def study = new OpenclinicaStudy(identifier: "default-study", OID: "S_DEFAULTS1", name:"Default Study")
		def study1 = new OpenclinicaStudy(identifier: "default-study", OID: "S_DEFAULTS1", name:"Default Study")
		def study2 = new OpenclinicaStudy(identifier: "001", OID: "S_001", name: "Test Study")
		
		assertTrue study.equals(study1)
		assertFalse study.equals(study2)
	}
}
