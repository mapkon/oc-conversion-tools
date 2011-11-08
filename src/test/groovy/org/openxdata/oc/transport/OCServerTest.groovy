package org.openxdata.oc.transport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openxdata.oc.model.ConvertedOpenclinicaStudy;
import org.openxdata.oc.transport.OpenClinicaSoapClient;
import org.openxdata.oc.transport.factory.ConnectionFactory;
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl;

@Ignore("Not intended to be run during standard build because it is dependent on existing openclinica installation.")
public class OCServerTest {

	def client
	
	@Before
	public void setUp(){
		def factory = new ConnectionFactory("http://158.37.6.164/OpenClinica-ws-3.1.1")
		client = new OpenClinicaSoapClientImpl("MarkG", "b9a60a9d91a96ee522d0c942e5b88dfba25b0a12")
		client.setConnectionFactory(factory)
	}
	
	@Test
	public void listAll() {
		List<ConvertedOpenclinicaStudy> studies = client.listAll()
		
		assertNotNull(studies)
		assertEquals(1, studies.size())
	}
	
	@Test
	public void testGetSubjects() {
		def subjects = client.getSubjectKeys("default-study")
		assertNotNull(subjects)
		assertEquals(82, subjects.size())
	}
	
	@Test
	public void testGetOpenXdataForm() {
		
		Collection<String> subjectKeys = client.getSubjectKeys("default-study")
		String xml = client.getOpenxdataForm("default-study", subjectKeys)
		
		assertNotNull(subjectKeys)
		assertNotNull(xml)
	}
	
	@Test
	public void testGetMetaData() {
		String studies = client.getMetadata("default-study")		
		
		assertNotNull(studies)
	}
}
