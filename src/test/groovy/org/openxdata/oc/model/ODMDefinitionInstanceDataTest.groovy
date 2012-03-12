package org.openxdata.oc.model

import org.junit.Test
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException


class ODMDefinitionInstanceDataTest extends GroovyTestCase {

	def exportedInstanceData
	
	public void setUp(){
		
		def odmDef = new ODMInstanceDataDefinition()
		exportedInstanceData = odmDef.appendInstanceData(TestData.getOpenXdataInstanceData())
	}
	
	@Test void testAppendInstanceDataShouldConvertedOpenXDataInstanceDataUsingCorrectProtocol(){
		def xml = new XmlParser().parseText(exportedInstanceData)
		
		assertNotNull xml
	}
	
	@Test void testInstanceDataHasMetaDataVersionOID() {
		
		def xml = new XmlParser().parseText(exportedInstanceData)
		assertEquals "v1.0.0", xml.@MetaDataVersion
	}
	
	@Test void testAppendInstanceDataAppendsTheInstanceData(){
		
		
		def xml = new XmlParser().parseText(exportedInstanceData)
		
		assertEquals xml.depthFirst().ItemData.size(), 26
	}
	
	@Test void testAppendInstanceDataShouldThrowExceptionOnNullInstanceData(){
		def emptyInstanceData = new ArrayList<String>()
		shouldFail(ImportException.class){
			new ODMInstanceDataDefinition().appendInstanceData(emptyInstanceData)
		}
	}
}
