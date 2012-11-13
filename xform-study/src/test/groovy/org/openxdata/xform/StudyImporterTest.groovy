package org.openxdata.xform

import org.junit.Before
import org.junit.Test


class StudyImporterTest extends GroovyTestCase {

	def xml
	def study
	def forms
	def importer

	@Before void setUp(){
		
		xml = new XmlSlurper().parseText(xmlString)

		importer = new StudyImporter(xml)
		study = importer.extractStudy()
		
		forms = study.getForms()
	}

	@Test void testImportStudyDoesNotReturnNull(){

		assertNotNull study
	}
	
	@Test void testImportStudyReturnsANewStudy(){
		assertTrue study.isNew()
	}

	@Test void testImportStudyReturnsValidStudyWithName(){

		assertEquals 'test', study.getName()
	}

	@Test void testImportStudyReturnsValidStudyWithNameOnToString(){

		assertEquals 'test', study.toString()
	}

	@Test void testImportStudyReturnsValidStudyWithDescription(){

		assertEquals 'Test Study', study.getDescription()
	}

	@Test void testImportStudyReturnsValidStudyWithStudyKey(){

		assertEquals 'Test Key', study.getStudyKey()
	}

	@Test void testImportStudyReturnsValidStudyWithFormElement(){

		assertNotNull forms
	}

	@Test void testImportStudyReturnsValidStudyWithCorrectNumberOfForms(){

		assertEquals 2, forms.size()
	}

	@Test void testImportStudyReturnsValidStudyWithCorrectFormName(){

		def form = study.getForm('Test Form')

		assertEquals 'Test Form', form.getName()

	}
	
	@Test void testImportStudyReturnsValidStudyWithCorrectFormName2(){

		def form1 = study.getForm('Test Form 1')

		assertEquals 'Test Form 1', form1.getName()
	}

	@Test void testImportStudyReturnsValidStudyWithCorrectFormDescription(){

		def form = study.getForm('Test Form')

		assertEquals 'Test Description', form.getDescription()
		
	}
	
