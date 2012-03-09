package org.openxdata.oc.data

import org.junit.Ignore
import org.openxdata.oc.util.TransformUtil


@Ignore("Some maven installations will attempt to run this as a test")
class TestData {

	static def getCRFWebServiceResponse() {
		
		def response = new TransformUtil().loadFileContents("crf-metadata-response.xml")
	}
	
	static def getOpenXdataInstanceData() {
		
		def instanceData = []
		def oxdInstanceData = '''
								<ODM StudyOID="S_12175" MetaDataVersionOID="v1.0.0" Description="This Xform was converted from an ODM file using the oc-conversion-tools" formKey="F_MSA2_1" name="SC2" StudyEventOID="SE_SC2" id="7" xmlns:xf="http://www.w3.org/2002/xforms">
								  <SubjectKey>Foo_Key</SubjectKey>
								  <IG_MSA2_MSA2_POARTPRECG>
								    <xf:I_MSA2_MSA2_POARTPREC xmlns:xf="http://www.w3.org/2002/xforms" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">Chloroquine</xf:I_MSA2_MSA2_POARTPREC>
								    <xf:I_MSA2_MSA2_POARTNBV xmlns:xf="http://www.w3.org/2002/xforms" ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">3</xf:I_MSA2_MSA2_POARTNBV>
								  </IG_MSA2_MSA2_POARTPRECG>
								  <I_MSA2_INIT ItemGroupOID="IG_MSA2_UNGROUPED">CTK</I_MSA2_INIT>
								  <I_MSA2_FROMD ItemGroupOID="IG_MSA2_UNGROUPED">2012-02-07</I_MSA2_FROMD>
								  <I_MSA2_IDV ItemGroupOID="IG_MSA2_UNGROUPED">009</I_MSA2_IDV>
								  <I_MSA2_MSA2_INITBF ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_INITBF>
								  <I_MSA2_MSA2_INTBF ItemGroupOID="IG_MSA2_UNGROUPED">3</I_MSA2_MSA2_INTBF>
								  <I_MSA2_MSA2_HAART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_HAART>
								  <I_MSA2_MSA2_ELHAART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_ELHAART>
								  <I_MSA2_MSA2_PAZT ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PAZT>
								  <I_MSA2_MSA2_PAZTNB ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_PAZTNB>
								  <I_MSA2_MSA2_POART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_POART>
								  <I_MSA2_MSA2_LNVPV ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LNVPV>
								  <I_MSA2_MSA2_LAZTV ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_LAZTV>
								  <I_MSA2_MSA2_L3TC ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_L3TC>
								  <I_MSA2_MSA2_LOART ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_LOART>
								  <I_MSA2_MSA2_LOCATB ItemGroupOID="IG_MSA2_UNGROUPED">2</I_MSA2_MSA2_LOCATB>
								  <I_MSA2_MSA2_OLOCATBPREC ItemGroupOID="IG_MSA2_UNGROUPED">None</I_MSA2_MSA2_OLOCATBPREC>
								  <I_MSA2_MSA2_TRANSF ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_TRANSF>
								  <I_MSA2_MSA2_TYPED ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_TYPED>
								  <I_MSA2_MSA2_PMTCTV ItemGroupOID="IG_MSA2_UNGROUPED">1</I_MSA2_MSA2_PMTCTV>
								  <I_MSA2_MSA2_PMTCT ItemGroupOID="IG_MSA2_UNGROUPED">0</I_MSA2_MSA2_PMTCT>
								  <I_MSA2_MSA2_LAZTNBV ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_LAZTNBV>
								  <I_MSA2_MSA2_L3TCNB ItemGroupOID="IG_MSA2_UNGROUPED">4</I_MSA2_MSA2_L3TCNB>
								  <I_MSA2_MSA2_L3TCNB2 ItemGroupOID="IG_MSA2_UNGROUPED_2">4</I_MSA2_MSA2_L3TCNB2>
								  <I_MSA2_MSA2_HAART_T ItemGroupOID="IG_MSA2_MSA2_POARTPRECG">0</I_MSA2_MSA2_HAART_T>

								</ODM>
								 '''
		
		instanceData.add(oxdInstanceData)

		return instanceData
	}

	static def event1Xml = """<event>
								<studySubjectOIDs>SS_20100200 SS_2M89098L SS_3M9779A</studySubjectOIDs>
								<formOID>F_AEAD_3</formOID>
								<eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
								<ordinal>1</ordinal>
							 </event>"""
	
