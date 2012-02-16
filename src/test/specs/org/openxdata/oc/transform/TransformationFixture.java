package org.openxdata.oc.transform;

import groovy.util.slurpersupport.NodeChild;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.LoggingTooltipExtension;
import org.concordion.ext.TimestampFormatterExtension;
import org.concordion.integration.junit3.ConcordionTestCase;
import org.openxdata.oc.Transform;
import org.openxdata.oc.util.TransformUtil;

@Extensions({TimestampFormatterExtension.class, LoggingTooltipExtension.class})
public class TransformationFixture extends ConcordionTestCase {

	public String getStudyName() {
		
		TransformUtil util = new TransformUtil();
		Object odmFileStream = (String) util.loadFileContents("test-odm.xml");
		
		Transform transformer = Transform.getTransformer();
		NodeChild convertedXform = (NodeChild) transformer.ConvertODMToXform(odmFileStream);
		
		String name = util.getStudyName(convertedXform);
		
		return name;
	}
}
