package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException


class ODMDefinitionTest extends GroovyTestCase {

	def exportedInstanceData
	def instanceData = new ArrayList<String>()
	
	public void setUp(){
		
		def odmDef = new ODMDefinition()
		exportedInstanceData = odmDef.appendInstanceData(TestData.getInstanceData())
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
