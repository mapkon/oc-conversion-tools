package org.openxdata.oc.transform;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.TimestampFormatterExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;
import org.openxdata.oc.service.OpenclinicaService;
import org.openxdata.oc.service.impl.OpenclinicaServiceImpl;
import org.openxdata.server.admin.model.StudyDef;

@RunWith(ConcordionRunner.class)
@Extensions(TimestampFormatterExtension.class)
public class TransformationFixture {

	private StudyDef getStudy() {
		OpenclinicaService openclinicaService = new OpenclinicaServiceImpl();
		
		StudyDef study = openclinicaService.importOpenClinicaStudy("S_DEFAULTS1");
		
		return study;
	}
	
	public String getStudyName() {
		
		return getStudy().getName();
	}
	
	public String getStudyKey() {
		
		return getStudy().getStudyKey();
	}
	
	public int getForms() {
		
		return getStudy().getForms().size();
	}
	
	public String getFormName(String formName) {

		return getStudy().getForm(formName).getName();
	}
}
