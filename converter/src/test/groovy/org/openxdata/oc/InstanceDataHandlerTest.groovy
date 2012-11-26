package org.openxdata.oc


import org.junit.Before
import org.junit.Test
import org.openxdata.oc.InstanceDataHandler
import org.openxdata.oc.data.TestData
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.util.TransformUtil
import org.openxdata.server.admin.model.FormData

class InstanceDataHandlerTest extends GroovyTestCase {

	def handler
	List<String> data
	def xmlWithHeaders = new TransformUtil().loadFileContents("data.xml")

	@Before public void setUp(){

		handler = new InstanceDataHandler()

		data = handler.processInstanceData(TestData.getInstanceData())
	}

	@Test void testAppendInstanceDataShouldConvertedOpenXDataInstanceDataUsingCorrectProtocol(){

		def xml = new XmlParser().parseText(data[0])

		assertNotNull xml
	}

	@Test void testAppendInstacenDataReturnsXmlWithODMAsRootElement() {

		def xml = new XmlParser().parseText(data[0])

		assertEquals "Root should be ODM", "ODM", xml.name()
	}

	@Test void testAppendInstacenDataReturnsXmlWithClinicalDataElement() {

		def xml = new XmlParser().parseText(data[0])

		assertEquals "Second Node should be ClinicalData", "ClinicalData", xml.children()[0].name()
	}

	@Test void testInstanceDataHasMetaDataVersionOID() {

		def xml = new XmlParser().parseText(data[0])
		assertEquals "v1.0.0", xml.ClinicalData.@MetaDataVersion[0]
	}

	@Test void testAppendInstanceReturnsCorrectNumberOfItemDatas(){

		def xml = new XmlParser().parseText(data[0])

		assertEquals "ItemData Nodes should equal number of child elements in the oxd instance data xml (including child elements of repeats)", 33, xml.depthFirst().ItemData.size()
	}

	@Test void testAppendInstanceDataShouldThrowExceptionOnNullInstanceData(){

		def emptyInstanceData = new ArrayList<String>()
		shouldFail(ImportException.class){
			new InstanceDataHandler().processInstanceData(emptyInstanceData)
		}
	}
	
	@Test void testAppendInstanceDataShouldFailWithCorrectExceptionmessage() {
		
		def emptyInstanceData = new ArrayList<String>()
		def msg = shouldFail(ImportException.class){
			new InstanceDataHandler().processInstanceData(emptyInstanceData)
		}
		
		assertEquals 'Cannot process empty instance data.', msg
	}

	@Test void testThatUncleanedXmlHasHeaders() {

		def dXml = new XmlSlurper().parseText(xmlWithHeaders)

		assertTrue "Xml has Headers", hasHeaders(dXml)
	}

	@Test void testThatInstanceDataWithHeaderQuestionsIsCleaned() {

		// Remove headers/sub headers
		def xml = handler.cleanXml(xmlWithHeaders)

		def cleanXml = new XmlSlurper().parseText(xml)

		assertFalse "Xml Has no Headers", hasHeaders(cleanXml)
	}

	@Test void testThatOnlyTheErraticDataFailsToExport() {

		def formDataList = createFormData()
		def odmFormattedDataList = handler.processInstanceData(formDataList)

		assertEquals 1, odmFormattedDataList.size()
	}

	private def hasHeaders(xml) {

		for(def node : xml.children())
			if(node.name().endsWith("_HEADER")) {
				return true
			}

		return false
	}

	private def createFormData() {

		def list = []

		def formData = new FormData()
		formData.setId(1)
		formData.setData("""<ODM formKey="Foo_Key"><subjectkey>key</subjectkey><foo>1</foo><bar>roses</bar></ODM>""")

		// Invalid form data - should fail export
		def formData2 = new FormData()
		formData2.setId(2)
		formData2.setData("""<ODM><foo>fail</foo></ODM>""")

		// initialize
		list.add(formData)
		list.add(formData2)

		return list
	}
}