	static def eventProxyResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									   <SOAP-ENV:Header/>
									   <SOAP-ENV:Body>
									      <findEventsByStudyOidResponse xmlns="http://openclinica.org/ws/event/v1">
									         <result>Success</result>
									         <events>
									            <event>
									               <studySubjectOIDs>SS_20100200 SS_2M89098L SS_3M9779A</studySubjectOIDs>
									               <formOID>F_AEAD_3</formOID>
									               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A SS_3M0001B SS_3C000LM</studySubjectOIDs>
									               <formOID>F_SAES_2</formOID>
									               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A SS_20100200</studySubjectOIDs>
									               <formOID>F_AEAD_3</formOID>
									               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
									               <formOID>F_SAES_2</formOID>
									               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M1001N SS_3M1235D SS_4M5678B SS_4M1234A SS_3M00022F</studySubjectOIDs>
									               <formOID>F_BC_1</formOID>
									               <eventDefinitionOID>SE_BOTTLE</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_1M0059F SS_2M0230K</studySubjectOIDs>
									               <formOID>F_CCAC_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_1M0059F SS_3C000LM</studySubjectOIDs>
									               <formOID>F_CCMC_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0001B SS_2M0230K</studySubjectOIDs>
									               <formOID>F_CEAC_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M0003B SS_1M0059F</studySubjectOIDs>
									               <formOID>F_CSDI_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0003Z SS_3M1235D SS_20100200_5643 SS_4M0003B SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MBA1_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
									               <formOID>F_MHIV_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
									               <formOID>F_MMLM_1</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M0003B SS_20100200_3763 SS_3M1235D SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_MEAM_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A SS_20100200_3763 SS_20100200_5643 SS_M20002Y SS_3C000LM SS_3M1235D SS_3R1020L</studySubjectOIDs>
									               <formOID>F_MMLM_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643 SS_3M1235D SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MHIV_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M5678B SS_20100200_3763 SS_20100200_5643 SS_3M1235D SS_M20002Y</studySubjectOIDs>
									               <formOID>F_CCMC_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763 SS_M20002Y SS_3M1235D SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M0003B SS_3M1235D SS_20100200_5643 SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CCAC_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M1235D SS_20100200_5643 SS_20100200_3763 SS_M20002Y</studySubjectOIDs>
									               <formOID>F_CLLC_4</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M0003B</studySubjectOIDs>
									               <formOID>F_CEAC_3</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643 SS_3M1234A SS_3M1235D SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CEAC_4</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643 SS_M20002Y SS_3M1235D SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CSDI_2</formOID>
									               <eventDefinitionOID>SE_D7</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_2465 SS_4M1234A SS_4M1001N SS_4M9889Z SS_4M5678B</studySubjectOIDs>
									               <formOID>F_CFSC_1</formOID>
									               <eventDefinitionOID>SE_FINALSTA</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M1234A SS_4M1001N SS_4M5678B</studySubjectOIDs>
									               <formOID>F_BC_1</formOID>
									               <eventDefinitionOID>SE_FINALSTA</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_11111 SS_20100200_8076 SS_4M0003B SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CILC_1</formOID>
									               <eventDefinitionOID>SE_IMMUNISA</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5457</studySubjectOIDs>
									               <formOID>F_CMIC_1</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A SS_3M9876Z</studySubjectOIDs>
									               <formOID>F_MBA2_1</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_6935 SS_3M9876Z SS_20100200_5589</studySubjectOIDs>
									               <formOID>F_MHIV_2</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9876Z SS_20100200_8748</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9876Z</studySubjectOIDs>
									               <formOID>F_CBLC_2</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9876Z</studySubjectOIDs>
									               <formOID>F_MMLM_2</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5589</studySubjectOIDs>
									               <formOID>F_MHIV_2</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
									               <formOID>F_CCMC_2</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
									               <ordinal>2</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M1012W SS_1M0059F</studySubjectOIDs>
									               <formOID>F_MHIV_1</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
									               <formOID>F_MLLM_1</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3C000LM SS_3M0001B SS_4M1004B SS_3R1020L</studySubjectOIDs>
									               <formOID>F_MSA1_1</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0016S SS_4M0003B SS_1M0059F</studySubjectOIDs>
									               <formOID>F_MSA1_2</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M1014M SS_20100200_2350 SS_3C0006S SS_20100200_6947 SS_20100200_4960 SS_20100200_5664 SS_3M0002T SS_20100200_336 SS_20100200_6288 SS_4M1012W SS_20100200_2072 SS_4M1001N SS_20100200_5306 SS_3M0005L SS_2M89098L SS_3M0003T SS_20100200_2465 SS_3M00022F SS_20100200_9269 SS_20100200_4364 SS_20100200_5643 SS_20100200_6999 SS_20100200_3197 SS_20100200_9805 SS_20100200_3931 SS_20100200_5678 SS_20100200_8245 SS_20100200_8693 SS_3M0020P SS_2M0230K SS_4M9999X SS_4M5678B SS_4M1234A SS_20100200_9838 SS_007 SS_20100200_9575 SS_20100200_3638 SS_20100200_8076 SS_20100200_6935 SS_0002 SS_0001 SS_0003 SS_00120 SS_20100200_5589 SS_20100200_8748 SS_0004 SS_20100200_5457 SS_4M1013Q SS_3M0003Z SS_20100200_897 SS_3M0004F SS_3M4651E SS_20100200_3763 SS_20100200 SS_3M1234A</studySubjectOIDs>
									               <formOID>F_MSA1_3</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3931 SS_4M1013Q SS_4M1014M SS_4M1001N SS_3M0020P SS_3M0016S SS_3M0003T SS_3M0005L SS_3M0004F SS_3M0003Z SS_20100200_8748 SS_00120 SS_0001 SS_0002 SS_20100200_6935 SS_20100200_8076 SS_0003 SS_20100200_5589 SS_0004 SS_20100200_5457 SS_20100200_897 SS_20100200_3763 SS_20100200 SS_20100200_2350 SS_20100200_6947 SS_20100200_4960 SS_20100200_5664 SS_20100200_336 SS_20100200_2072 SS_20100200_5306 SS_20100200_5643 SS_20100200_6288 SS_20100200_3638 SS_20100200_9575 SS_20100200_9838 SS_20100200_8693 SS_20100200_4364 SS_20100200_9269 SS_20100200_2465 SS_20100200_6999 SS_20100200_3197 SS_20100200_9805 SS_20100200_5678 SS_20100200_8245 SS_4M9999X SS_4M5678B SS_4M1234A SS_3M1234A SS_2M89098L</studySubjectOIDs>
									               <formOID>F_MHIV_2</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5664 SS_2M89098L SS_20100200_2072 SS_20100200_5306 SS_3M0004F SS_20100200_5678 SS_4M1012W SS_20100200_9805 SS_20100200_8245 SS_4M1014M SS_4M1001N SS_3M0003Z SS_20100200_3197 SS_20100200_8748 SS_20100200_3931 SS_20100200_6999 SS_20100200_4364 SS_20100200_9269 SS_20100200_8693 SS_00120 SS_0001 SS_0002 SS_4M1013Q SS_20100200_6935 SS_4M9999X SS_20100200_8076 SS_4M5678B SS_20100200_4960 SS_20100200_336 SS_0003 SS_20100200_5589 SS_3M0005L SS_4M1234A SS_20100200_2465 SS_20100200_9838 SS_20100200_9575 SS_0004 SS_20100200_5457 SS_20100200_897 SS_20100200_3638 SS_2M89098L SS_20100200_6288 SS_20100200_3763 SS_3M1234A SS_20100200 SS_20100200_2350 SS_20100200_5643 SS_3M00022F SS_3M0020P SS_3M0016S SS_3M0003T SS_20100200_6947</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_2M0230K</studySubjectOIDs>
									               <formOID>F_CLLC_1</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3C000LM</studySubjectOIDs>
									               <formOID>F_CSAC_1</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0004F SS_3C000LM SS_4M1004B SS_4M1001N SS_1M0059F SS_3M0005L</studySubjectOIDs>
									               <formOID>F_MSA2_1</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_6947 SS_20100200_3763 SS_3M0082T SS_3M1235D SS_4M1001N SS_20100200_4960 SS_3M0005L SS_4M1004B SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M1004B</studySubjectOIDs>
									               <formOID>F_CSAC_2</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763 SS_0004 SS_20100200_4960 SS_20100200_5643 SS_4M1004B SS_3M1235D SS_20100200_6947 SS_3M0082T SS_3COO50M SS_3C0052A SS_3M0051T</studySubjectOIDs>
									               <formOID>F_CLLC_4</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0082T SS_3M0051T SS_20100200_3763 SS_000A SS_3C0052A SS_20100200_4960 SS_3M1235D SS_20100200_6947 SS_4M0003B SS_3M0050M SS_20100200_5643 SS_0004</studySubjectOIDs>
									               <formOID>F_MSA2_2</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763 SS_20100200_6947 SS_20100200_4960 SS_20100200_5643 SS_3M0082T SS_3COO50M SS_3C0052A SS_3M0051T SS_3M1235D</studySubjectOIDs>
									               <formOID>F_CSAC_3</formOID>
									               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_MBA1_1</formOID>
									               <eventDefinitionOID>SE_W10</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_CCAC_2</formOID>
									               <eventDefinitionOID>SE_W10</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
									               <formOID>F_CBLC_2</formOID>
									               <eventDefinitionOID>SE_W10</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MBA1_1</formOID>
									               <eventDefinitionOID>SE_W14</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0032G</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_W14</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CCAC_2</formOID>
									               <eventDefinitionOID>SE_W14</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CBLC_2</formOID>
									               <eventDefinitionOID>SE_W14</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_8748</studySubjectOIDs>
									               <formOID>F_CCAC_2</formOID>
									               <eventDefinitionOID>SE_W18</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_4M0003B</studySubjectOIDs>
									               <formOID>F_CBLC_1</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CMIC_1</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0001B</studySubjectOIDs>
									               <formOID>F_CSDC_1</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MBA1_1</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MMLM_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MHIV_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763 SS_2M89098L</studySubjectOIDs>
									               <formOID>F_CCMC_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0011Q SS_20100200_5643 SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_MLLM_3</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CCAC_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
									               <formOID>F_CBLC_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_3763 SS_M001</studySubjectOIDs>
									               <formOID>F_CSDC_2</formOID>
									               <eventDefinitionOID>SE_W2</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_2M0230K</studySubjectOIDs>
									               <formOID>F_MLLM_5</formOID>
									               <eventDefinitionOID>SE_W50</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_3M0061H</studySubjectOIDs>
									               <formOID>F_CMIC_1</formOID>
									               <eventDefinitionOID>SE_W6</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									            <event>
									               <studySubjectOIDs>SS_20100200_8748 SS_3M0061H</studySubjectOIDs>
									               <formOID>F_CBLC_2</formOID>
									               <eventDefinitionOID>SE_W6</eventDefinitionOID>
									               <ordinal>1</ordinal>
									            </event>
									         </events>
									      </findEventsByStudyOidResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>"""
	
	static def eventNode = """<events>
					            <event>
					               <studySubjectOIDs>SS_20100200 SS_2M89098L SS_3M9779A</studySubjectOIDs>
					               <formOID>F_AEAD_3</formOID>
					               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A SS_3M0001B SS_3C000LM</studySubjectOIDs>
					               <formOID>F_SAES_2</formOID>
					               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A SS_20100200</studySubjectOIDs>
					               <formOID>F_AEAD_3</formOID>
					               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
					               <formOID>F_SAES_2</formOID>
					               <eventDefinitionOID>SE_ADVERSEE</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M1001N SS_3M1235D SS_4M5678B SS_4M1234A SS_3M00022F</studySubjectOIDs>
					               <formOID>F_BC_1</formOID>
					               <eventDefinitionOID>SE_BOTTLE</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_1M0059F SS_2M0230K</studySubjectOIDs>
					               <formOID>F_CCAC_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_1M0059F SS_3C000LM</studySubjectOIDs>
					               <formOID>F_CCMC_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0001B SS_2M0230K</studySubjectOIDs>
					               <formOID>F_CEAC_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M0003B SS_1M0059F</studySubjectOIDs>
					               <formOID>F_CSDI_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0003Z SS_3M1235D SS_20100200_5643 SS_4M0003B SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MBA1_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
					               <formOID>F_MHIV_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
					               <formOID>F_MMLM_1</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M0003B SS_20100200_3763 SS_3M1235D SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_MEAM_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A SS_20100200_3763 SS_20100200_5643 SS_M20002Y SS_3C000LM SS_3M1235D SS_3R1020L</studySubjectOIDs>
					               <formOID>F_MMLM_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643 SS_3M1235D SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MHIV_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M5678B SS_20100200_3763 SS_20100200_5643 SS_3M1235D SS_M20002Y</studySubjectOIDs>
					               <formOID>F_CCMC_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763 SS_M20002Y SS_3M1235D SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M0003B SS_3M1235D SS_20100200_5643 SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CCAC_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M1235D SS_20100200_5643 SS_20100200_3763 SS_M20002Y</studySubjectOIDs>
					               <formOID>F_CLLC_4</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M0003B</studySubjectOIDs>
					               <formOID>F_CEAC_3</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643 SS_3M1234A SS_3M1235D SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CEAC_4</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643 SS_M20002Y SS_3M1235D SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CSDI_2</formOID>
					               <eventDefinitionOID>SE_D7</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_2465 SS_4M1234A SS_4M1001N SS_4M9889Z SS_4M5678B</studySubjectOIDs>
					               <formOID>F_CFSC_1</formOID>
					               <eventDefinitionOID>SE_FINALSTA</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M1234A SS_4M1001N SS_4M5678B</studySubjectOIDs>
					               <formOID>F_BC_1</formOID>
					               <eventDefinitionOID>SE_FINALSTA</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_11111 SS_20100200_8076 SS_4M0003B SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CILC_1</formOID>
					               <eventDefinitionOID>SE_IMMUNISA</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5457</studySubjectOIDs>
					               <formOID>F_CMIC_1</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A SS_3M9876Z</studySubjectOIDs>
					               <formOID>F_MBA2_1</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_6935 SS_3M9876Z SS_20100200_5589</studySubjectOIDs>
					               <formOID>F_MHIV_2</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9876Z SS_20100200_8748</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9876Z</studySubjectOIDs>
					               <formOID>F_CBLC_2</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9876Z</studySubjectOIDs>
					               <formOID>F_MMLM_2</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5589</studySubjectOIDs>
					               <formOID>F_MHIV_2</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
					               <formOID>F_CCMC_2</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M9779A</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_INTERCUR</eventDefinitionOID>
					               <ordinal>2</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M1012W SS_1M0059F</studySubjectOIDs>
					               <formOID>F_MHIV_1</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_1M0059F</studySubjectOIDs>
					               <formOID>F_MLLM_1</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3C000LM SS_3M0001B SS_4M1004B SS_3R1020L</studySubjectOIDs>
					               <formOID>F_MSA1_1</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0016S SS_4M0003B SS_1M0059F</studySubjectOIDs>
					               <formOID>F_MSA1_2</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M1014M SS_20100200_2350 SS_3C0006S SS_20100200_6947 SS_20100200_4960 SS_20100200_5664 SS_3M0002T SS_20100200_336 SS_20100200_6288 SS_4M1012W SS_20100200_2072 SS_4M1001N SS_20100200_5306 SS_3M0005L SS_2M89098L SS_3M0003T SS_20100200_2465 SS_3M00022F SS_20100200_9269 SS_20100200_4364 SS_20100200_5643 SS_20100200_6999 SS_20100200_3197 SS_20100200_9805 SS_20100200_3931 SS_20100200_5678 SS_20100200_8245 SS_20100200_8693 SS_3M0020P SS_2M0230K SS_4M9999X SS_4M5678B SS_4M1234A SS_20100200_9838 SS_007 SS_20100200_9575 SS_20100200_3638 SS_20100200_8076 SS_20100200_6935 SS_0002 SS_0001 SS_0003 SS_00120 SS_20100200_5589 SS_20100200_8748 SS_0004 SS_20100200_5457 SS_4M1013Q SS_3M0003Z SS_20100200_897 SS_3M0004F SS_3M4651E SS_20100200_3763 SS_20100200 SS_3M1234A</studySubjectOIDs>
					               <formOID>F_MSA1_3</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3931 SS_4M1013Q SS_4M1014M SS_4M1001N SS_3M0020P SS_3M0016S SS_3M0003T SS_3M0005L SS_3M0004F SS_3M0003Z SS_20100200_8748 SS_00120 SS_0001 SS_0002 SS_20100200_6935 SS_20100200_8076 SS_0003 SS_20100200_5589 SS_0004 SS_20100200_5457 SS_20100200_897 SS_20100200_3763 SS_20100200 SS_20100200_2350 SS_20100200_6947 SS_20100200_4960 SS_20100200_5664 SS_20100200_336 SS_20100200_2072 SS_20100200_5306 SS_20100200_5643 SS_20100200_6288 SS_20100200_3638 SS_20100200_9575 SS_20100200_9838 SS_20100200_8693 SS_20100200_4364 SS_20100200_9269 SS_20100200_2465 SS_20100200_6999 SS_20100200_3197 SS_20100200_9805 SS_20100200_5678 SS_20100200_8245 SS_4M9999X SS_4M5678B SS_4M1234A SS_3M1234A SS_2M89098L</studySubjectOIDs>
					               <formOID>F_MHIV_2</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5664 SS_2M89098L SS_20100200_2072 SS_20100200_5306 SS_3M0004F SS_20100200_5678 SS_4M1012W SS_20100200_9805 SS_20100200_8245 SS_4M1014M SS_4M1001N SS_3M0003Z SS_20100200_3197 SS_20100200_8748 SS_20100200_3931 SS_20100200_6999 SS_20100200_4364 SS_20100200_9269 SS_20100200_8693 SS_00120 SS_0001 SS_0002 SS_4M1013Q SS_20100200_6935 SS_4M9999X SS_20100200_8076 SS_4M5678B SS_20100200_4960 SS_20100200_336 SS_0003 SS_20100200_5589 SS_3M0005L SS_4M1234A SS_20100200_2465 SS_20100200_9838 SS_20100200_9575 SS_0004 SS_20100200_5457 SS_20100200_897 SS_20100200_3638 SS_2M89098L SS_20100200_6288 SS_20100200_3763 SS_3M1234A SS_20100200 SS_20100200_2350 SS_20100200_5643 SS_3M00022F SS_3M0020P SS_3M0016S SS_3M0003T SS_20100200_6947</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_SC1</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_2M0230K</studySubjectOIDs>
					               <formOID>F_CLLC_1</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3C000LM</studySubjectOIDs>
					               <formOID>F_CSAC_1</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0004F SS_3C000LM SS_4M1004B SS_4M1001N SS_1M0059F SS_3M0005L</studySubjectOIDs>
					               <formOID>F_MSA2_1</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_6947 SS_20100200_3763 SS_3M0082T SS_3M1235D SS_4M1001N SS_20100200_4960 SS_3M0005L SS_4M1004B SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M1004B</studySubjectOIDs>
					               <formOID>F_CSAC_2</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763 SS_0004 SS_20100200_4960 SS_20100200_5643 SS_4M1004B SS_3M1235D SS_20100200_6947 SS_3M0082T SS_3COO50M SS_3C0052A SS_3M0051T</studySubjectOIDs>
					               <formOID>F_CLLC_4</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0082T SS_3M0051T SS_20100200_3763 SS_000A SS_3C0052A SS_20100200_4960 SS_3M1235D SS_20100200_6947 SS_4M0003B SS_3M0050M SS_20100200_5643 SS_0004</studySubjectOIDs>
					               <formOID>F_MSA2_2</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763 SS_20100200_6947 SS_20100200_4960 SS_20100200_5643 SS_3M0082T SS_3COO50M SS_3C0052A SS_3M0051T SS_3M1235D</studySubjectOIDs>
					               <formOID>F_CSAC_3</formOID>
					               <eventDefinitionOID>SE_SC2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_MBA1_1</formOID>
					               <eventDefinitionOID>SE_W10</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_CCAC_2</formOID>
					               <eventDefinitionOID>SE_W10</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_5643</studySubjectOIDs>
					               <formOID>F_CBLC_2</formOID>
					               <eventDefinitionOID>SE_W10</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MBA1_1</formOID>
					               <eventDefinitionOID>SE_W14</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0032G</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_W14</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CCAC_2</formOID>
					               <eventDefinitionOID>SE_W14</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CBLC_2</formOID>
					               <eventDefinitionOID>SE_W14</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_8748</studySubjectOIDs>
					               <formOID>F_CCAC_2</formOID>
					               <eventDefinitionOID>SE_W18</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_4M0003B</studySubjectOIDs>
					               <formOID>F_CBLC_1</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CMIC_1</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0001B</studySubjectOIDs>
					               <formOID>F_CSDC_1</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MBA1_1</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MMLM_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MHIV_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763 SS_2M89098L</studySubjectOIDs>
					               <formOID>F_CCMC_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0011Q SS_20100200_5643 SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_MLLM_3</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CCAC_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763</studySubjectOIDs>
					               <formOID>F_CBLC_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_3763 SS_M001</studySubjectOIDs>
					               <formOID>F_CSDC_2</formOID>
					               <eventDefinitionOID>SE_W2</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_2M0230K</studySubjectOIDs>
					               <formOID>F_MLLM_5</formOID>
					               <eventDefinitionOID>SE_W50</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_3M0061H</studySubjectOIDs>
					               <formOID>F_CMIC_1</formOID>
					               <eventDefinitionOID>SE_W6</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					            <event>
					               <studySubjectOIDs>SS_20100200_8748 SS_3M0061H</studySubjectOIDs>
					               <formOID>F_CBLC_2</formOID>
					               <eventDefinitionOID>SE_W6</eventDefinitionOID>
					               <ordinal>1</ordinal>
					            </event>
					         </events>"""
	
	static def studySubjectListSOAPResponse = """<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										   <SOAP-ENV:Header/>
										   <SOAP-ENV:Body>
											  <ns4:listAllByStudyResponse xmlns:ns4="http://openclinica.org/ws/studySubject/v1" xmlns:ns2="http://openclinica.org/ws/beans" xmlns:ns3="http://openclinica.org/ws/crf/v1">
												 <ns4:result>Success</ns4:result>
												 <ns4:studySubjects>
													<ns2:studySubject>
													   <ns2:label>Morten</ns2:label>
													   <ns2:secondaryLabel/>
													   <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
													   <ns2:subject>
														  <ns2:uniqueIdentifier>SS_Morten</ns2:uniqueIdentifier>
														  <ns2:gender>m</ns2:gender>
														  <ns2:dateOfBirth>2011-09-06</ns2:dateOfBirth>
													   </ns2:subject>
													   <ns2:events/>
													</ns2:studySubject>
													<ns2:studySubject>
													   <ns2:label>jorn</ns2:label>
													   <ns2:secondaryLabel/>
													   <ns2:enrollmentDate>2011-09-08</ns2:enrollmentDate>
													   <ns2:subject>
														  <ns2:uniqueIdentifier>SS_Jørn</ns2:uniqueIdentifier>
														  <ns2:gender>m</ns2:gender>
														  <ns2:dateOfBirth>2011-09-29</ns2:dateOfBirth>
													   </ns2:subject>
													   <ns2:events/>
													</ns2:studySubject>
													<ns2:studySubject>
													   <ns2:label>jonny</ns2:label>
													   <ns2:secondaryLabel/>
													   <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
													   <ns2:subject>
														  <ns2:uniqueIdentifier>SS_Jonny</ns2:uniqueIdentifier>
														  <ns2:gender>m</ns2:gender>
														  <ns2:dateOfBirth>2011-09-12</ns2:dateOfBirth>
													   </ns2:subject>
													   <ns2:events/>
													</ns2:studySubject>
													<ns2:studySubject>
													   <ns2:label>janne</ns2:label>
													   <ns2:secondaryLabel/>
													   <ns2:enrollmentDate>2011-09-29</ns2:enrollmentDate>
													   <ns2:subject>
														  <ns2:uniqueIdentifier>SS_Janne</ns2:uniqueIdentifier>
														  <ns2:gender>m</ns2:gender>
														  <ns2:dateOfBirth>2011-09-16</ns2:dateOfBirth>
													   </ns2:subject>
													   <ns2:events/>
													</ns2:studySubject>
												 </ns4:studySubjects>
											  </ns4:listAllByStudyResponse>
										   </SOAP-ENV:Body>
										</SOAP-ENV:Envelope>"""

	static def importSOAPSuccessResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
										   <SOAP-ENV:Header/>
										   <SOAP-ENV:Body>
											  <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
												 <result>Success</result>
											  </importDataResponse>
										   </SOAP-ENV:Body>
										</SOAP-ENV:Envelope>'''

	static def importSOAPErrorResponse = '''<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
									   <SOAP-ENV:Header/>
									   <SOAP-ENV:Body>
									      <importDataResponse xmlns="http://openclinica.org/ws/data/v1">
									         <result>Fail</result>
									         <error>Error.</error>
									      </importDataResponse>
									   </SOAP-ENV:Body>
									</SOAP-ENV:Envelope>'''
}
