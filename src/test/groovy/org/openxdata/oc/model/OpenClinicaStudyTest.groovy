package org.openxdata.oc.model

import org.junit.Test

class OpenClinicaStudyTest extends GroovyTestCase {

	def study
	public void setUp(){
		study = new OpenclinicaStudy(OID:"S_DEFAULTS1", identifier:"default-study", name:"Default Study")
	}
	
	@Test void testEquals(){
		
		def study2 = new OpenclinicaStudy(OID:"S_001", identifier:"001",  name:"Test Study")
		
		assertTrue study.equals(study)
		assertSame "same objects", study, study
		
		// Triangulation
		assertNotSame "Different Objects", study, study2
	}
	
	@Test void testToString(){		
		
		def expected = "org.openxdata.oc.model.OpenclinicaStudy(OID:S_DEFAULTS1, identifier:default-study, name:Default Study)"
		
		assertEquals "Should be equal", expected, study.toString()
		
		// Triangulation
		assertFalse "The names should not be equal", "org.openxdata.oc.model.OpenclinicaStudy(OID:S_DEFAULTS2, identifier:Default-study2, name:Default Study2)".equals(study.toString())
	}
}
