package org.openxdata.oc


import org.junit.Before
import org.junit.Test
import org.openxdata.oc.InstanceDataHandler
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.util.TransformUtil;


class InstanceDataHandlerTest extends GroovyTestCase {

	def handler
	List<String> exportedInstanceData
	
	@Before public void setUp(){

		handler = new InstanceDataHandler()

		exportedInstanceData = handler.processInstanceData(TestData.getInstanceData())
	}

	@Test void testAppendInstanceDataShouldConvertedOpenXDataInstanceDataUsingCorrectProtocol(){

		def xml = new XmlParser().parseText(exportedInstanceData[0])

		assertNotNull xml
	}

	@Test void testAppendInstacenDataReturnsXmlWithODMAsRootElement() {

		def xml = new XmlParser().parseText(exportedInstanceData[0])

		assertEquals "Root should be ODM", "ODM", xml.name()
	}

	@Test void testAppendInstacenDataReturnsXmlWithClinicalDataElement() {

		def xml = new XmlParser().parseText(exportedInstanceData[0])

		assertEquals "Second Node should be ClinicalData", "ClinicalData", xml.children()[0].name()
	}

	@Test void testInstanceDataHasMetaDataVersionOID() {

		def xml = new XmlParser().parseText(exportedInstanceData[0])
		assertEquals "v1.0.0", xml.ClinicalData.@MetaDataVersion[0]
	}

	@Test void testAppendInstanceReturnsCorrectNumberOfItemDatas(){


		def xml = new XmlParser().parseText(exportedInstanceData[0])

		assertEquals "ItemData Nodes should equal number of child elements in the oxd instance data xml (including child elements of repeats)", 31, xml.depthFirst().ItemData.size()
	}

	@Test void testAppendInstanceDataShouldThrowExceptionOnNullInstanceData(){

		def emptyInstanceData = new ArrayList<String>()
		shouldFail(ImportException.class){
			new InstanceDataHandler().processInstanceData(emptyInstanceData)
		}
	}

	@Test void testThatUncleanedXmlHasHeaders() {
		
		def xmlWithHeaders = new TransformUtil().loadFileContents("data.xml")
		
		def dXml = new XmlSlurper().parseText(xmlWithHeaders)
		
		assertTrue "Xml has Headers", hasHeaders(dXml)
	}
	
	@Test void testThatInstanceXmlWithHeaderQuestionsIsCleaned() {

		def xmlWithHeaders = new TransformUtil().loadFileContents("data.xml")
		
		def xml = handler.cleanXml(xmlWithHeaders)
		def cleanXml = new XmlSlurper().parseText(xml)

		assertFalse "Xml Has no Headers", hasHeaders(cleanXml)
	}
	
	private def hasHeaders(xml) {

		for(def node : xml.children())
			if(node.name().endsWith("_HEADER")) {
				return true
			}

		return false
	}
}
