package org.openxdata.oc.util

import org.junit.Test


class TransformUtilTest extends GroovyTestCase {

	def xml
	def odmFileContent

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

		odmFileContent = util.loadFileContents('test-odm.xml')
	}

	@Test void testLoadFileContentsDoesNotReturnNull(){
		assertNotNull odmFileContent
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectStartingTag(){

		assertTrue odmFileContent.contains('<ODM')
	}

	@Test void testLoadFileContentsReturnsValidODMFileWithCorrectEndingTag(){

		assertTrue odmFileContent.endsWith('</ODM>')
	}

	@Test void testLoadFileContentsStartWithXmlProcessingInstructions(){
		assertTrue odmFileContent.startsWith('''<?xml version="1.0" encoding="UTF-8"?>''')
	}

	@Test void testLoadFileContentsMUSTThrowExceptionOnNullOrEmptyFileName(){
		shouldFail(IllegalArgumentException.class){
			def file = util.loadFileContents('')
		}
	}

	@Test void testLoadFileContentsRendersCorrectMessageOnEmptyFileName(){
		try{
			util.loadFileContents('')
		}catch(def ex){
			assertEquals 'File name cannot be null or empty.', ex.getMessage()
		}
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

		def node = new XmlSlurper().parseText("<item/>")

		assertFalse util.isRepeat("", node)
	}
}
