package org.openxdata.oc.convert;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;
import org.openxdata.oc.convert.exception.InvalidXMLException;
import org.openxdata.oc.convert.util.XMLUtil;
import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.transforms.TransformationException;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XSLTXMLConversionTest {
	
	@Test
	public void test() throws IOException, InvalidXMLException, TransformationException {
		Init.init();
		String xslt = XMLUtil.loadFile("/org/openxdata/oc/oc-oxd.xsl");
		XSLTCompiler compiler = new XSLTCompiler(xslt);		
		String odm = XMLUtil.loadFile("/org/openxdata/oc/oc-odm.xml");
		Document doc = compiler.transform(odm);
		
		System.out.println(format(doc));
	}
	
	public String format(Document document) {
        try {
            OutputFormat format = new OutputFormat(document);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
