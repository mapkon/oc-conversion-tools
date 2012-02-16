package org.openxdata.oc.util

import org.junit.Test


class TransformUtilTest extends GroovyTestCase {

	def odmFileContent

	def util = new TransformUtil()

	public void setUp() {

		odmFileContent = util.loadFileContents('test-odm.xml')
	}

	@Test void testLoadFileContentsDoesNotReturnNull(){
		assertNotNull odmFileContent
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectStartingTag(){

		assertTrue odmFileContent.contains('<ODM')
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectEndingTag(){

		assertTrue odmFileContent.endsWith('</ODM>')
	}

	@Test void testLoadFileContentsStartWithXmlProcessingInstructions(){
		assertTrue odmFileContent.startsWith('''<?xml version="1.0" encoding="UTF-8"?>''')
	}

	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFileContents('')
		}
	}

	@Test void testLoadFileContentsRendersCorrectMessageOnEmptyFileName(){
		try{
			util.loadFileContents('')
		}catch(def ex){
			assertEquals 'File name cannot be null or empty.', ex.getMessage()
		}
	}
	
	@Test void testGetNameReturnsShouldNotReturnNull() {
		def name = util.getStudyName(convertedXform)
		
		assertNotNull name
	}
	
	@Test void testGetNameReturnsCorrectNameOnValidInputFile() {
		
		def name = util.getStudyName(convertedXform)
		
		assertEquals 'Default Study - Uganda', name
	}
	
