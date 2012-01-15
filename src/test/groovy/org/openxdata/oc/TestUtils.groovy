package org.openxdata.oc

import org.openxdata.oc.util.TransformUtil

class TestUtils {
	
	static def getReturnXml() {
		def response = new TransformUtil().loadFileContents('CRFRequestResponse.xml')
	}
}
