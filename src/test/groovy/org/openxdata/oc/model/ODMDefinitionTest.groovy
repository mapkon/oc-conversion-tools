package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.TransformUtil


class ODMDefinitionTest extends GroovyTestCase {

	def odmDef
	def instanceData = new ArrayList<String>()
	
	public void setUp(){

		def odmXmlStream = new TransformUtil().loadFileContents("test-odm.xml")

		odmDef = new ODMDefinition()
		odmDef.initializeProperties(odmXmlStream)
		
		instanceData.add("""<?xml version="1.0" encoding="UTF-8"?>
							<test_study_se_visit_visit-v1 xmlns="" Description="converted from ODM to Xform" formKey="test_study_se_visit_visit-v1" id="10" name="SE_VISIT_Visit-v1">
							  <ClinicalData xmlns="http://www.w3.org/2002/xforms" MetaDataVersionOID="v1.0.0" StudyOID="S_001">
								<SubjectData SubjectKey="SS_MARK">
								  <StudyEventData StudyEventOID="SE_VISIT">
									<FormData FormOID="F_SAMPLECRF_1">
									  <ItemGroupData ItemGroupOID="IG_SAMPL_UNGROUPED">
										<ItemData ItemOID="I_SAMPL_SC_ITEM_01" Value="really" value=""/>
										<ItemData ItemOID="I_SAMPL_SC_ITEM_02" Value="ok" value=""/>
									  </ItemGroupData>
									  <ItemGroupData ItemGroupOID="IG_SAMPL_GROUP01">
										<ItemData ItemOID="I_SAMPL_SC_REPEATING_ITEM_01" Value="2011-09-15" value=""/>
										<ItemData ItemOID="I_SAMPL_SC_REPEATING_ITEM_02" Value="222" value=""/>
									  </ItemGroupData>
									</FormData>
								  </StudyEventData>
								</SubjectData>
							  </ClinicalData>
							</test_study_se_visit_visit-v1>""")
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
	
	@Test void testAppendInstanceDataAppendsTheInstanceData(){
		def odm = odmDef.appendInstanceData(instanceData)
		
		def xml = new XmlParser().parseText(odm)
		
		assertNotNull odmDef.instanceData
		assertTrue xml.depthFirst().ItemData.size() > 1
		assertEquals xml.depthFirst().ItemData.size(), 4
	}
	
	//Test empty instance data.
}
