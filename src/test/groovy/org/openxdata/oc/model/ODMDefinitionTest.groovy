package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.TransformUtil

class ODMDefinitionTest extends GroovyTestCase {

	def odmDef
	
	public void setUp(){

		def odmXmlStream = new TransformUtil().loadFileContents("test-odm.xml")

		odmDef = new ODMDefinition()
		odmDef.initializeProperties(odmXmlStream)
	}

	@Test void testInitializePropertiesShouldInitializeProperties(){
		assertNotNull odmDef.OID
		assertNotNull odmDef.name
		assertNotNull odmDef.description	
		assertNotNull odmDef.studyEventDefs
	}
	
	@Test void testInitializedOIDShouldEqualsNameInODM(){
		assertEquals "S_12175", odmDef.OID
	}
	
	@Test void testInitializedNameShouldEqualsNameInODM(){
		assertEquals "Default Study - Uganda", odmDef.name
	}
	
	@Test void testInitializedDescriptionShouldEqualsNameInODM(){
		assertEquals "test instance - Site of Uganda", odmDef.description
	}
	
	@Test void testInitializedStudyEventDefsShouldBeSameSizeAsInODM(){
		
		def studyEventDefs = odmDef.studyEventDefs
		
		assertEquals 2, studyEventDefs.size()
		assertEquals 'SE_SC1', studyEventDefs[0].'@OID'.text()
		assertEquals 'SE_SC2', studyEventDefs[1].'@OID'.text()
	}
}
