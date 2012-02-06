package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.exception.ImportException


class ODMDefinitionTest extends GroovyTestCase {

	def exportedInstanceData
	def instanceData = new ArrayList<String>()
	
	public void setUp(){

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
		
		def odmDef = new ODMDefinition()
		exportedInstanceData = odmDef.appendInstanceData(instanceData)
	}
	
	@Test void testAppendInstanceDataDoesNotReturnNull() {
		
		def xml = new XmlParser().parseText(exportedInstanceData)
		
		assertNotNull xml.instanceData
	}
	
	@Test void testAppendInstanceDataAppendsTheInstanceData(){
		
		
		def xml = new XmlParser().parseText(exportedInstanceData)
		
		assertEquals xml.depthFirst().ItemData.size(), 4
	}
	
	@Test void testAppendInstanceDataShouldThrowExceptionOnNullInstanceData(){
		def emptyInstanceData = new ArrayList<String>()
		shouldFail(ImportException.class){
			new ODMDefinition().appendInstanceData(emptyInstanceData)
		}
	}
}
