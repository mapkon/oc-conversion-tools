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
		assertEquals "v1.0.0", xml.ClinicalData.@MetaDataVersion[0]
	}
	
	@Test void testAppendInstanceReturnsCorrectNumberOfItemDatas(){
		
		
		def xml = new XmlParser().parseText(exportedInstanceData)
		
		assertEquals "ItemData Nodes should equal number of child elements in the oxd instance data xml (including child elements of repeats)", 30, xml.depthFirst().ItemData.size()
	}
	
	@Test void testAppendInstanceDataShouldThrowExceptionOnNullInstanceData(){
		
		def emptyInstanceData = new ArrayList<String>()
		shouldFail(ImportException.class){
			new ODMInstanceDataDefinition().appendInstanceData(emptyInstanceData)
		}
	}
}
