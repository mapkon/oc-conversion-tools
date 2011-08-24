package org.openxdata.oc.convert;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Test;
import org.openxdata.server.admin.model.Editable;
import org.xml.sax.SAXException;
public class ODMImportTest {
	
	@Test
	public void testImportItemReturnsValidEditable() throws SAXException, IOException {
		String xml = "<hei/>";
		ODMImport imp = new ODMImport();
		Editable e = imp.importStudyItem(xml);
		assertNotNull(e);
	}
}
