package org.openxdata.oc;

import static org.junit.Assert.*

import org.junit.Test

class ODMBuilderTest {

	@Test
	void testThatInstanceDataIsInserted(){
		
		def instanceData = new ArrayList<String>()
		instanceData.add("""<?xml version="1.0" encoding="UTF-8"?>
						<default_study_-_uganda_se_sc1_sc1-v1 xmlns="" Description="converted from ODM to Xform" formKey="default_study_-_uganda_se_sc1_sc1-v1" id="8" name="SE_SC1_SC1-v1">
						  <ClinicalData xmlns="http://www.w3.org/2002/xforms" MetaDataVersionOID="v1.0.0" StudyOID="S_12175">
						    <SubjectData SubjectKey="Mark">
						      <StudyEventData StudyEventOID="SE_SC1">
						        <FormData FormOID="F_MSA1_3">
						          <ItemGroupData ItemGroupOID="IG_MSA1_UNGROUPED">
						            <ItemData ItemOID="I_MSA1_INIT" value="" Value="Mark Gerard"/>
						            <ItemData ItemOID="I_MSA1_FORMD" value="" Value="2011-08-23"/>
						            <ItemData ItemOID="I_MSA1_MSA1_CONS" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_PGT" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_BIRTHD" value="" Value="2011-09-29"/>
						            <ItemData ItemOID="I_MSA1_MSA1_OLD" value="" Value="26"/>
						            <ItemData ItemOID="I_MSA1_MSA1_KHIV" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_HAART" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_ELHAART" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_BEFPEP" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_OTRIAL" value="" Value="0"/>
						            <ItemData ItemOID="I_MSA1_MSA1_OTRIAL_S" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_MOVE" value="" Value="0"/>
						            <ItemData ItemOID="I_MSA1_MSA1_PMTCT" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_COMPLY" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_MSA1_ELIG" value="" Value="0"/>
						            <ItemData ItemOID="I_MSA1_MSA1_DELIVERY" value="" Value="Morten's home"/>
						            <ItemData ItemOID="I_MSA1_MSA1_CONSELLING" value="" Value="1"/>
						            <ItemData ItemOID="I_MSA1_IDPAT" value="" Value="0001"/>
						            <ItemData ItemOID="I_MSA1_ID" value="" Value="001"/>
						          </ItemGroupData>
						        </FormData>
						        <FormData FormOID="F_MSA1_2">
						          <ItemGroupData ItemGroupOID="IG_MSA1_MSA1_COMPLYREASG">
						            <ItemData ItemOID="I_MSA1_MSA1_COMPLYREAS" value="" Value="Ask your mother"/>
						          </ItemGroupData>
						        </FormData>
						      </StudyEventData>
						    </SubjectData>
						  </ClinicalData>
						</default_study_-_uganda_se_sc1_sc1-v1>""")
		
		def builder = new ODMBuilder()
		def odm = builder.buildODM(instanceData)
		
		def xml = new XmlParser().parseText(odm);
		assertTrue(xml.depthFirst().ItemData.size() > 1)
		assertEquals(xml.depthFirst().ItemData.size(), 21)
		
	}
}
