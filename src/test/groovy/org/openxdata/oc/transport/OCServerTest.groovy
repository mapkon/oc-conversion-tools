package org.openxdata.oc.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import groovy.xml.XmlUtil;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openxdata.oc.model.ConvertedOpenclinicaStudy;
import org.openxdata.oc.transport.OpenClinicaSoapClient;
import org.openxdata.oc.transport.factory.ConnectionFactory;
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl;

//@Ignore("Not intended to be run during standard build because it is dependent on existing openclinica installation.")
public class OCServerTest {

	def client
	
	@Before
	public void setUp(){
		def factory = new ConnectionFactory("http://158.37.6.164/OpenClinica-ws")
		client = new OpenClinicaSoapClientImpl("MarkG", "b9a60a9d91a96ee522d0c942e5b88dfba25b0a12", factory)
	}
	
	@Test
	public void listAllDoesNotReturnNull() {
		List<ConvertedOpenclinicaStudy> studies = client.listAll()
		
		assertNotNull studies
	}
	
	@Test
	public void listAllReturns1Study() {
		List<ConvertedOpenclinicaStudy> studies = client.listAll()
		
		assertEquals 1, studies.size()
	}
	
	@Test
	public void testGetSubjectsDoesNotReturnNull() {
		def subjects = client.getSubjectKeys("default-study")
		
		assertNotNull subjects
	}
	
	@Test
	public void testGetSubjectsReturnsCorrectNumberOfSubjects() {
		def subjects = client.getSubjectKeys("default-study")
		
		assertEquals 82, subjects.size()
	}
	
	@Test
	public void testGetOpenXdataFormDoesNotReturnNull() {
		
		String convertedXml = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(convertedXml)
		
		assertNotNull xml		
	}
	
	@Test
	public void testGetOpenXdataFormReturnsValidStudyRoot() {
		
		String metadata = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(metadata)
		
		assertEquals 'study', xml.name()
				
	}
	
	@Test
	public void testGetOpenXdataFormReturnsValidStudyWithFormElement() {
		
		String metadata = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(metadata)
		
		def form = xml.form
		
		assertEquals 'form', form.name()
		
	}
	
	@Test
	public void testGetOpenXdataFormReturnsValidStudyWithVersionElement() {
		
		String metadata = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(metadata)
		
		def version = xml.form.version
		
		assertEquals 'version', version.name()
		
	}
	
	@Test
	public void testGetOpenXdataFormReturnsValidStudyWithXformElement() {
		
		String metadata = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(metadata)
		
		def xform = xml.form.version.xform
		
		assertEquals 'xform', xform.name()
		
	}
	
	@Test
	public void testGetOpenXdataFormReturnsValidStudyWithXformsElement() {
		
		String metadata = client.getOpenxdataForm("default-study")
		
		def xml = new XmlSlurper().parseText(metadata)
		
		def xforms = xml.form.version.xform.xforms
		
		assertEquals 'xforms', xforms.name()
		
	}
	
	@Test
	public void testGetMetaDataDoesNotReturnNull() {
		def metadata = client.getMetadata("default-study")		
		
		assertNotNull metadata
	}
	
	@Test
	public void testGetMetaDataReturnsValidODM() {
		def metadata = client.getMetadata("default-study")
		
		def metadataXml = new XmlSlurper().parseText(metadata)
		assertEquals 'ODM', metadataXml.name()
	}
	
	@Test
	public void testGetMetaDataReturnsValidODMWithStudy() {
		def metadata = client.getMetadata("default-study")
		
		def metadataXml = new XmlSlurper().parseText(metadata)
		
		def study = metadataXml.ODM.Study
		
		assertEquals 'Study', study.name()
	}
}