	def convertedXform = """<study description="test instance - Site of Uganda" name="Default Study - Uganda" studyKey="S_12175">
							  <form description="SC1" name="SE_SC1">
							    <version description="Converted from ODM using the oc-conversion-tools" name="SC1-v1">
							      <xform>&lt;?xml version="1.0" encoding="UTF-8"?&gt;
									&lt;xf:xforms xmlns:xf='http://www.w3.org/2002/xforms'&gt;&lt;xf:model&gt;&lt;xf:instance id='S_12175 - SE_SC1'&gt;&lt;xf:ODM StudyOID='S_12175' MetaDataVersionOID='v1.0.0' Description='This Xform was converted from an ODM file using the oc-conversion-tools' formKey='SE_SC1' name='SC1' SubjectKey='' StudyEventOID='SE_SC1'&gt;&lt;xf:I_MSA1_INIT FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_INIT&gt;&lt;xf:I_MSA1_FORMD FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_FORMD&gt;&lt;xf:I_MSA1_MSA1_CONS FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_CONS&gt;&lt;xf:I_MSA1_MSA1_PGT FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_PGT&gt;&lt;xf:I_MSA1_MSA1_BIRTHD FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_BIRTHD&gt;&lt;xf:I_MSA1_MSA1_OLD FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_OLD&gt;&lt;xf:I_MSA1_MSA1_KHIV FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_KHIV&gt;&lt;xf:I_MSA1_MSA1_HAART FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_HAART&gt;&lt;xf:I_MSA1_MSA1_ELHAART FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_ELHAART&gt;&lt;xf:I_MSA1_MSA1_BEFPEP FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_BEFPEP&gt;&lt;xf:I_MSA1_MSA1_OTRIAL FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_OTRIAL&gt;&lt;xf:I_MSA1_MSA1_OTRIAL_S FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_OTRIAL_S&gt;&lt;xf:I_MSA1_MSA1_MOVE FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_MOVE&gt;&lt;xf:I_MSA1_MSA1_PMTCT FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_PMTCT&gt;&lt;xf:I_MSA1_MSA1_COMPLY FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_COMPLY&gt;&lt;xf:I_MSA1_MSA1_ELIG FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_ELIG&gt;&lt;xf:I_MSA1_MSA1_DELIVERY FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_DELIVERY&gt;&lt;xf:I_MSA1_MSA1_CONSELLING FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_MSA1_CONSELLING&gt;&lt;xf:I_MSA1_IDPAT FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_IDPAT&gt;&lt;xf:I_MSA1_ID FormOID='F_MSA1_3' ItemGroupOID='IG_MSA1_UNGROUPED'&gt;&lt;/xf:I_MSA1_ID&gt;&lt;xf:IG_MSA1_MSA1_COMPLYREASG&gt;&lt;xf:I_MSA1_MSA1_COMPLYREAS FormOID='F_MSA1_2' ItemGroupOID='IG_MSA1_MSA1_COMPLYREASG'&gt;&lt;/xf:I_MSA1_MSA1_COMPLYREAS&gt;&lt;/xf:IG_MSA1_MSA1_COMPLYREASG&gt;&lt;/xf:ODM&gt;&lt;/xf:instance&gt;&lt;xf:bind id='I_MSA1_INIT' nodeset='/ODM/I_MSA1_INIT' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_FORMD' nodeset='/ODM/I_MSA1_FORMD' type='xsd:date'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_CONS' nodeset='/ODM/I_MSA1_MSA1_CONS' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_PGT' nodeset='/ODM/I_MSA1_MSA1_PGT' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_BIRTHD' nodeset='/ODM/I_MSA1_MSA1_BIRTHD' type='xsd:date'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_OLD' nodeset='/ODM/I_MSA1_MSA1_OLD' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_KHIV' nodeset='/ODM/I_MSA1_MSA1_KHIV' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_HAART' nodeset='/ODM/I_MSA1_MSA1_HAART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_ELHAART' nodeset='/ODM/I_MSA1_MSA1_ELHAART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_BEFPEP' nodeset='/ODM/I_MSA1_MSA1_BEFPEP' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_OTRIAL' nodeset='/ODM/I_MSA1_MSA1_OTRIAL' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_OTRIAL_S' nodeset='/ODM/I_MSA1_MSA1_OTRIAL_S' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_MOVE' nodeset='/ODM/I_MSA1_MSA1_MOVE' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_PMTCT' nodeset='/ODM/I_MSA1_MSA1_PMTCT' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_COMPLY' nodeset='/ODM/I_MSA1_MSA1_COMPLY' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_ELIG' nodeset='/ODM/I_MSA1_MSA1_ELIG' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_DELIVERY' nodeset='/ODM/I_MSA1_MSA1_DELIVERY' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_CONSELLING' nodeset='/ODM/I_MSA1_MSA1_CONSELLING' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_IDPAT' nodeset='/ODM/I_MSA1_IDPAT' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_ID' nodeset='/ODM/I_MSA1_ID' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='IG_MSA1_MSA1_COMPLYREASG' nodeset='/ODM/IG_MSA1_MSA1_COMPLYREASG'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA1_MSA1_COMPLYREAS' nodeset='/ODM/IG_MSA1_MSA1_COMPLYREASG/I_MSA1_MSA1_COMPLYREAS' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;/xf:model&gt;&lt;xf:group id='1'&gt;&lt;xf:label&gt;MSA1: Mother Screening Assessment 1 - 3&lt;/xf:label&gt;&lt;xf:input bind='I_MSA1_INIT'&gt;&lt;xf:label&gt;Fill in your initials (Given Name (G), Surname (S))&lt;/xf:label&gt;&lt;xf:hint&gt;10^3/MM^3&lt;/xf:hint&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA1_FORMD'&gt;&lt;xf:label&gt;Date of form&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA1_MSA1_CONS'&gt;&lt;xf:label&gt;Inclusion/Exclusion criteria - Has the woman understood/read and signed the informed consent for screening?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_PGT'&gt;&lt;xf:label&gt;Is she pregnant?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA1_MSA1_BIRTHD'&gt;&lt;xf:label&gt;What is her birth date?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA1_MSA1_OLD'&gt;&lt;xf:label&gt;If birth date not documented, how old is she?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA1_MSA1_KHIV'&gt;&lt;xf:label&gt;Is her HIV status documented from the antenatal clinic (or other centre)?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_HAART'&gt;&lt;xf:label&gt;Is she currently on HAART?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_ELHAART'&gt;&lt;xf:label&gt;If not, is she eligible for HAART?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_BEFPEP'&gt;&lt;xf:label&gt;Has she participated in the Promise PEP study during a previous pregnancy?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_OTRIAL'&gt;&lt;xf:label&gt;Is she currently in any other trial ?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_OTRIAL_S'&gt;&lt;xf:label&gt;If yes, will she still be participating in this trial one week after delivery?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_MOVE'&gt;&lt;xf:label&gt;Does she intend to move out of the area of the study in the next year?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_PMTCT'&gt;&lt;xf:label&gt;Is she included in a PMTCT program?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_COMPLY'&gt;&lt;xf:label&gt;Is there anything that could prevent her from complying with study procedures? (probe according to SOP)&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA1_MSA1_ELIG'&gt;&lt;xf:label&gt;Based on the screening procedures and questions so far: Is the mother eligible for further participation&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA1_MSA1_DELIVERY'&gt;&lt;xf:label&gt;Mother related - Where does she intend to deliver?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA1_MSA1_CONSELLING'&gt;&lt;xf:label&gt;Is she willing to accept infant feeding option counselling from a designated research counsellor?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA1_IDPAT'&gt;&lt;xf:label&gt;Patient ID&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA1_ID'&gt;&lt;xf:label&gt;Patient ID&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;/xf:group&gt;&lt;xf:group id='2'&gt;&lt;xf:label&gt;MSA1: Mother Screening Assessment 1 - 2&lt;/xf:label&gt;&lt;xf:group id='IG_MSA1_MSA1_COMPLYREASG'&gt;&lt;xf:label&gt;MSA1: Mother Screening Assessment 1 - 2&lt;/xf:label&gt;&lt;xf:repeat bind='IG_MSA1_MSA1_COMPLYREASG'&gt;&lt;xf:input bind='I_MSA1_MSA1_COMPLYREAS'&gt;&lt;xf:label&gt;Test ItemHeader - Test ItemSubHeader - Test Left Item Text&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;/xf:repeat&gt;&lt;/xf:group&gt;&lt;/xf:group&gt;&lt;/xf:xforms&gt;</xform>
							    </version>
							  </form>
							  <form description="SC2" name="SE_SC2">
							    <version description="Converted from ODM using the oc-conversion-tools" name="SC2-v1">
							      <xform>&lt;?xml version="1.0" encoding="UTF-8"?&gt;
									&lt;xf:xforms xmlns:xf='http://www.w3.org/2002/xforms'&gt;&lt;xf:model&gt;&lt;xf:instance id='S_12175 - SE_SC2'&gt;&lt;xf:ODM StudyOID='S_12175' MetaDataVersionOID='v1.0.0' Description='This Xform was converted from an ODM file using the oc-conversion-tools' formKey='SE_SC2' name='SC2' SubjectKey='' StudyEventOID='SE_SC2'&gt;&lt;xf:IG_MSA2_MSA2_POARTPRECG&gt;&lt;xf:I_MSA2_MSA2_POARTPREC FormOID='F_MSA2_2' ItemGroupOID='IG_MSA2_MSA2_POARTPRECG'&gt;&lt;/xf:I_MSA2_MSA2_POARTPREC&gt;&lt;xf:I_MSA2_MSA2_POARTNBV FormOID='F_MSA2_2' ItemGroupOID='IG_MSA2_MSA2_POARTPRECG'&gt;&lt;/xf:I_MSA2_MSA2_POARTNBV&gt;&lt;/xf:IG_MSA2_MSA2_POARTPRECG&gt;&lt;xf:I_MSA2_INIT FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_INIT&gt;&lt;xf:I_MSA2_FROMD FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_FROMD&gt;&lt;xf:I_MSA2_IDV FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_IDV&gt;&lt;xf:I_MSA2_MSA2_INITBF FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_INITBF&gt;&lt;xf:I_MSA2_MSA2_INTBF FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_INTBF&gt;&lt;xf:I_MSA2_MSA2_HAART FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_HAART&gt;&lt;xf:I_MSA2_MSA2_ELHAART FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_ELHAART&gt;&lt;xf:I_MSA2_MSA2_PAZT FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_PAZT&gt;&lt;xf:I_MSA2_MSA2_PAZTNB FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_PAZTNB&gt;&lt;xf:I_MSA2_MSA2_POART FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_POART&gt;&lt;xf:I_MSA2_MSA2_LNVPV FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_LNVPV&gt;&lt;xf:I_MSA2_MSA2_LAZTV FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_LAZTV&gt;&lt;xf:I_MSA2_MSA2_L3TC FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_L3TC&gt;&lt;xf:I_MSA2_MSA2_LOART FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_LOART&gt;&lt;xf:I_MSA2_MSA2_LOCATB FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_LOCATB&gt;&lt;xf:I_MSA2_MSA2_OLOCATBPREC FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_OLOCATBPREC&gt;&lt;xf:I_MSA2_MSA2_TRANSF FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_TRANSF&gt;&lt;xf:I_MSA2_MSA2_TYPED FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_TYPED&gt;&lt;xf:I_MSA2_MSA2_PMTCTV FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_PMTCTV&gt;&lt;xf:I_MSA2_MSA2_PMTCT FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_PMTCT&gt;&lt;xf:I_MSA2_MSA2_LAZTNBV FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_LAZTNBV&gt;&lt;xf:I_MSA2_MSA2_L3TCNB FormOID='F_MSA2_1' ItemGroupOID='IG_MSA2_UNGROUPED'&gt;&lt;/xf:I_MSA2_MSA2_L3TCNB&gt;&lt;/xf:ODM&gt;&lt;/xf:instance&gt;&lt;xf:bind id='IG_MSA2_MSA2_POARTPRECG' nodeset='/ODM/IG_MSA2_MSA2_POARTPRECG'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_POARTPREC' nodeset='/ODM/IG_MSA2_MSA2_POARTPRECG/I_MSA2_MSA2_POARTPREC' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_POARTNBV' nodeset='/ODM/IG_MSA2_MSA2_POARTPRECG/I_MSA2_MSA2_POARTNBV' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_INIT' nodeset='/ODM/I_MSA2_INIT' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_FROMD' nodeset='/ODM/I_MSA2_FROMD' type='xsd:date'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_IDV' nodeset='/ODM/I_MSA2_IDV' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_INITBF' nodeset='/ODM/I_MSA2_MSA2_INITBF' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_INTBF' nodeset='/ODM/I_MSA2_MSA2_INTBF' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_HAART' nodeset='/ODM/I_MSA2_MSA2_HAART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_ELHAART' nodeset='/ODM/I_MSA2_MSA2_ELHAART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_PAZT' nodeset='/ODM/I_MSA2_MSA2_PAZT' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_PAZTNB' nodeset='/ODM/I_MSA2_MSA2_PAZTNB' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_POART' nodeset='/ODM/I_MSA2_MSA2_POART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_LNVPV' nodeset='/ODM/I_MSA2_MSA2_LNVPV' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_LAZTV' nodeset='/ODM/I_MSA2_MSA2_LAZTV' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_L3TC' nodeset='/ODM/I_MSA2_MSA2_L3TC' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_LOART' nodeset='/ODM/I_MSA2_MSA2_LOART' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_LOCATB' nodeset='/ODM/I_MSA2_MSA2_LOCATB' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_OLOCATBPREC' nodeset='/ODM/I_MSA2_MSA2_OLOCATBPREC' type='xsd:string'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_TRANSF' nodeset='/ODM/I_MSA2_MSA2_TRANSF' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_TYPED' nodeset='/ODM/I_MSA2_MSA2_TYPED' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_PMTCTV' nodeset='/ODM/I_MSA2_MSA2_PMTCTV' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_PMTCT' nodeset='/ODM/I_MSA2_MSA2_PMTCT' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_LAZTNBV' nodeset='/ODM/I_MSA2_MSA2_LAZTNBV' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;xf:bind id='I_MSA2_MSA2_L3TCNB' nodeset='/ODM/I_MSA2_MSA2_L3TCNB' type='xsd:int'&gt;&lt;/xf:bind&gt;&lt;/xf:model&gt;&lt;xf:group id='1'&gt;&lt;xf:label&gt;MSA2: Mother Screening Assessment 2 - 2&lt;/xf:label&gt;&lt;xf:group id='IG_MSA2_MSA2_POARTPRECG'&gt;&lt;xf:label&gt;MSA2: Mother Screening Assessment 2 - 2&lt;/xf:label&gt;&lt;xf:repeat bind='IG_MSA2_MSA2_POARTPRECG'&gt;&lt;xf:input bind='I_MSA2_MSA2_POARTPREC'&gt;&lt;xf:label&gt;Drug - Drug&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA2_MSA2_POARTNBV'&gt;&lt;xf:label&gt;For how many weeks?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;/xf:repeat&gt;&lt;/xf:group&gt;&lt;/xf:group&gt;&lt;xf:group id='2'&gt;&lt;xf:label&gt;MSA2: Mother Screening Assessment 2 - 1&lt;/xf:label&gt;&lt;xf:input bind='I_MSA2_INIT'&gt;&lt;xf:label&gt;Fill in your initials (Given Name (G), Surname (S))&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA2_FROMD'&gt;&lt;xf:label&gt;Date of form&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA2_IDV'&gt;&lt;xf:label&gt;Patient ID&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA2_MSA2_INITBF'&gt;&lt;xf:label&gt;Inclusion/Exclusion criteria - Have you initiated breastfeeding?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_INTBF'&gt;&lt;xf:label&gt;If not, do you intend to breastfeed your infant?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='3'&gt;&lt;xf:label&gt;Unsure&lt;/xf:label&gt;&lt;xf:value&gt;3&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_HAART'&gt;&lt;xf:label&gt;Is the mother currently on HAART?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_ELHAART'&gt;&lt;xf:label&gt;If not, is the mother eligible for HAART?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_PAZT'&gt;&lt;xf:label&gt;If yes specify which regimen - AZT&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA2_MSA2_PAZTNB'&gt;&lt;xf:label&gt;If yes, for how many weeks?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA2_MSA2_POART'&gt;&lt;xf:label&gt;Other ART&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_LNVPV'&gt;&lt;xf:label&gt;Single-dose nevirapine&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_LAZTV'&gt;&lt;xf:label&gt;AZT&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_L3TC'&gt;&lt;xf:label&gt;3TC&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_LOART'&gt;&lt;xf:label&gt;Other ART&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_LOCATB'&gt;&lt;xf:label&gt;Mother delivery - Where did you give birth to your baby?&lt;/xf:label&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Health care centre&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='2'&gt;&lt;xf:label&gt;Home assisted&lt;/xf:label&gt;&lt;xf:value&gt;2&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='3'&gt;&lt;xf:label&gt;Home non assisted&lt;/xf:label&gt;&lt;xf:value&gt;3&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='4'&gt;&lt;xf:label&gt;Other&lt;/xf:label&gt;&lt;xf:value&gt;4&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA2_MSA2_OLOCATBPREC'&gt;&lt;xf:label&gt;If other, specify&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:select1 bind='I_MSA2_MSA2_TRANSF'&gt;&lt;xf:label&gt;Did you receive a blood transfusion?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_TYPED'&gt;&lt;xf:label&gt;What kind of delivery did you have with this baby?&lt;/xf:label&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Vaginal&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='2'&gt;&lt;xf:label&gt;Vacuum&lt;/xf:label&gt;&lt;xf:value&gt;2&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='3'&gt;&lt;xf:label&gt;Forceps&lt;/xf:label&gt;&lt;xf:value&gt;3&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='4'&gt;&lt;xf:label&gt;Elective-caesarean section&lt;/xf:label&gt;&lt;xf:value&gt;4&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='5'&gt;&lt;xf:label&gt;Emergency caesarean section&lt;/xf:label&gt;&lt;xf:value&gt;5&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='6'&gt;&lt;xf:label&gt;Other&lt;/xf:label&gt;&lt;xf:value&gt;6&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_PMTCTV'&gt;&lt;xf:label&gt;Has the mother received any ART during pregnancy OR labour?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:select1 bind='I_MSA2_MSA2_PMTCT'&gt;&lt;xf:label&gt;Has the mother received any ART during pregnancy and labour?&lt;/xf:label&gt;&lt;xf:item id='0'&gt;&lt;xf:label&gt;No&lt;/xf:label&gt;&lt;xf:value&gt;0&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;xf:item id='1'&gt;&lt;xf:label&gt;Yes&lt;/xf:label&gt;&lt;xf:value&gt;1&lt;/xf:value&gt;&lt;/xf:item&gt;&lt;/xf:select1&gt;&lt;xf:input bind='I_MSA2_MSA2_LAZTNBV'&gt;&lt;xf:label&gt;If yes, for how many days?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;xf:input bind='I_MSA2_MSA2_L3TCNB'&gt;&lt;xf:label&gt;If yes, for how many days?&lt;/xf:label&gt;&lt;/xf:input&gt;&lt;/xf:group&gt;&lt;/xf:xforms&gt;</xform>
							    </version>
							  </form>
							</study>"""
}
