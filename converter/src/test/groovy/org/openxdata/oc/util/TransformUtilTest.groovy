package org.openxdata.oc.util

import groovy.util.GroovyTestCase
import groovy.util.XmlSlurper

import org.junit.Test

class TransformUtilTest extends GroovyTestCase {

	def xml
	def odmFileContent
	def xmlWithHeaders

	def util = new TransformUtil()

	public void setUp() {

		def stream = """<root>
							<item>foo</item>
							<item2>
								<child>1</child>
								<child1>2</child1>
							</item2>
						</root>"""


		xml = new XmlSlurper().parseText(stream)

		xmlWithHeaders = util.loadFileContents("data.xml")

		odmFileContent = util.loadFileContents('test-odm.xml')
	}

	@Test void testLoadFileContentsDoesNotReturnNull() {
		
		assertNotNull odmFileContent
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectStartingTag(){

		assertTrue odmFileContent.contains('<ODM')
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectEndingTag(){

		assertTrue odmFileContent.endsWith('</ODM>')
	}

	@Test void testLoadFileContentsStartWithXmlProcessingInstructions() {
		
		assert odmFileContent =~ /^<\?xml\s+version/
	}

	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException){
			def file = util.loadFileContents('')
		}
	}

	@Test void testLoadFileContentsRendersCorrectMessageOnEmptyFileName(){
		
		def msg = shouldFail(IllegalArgumentException) {
			util.loadFileContents('')
		}
		
		assertEquals 'File name cannot be null or empty.', msg
	}

	@Test void testIsRepeatReturnsFalseWhenItemIsRepeat() {

		assertFalse util.isRepeat(xml, "item")
	}

	@Test void testIsRepeatReturnsTrueWhenItemIsRepeat() {

		assertTrue util.isRepeat(xml, "item2")
	}

	@Test void testThatIsRepeatReturnsTrueWhenPassedJustAnXMLNode() {

		def node = xml.item2

		assertTrue util.isRepeat("", node)
	}

	@Test void testThatIsRepeatReturnsFalseWhenPassedJustAnXMLNodeThatIsNotARepeat() {

		def node = new XmlSlurper().parseText("<item>1</item>")

		assertFalse util.isRepeat("", node)
	}

	@Test void testThatIsolateRepeatsMakesRepeatsUnique() {


		// Isolate repeating groups so protocol does not ignore them
		def uniqueXml = util.isolateRepeats(xmlWithHeaders)

		def xml = new XmlSlurper().parseText(uniqueXml)

		xml.children().each {
			if(util.isRepeat("", it)) {
				assertNotNull it.@repeatKey
			}
		}
	}

	@Test void testThatIsolateRepeatsAddsCorrectRepeakKeys(){

		// Isolate repeating groups so protocol does not ignore them
		def uniqueXml = util.isolateRepeats(xmlWithHeaders)

		def xml = new XmlSlurper().parseText(uniqueXml)

		def repeats = xml.depthFirst().findAll {
			it.name().equals("IG_CLLC_CLL_OTESTG")
		}

		repeats.eachWithIndex { rpt, idx ->

			// Repeat keys are added in incremental order
			assertEquals rpt.@repeatKey, idx.toString()
		}
	}

	@Test void testThatIsolateRepeatsAddsCorrectRepeakKeyNumber(){

		// Isolate repeating groups so protocol does not ignore them
		def uniqueXml = util.isolateRepeats(xmlWithHeaders)

		def xml = new XmlSlurper().parseText(uniqueXml)

		def repeatKeys = xml.depthFirst().findAll {
			it.name().equals("IG_CLLC_CLL_OTESTG") && it.@repeatKey != null
		}

		assertEquals 4, repeatKeys.size()
	}
}
