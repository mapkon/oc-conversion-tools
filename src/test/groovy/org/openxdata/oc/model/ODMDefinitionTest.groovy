package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException


class ODMDefinitionTest extends GroovyTestCase {

	def instanceData = []
	def exportedInstanceData
	
	public void setUp(){
		
		def odmDef = new ODMDefinition()
		exportedInstanceData = odmDef.appendInstanceData(TestData.getInstanceData())
	}
	
	@Test void testInstanceDataHasMetaDataVersionOID() {
		
		def instanceData = TestData.getOpenXdataInstanceData()
		instanceData.each {
			
			def xml = new XmlParser().parseText(it)
			assertEquals "v1.0.0", xml.@MetaDataVersionOID
		}
	}
	
	@Test void testAppendInstanceDataDoesNotReturnNull() {
				
		assertNotNull exportedInstanceData
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