	@Test void testImportStudyReturnsValidStudyWithCorrectFormDescription2(){

		def form1 = study.getForm('Test Form 1')

		assertEquals 'Test Description 1', form1.getDescription()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersion() {
		
		forms.each{
			assertNotNull it.getVersions()
		}
	}
	
	@Test void testImportStudyReturnsValidStudyWithCorrectNumberOfFormVersionsForEachForm() {
		
		forms.each{
			assertEquals 2, it.getVersions().size()
		}
	}
	
	@Test void testImportStudyReturnsValidStudyWithCorrectFormVersion() {
				
		def form1 = forms[0]
		
		assertEquals 'Test Version', form1.getVersion('Test Version').getName()
		assertEquals 'Test Version 1', form1.getVersion('Test Version 1').getName()
		
	}
	
	@Test void testImportStudyReturnsValidStudyWithCorrectFormVersion2() {

		def form2 = forms[1]
		assertEquals 'Test Version 2', form2.getVersion('Test Version 2').getName()
		assertEquals 'Test Version 3', form2.getVersion('Test Version 3').getName()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormsHavingDefaultFormVersion() {
				
		def form1 = forms[0]
		
		assertEquals  'Test Version', form1.getDefaultVersion().getName()
		
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormsHavingDefaultFormVersion2() {

		def form2 = forms[1]

		assertEquals  'Test Version 2', form2.getDefaultVersion().getName()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXform() {
		
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version')
		
		assertNotNull version.getXform()
	}
	
	@Test void testImportStudyReturnsValidStudyWithValidXformElement() {
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version')
		
		def xform = version.getXform()
		
		def xformText = new XmlSlurper().parseText(xform)
		
		assertEquals 'xforms', xformText.name()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXform2() {
		
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version 1')
		
		assertNotNull version.getXform()
	}
	
	@Test void testImportStudyReturnsValidStudyWithValidXformElement2() {
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version 1')
		
		def xform = version.getXform()
		
		def xformText = new XmlSlurper().parseText(xform)
		
		assertEquals 'xforms', xformText.name()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXform3() {
		
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 2')
		
		assertNotNull version.getXform()
	}
	
	@Test void testImportStudyReturnsValidStudyWithValidXformElement3() {
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 2')
		
		def xform = version.getXform()
		
		def xformText = new XmlSlurper().parseText(xform)
		
		assertEquals 'xforms', xformText.name()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXform4() {
		
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 3')
		
		assertNotNull version.getXform()
	}
	
	@Test void testImportStudyReturnsValidStudyWithValidXformElement4() {
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 3')
		
		def xform = version.getXform()
		
		def xformText = new XmlSlurper().parseText(xform)
		
		assertEquals 'xforms', xformText.name()
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXformText() {
		
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version')
		def versionText = version.getXform()
		
		assertTrue versionText instanceof String
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXformText2() {
		
		def form = study.getForm('Test Form')
		
		def version = form.getVersion('Test Version 1')
		def versionText = version.getXform()
		
		assertTrue versionText instanceof String
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXformText3() {
		
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 2')
		def versionText = version.getXform()
		
		assertTrue versionText instanceof String
	}
	
	@Test void testImportStudyReturnsValidStudyWithFormVersionHavingXformText4() {
		
		def form = study.getForm('Test Form 1')
		
		def version = form.getVersion('Test Version 3')
		def versionText = version.getXform()
		
		assertTrue versionText instanceof String
	}
	
	def xmlString = '''<study name='test' description='Test Study' studyKey='Test Key'>
						<form name='Test Form' description='Test Description'>
						 <version name='Test Version'>
							  <xform>
								&lt;xf:xforms xmlns:xf=&quot;http://www.w3.org/2002/xforms&quot;
								xmlns:xsd=&quot;http://www.w3.org/2001/XMLSchema&quot;&gt;&#10;
									&lt;xf:model&gt;&#10;
										&lt;xf:instance id=&quot;test&quot;&gt;&#10;&lt;/xf:instance&gt;&#10;
									&lt;/xf:model&gt;
									&lt;/xf:xforms&gt;
							</xform>
						 </version>
						 <version name='Test Version 1'>
							  <xform>
								&lt;xf:xforms xmlns:xf=&quot;http://www.w3.org/2002/xforms&quot;
								xmlns:xsd=&quot;http://www.w3.org/2001/XMLSchema&quot;&gt;&#10;
									&lt;xf:model&gt;&#10;
										&lt;xf:instance id=&quot;test&quot;&gt;&#10;&lt;/xf:instance&gt;
									&lt;/xf:model&gt;
									&lt;/xf:xforms&gt;
							</xform>
						 </version>
						</form>
						<form name='Test Form 1' description='Test Description 1'>
						 <version name='Test Version 2'>
							<xform>
								&lt;xf:xforms xmlns:xf=&quot;http://www.w3.org/2002/xforms&quot;
								xmlns:xsd=&quot;http://www.w3.org/2001/XMLSchema&quot;&gt;&#10;
									&lt;xf:model&gt;&#10;
										&lt;xf:instance id=&quot;test&quot;&gt;&#10;&lt;/xf:instance&gt;
									&lt;/xf:model&gt;
									&lt;/xf:xforms&gt;
							</xform>
						 </version>
						 <version name='Test Version 3'>
							<xform>
								&lt;xf:xforms xmlns:xf=&quot;http://www.w3.org/2002/xforms&quot;
								xmlns:xsd=&quot;http://www.w3.org/2001/XMLSchema&quot;&gt;&#10;
									&lt;xf:model&gt;&#10;
										&lt;xf:instance id=&quot;test&quot;&gt;&#10;&lt;/xf:instance&gt;
									&lt;/xf:model&gt;
									&lt;/xf:xforms&gt;
							</xform>
						 </version>
						</form>
					  </study>'''
}
