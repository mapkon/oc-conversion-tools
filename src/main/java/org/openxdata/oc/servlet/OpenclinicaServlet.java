package org.openxdata.oc.servlet;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.service.OpenclinicaService;
import org.openxdata.oc.service.impl.OpenclinicaServiceImpl;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.FormService;
import org.openxdata.server.service.StudyManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OpenclinicaServlet extends HttpServlet {

	private static final long serialVersionUID = 6577932874016086164L;
		
	private static final String DOWNLOAD_AND_CONVERT = "downloadAndConvert";
	
	private FormService formService;
	private StudyManagerService studyService;
	
	private OpenclinicaService openclinicaService;
	
	private static final Logger log = LoggerFactory.getLogger(OpenclinicaServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);

		formService = (FormService) ctx.getBean("formService");
		studyService = (StudyManagerService) ctx.getBean("studyManagerService");

		openclinicaService = new OpenclinicaServiceImpl();

		openclinicaService.setStudyService(studyService);
		openclinicaService.setFormService(formService);

	}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    	
    	StudyDef study = null;
    	String oid = request.getParameter("oid");
    	String action = request.getParameter("action");
    	
    	if(DOWNLOAD_AND_CONVERT.equals(action)) {
    		study = fetchAndSaveStudy(oid);
    	}
    	
    	request.getSession().setAttribute("study", study);
    }
    
	private StudyDef fetchAndSaveStudy(String oid) {
		
		log.info("Fetching study for oid: " + oid);
		StudyDef study = openclinicaService.importOpenClinicaStudy(oid);
		study = validateAndSaveStudy(study);
		
		return study;
	}

	private StudyDef validateAndSaveStudy(StudyDef study) {

		if (studyService != null) {
			
			log.info("Validating Converted Study: " + study.getName());
			StudyDef existingStudy = studyService.getStudyByKey(study.getStudyKey());
			
			if (existingStudy != null) {
				
				List<FormDefVersion> existingStudyFormVersions = getStudyFormVersions(existingStudy);

				for (FormDefVersion version : existingStudyFormVersions) {
					inspectStudyFormVersions(version, study);
				}

			}
			
			studyService.saveStudy(study);
		}

		return study;
	}

	private void inspectStudyFormVersions(FormDefVersion version, StudyDef study) {

		log.info("Inspect for conflicting Study Form Versions: " + study.getName());

		List<FormDefVersion> studyFormVersions = getStudyFormVersions(study);
		for (FormDefVersion x : studyFormVersions) {
			if (x.getName().equals(version.getName())) {
				incrementAndMakeDefault(x, version);
			}
		}
	}
	
	private List<FormDefVersion> getStudyFormVersions(StudyDef study) {
		List<FormDefVersion> versions = new ArrayList<FormDefVersion>();
		for(FormDef form : study.getForms()) {
			versions.add(form.getDefaultVersion());
		}
		return versions;
	}

	private void incrementAndMakeDefault(FormDefVersion versionToIncrement, FormDefVersion version) {
		
		log.info("Incrementing the latest Version: " + versionToIncrement.getName());

		String versionName = version.getName();
		String versionNumber = versionName.substring(versionName.length() - 1);
		
		int number = Integer.valueOf(versionNumber) + 1;
		
		String newVersionName = versionToIncrement.getName();
		String newVersionNumber = newVersionName.substring(newVersionName.length() - 1);
		
		newVersionName.replace(newVersionNumber, number+"");
		
		versionToIncrement.setName(newVersionName.trim());
		
		FormDef form = version.getFormDef();
		form.addVersion(versionToIncrement);
		
		form.turnOffOtherDefaults(versionToIncrement);
		
	}
}
