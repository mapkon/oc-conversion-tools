package org.openxdata.oc.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.exception.OpenXdataDataAccessException;
import org.openxdata.server.service.AuthenticationService;
import org.openxdata.server.service.FormService;
import org.openxdata.server.service.StudyManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OpenclinicaServlet extends HttpServlet {

	private static final long serialVersionUID = 6577932874016086164L;
		
	private static final String DOWNLOAD_AND_CONVERT = "downloadAndConvert";
	
	private Properties props;
	private FormService formService;
	private StudyManagerService studyService;
	
	private OpenclinicaService openclinicaService;
	private AuthenticationService authenticationService;
	
	private static final Logger log = LoggerFactory.getLogger(OpenclinicaServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);

		formService = (FormService) ctx.getBean("formService");
		studyService = (StudyManagerService) ctx.getBean("studyManagerService");
		authenticationService = (AuthenticationService) ctx.getBean("authenticationService");
		
    	props = loadProperties();
    	
		openclinicaService = new OpenclinicaServiceImpl(props);

		openclinicaService.setStudyService(studyService);
		openclinicaService.setFormService(formService);

	}
	
    private Properties loadProperties() {
    	
    	Properties props = new Properties();
    	try {
    		
    		InputStream fileName = getServletContext().getResourceAsStream("openclinica.properties");
			props.load(fileName);
			
		} catch (IOException e) {
			log.error(e.getLocalizedMessage());
		}
    	
		return props;
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    	
    	StudyDef study = null;
    	String oid = request.getParameter("oid");
    	String action = request.getParameter("action");

    	User user = authenticate();

    	if(DOWNLOAD_AND_CONVERT.equals(action)) {
    		study = fetchAndSaveStudy(oid);
    	}
    	
    	request.getSession().setAttribute("study", study);
    	request.getSession().setAttribute("oxdUser", user);
    }
	
	private User authenticate() {
		
		User user = null;
		if(authenticationService != null) {
			
			String oxdUserName = props.getProperty("oxdUserName");
			String oxdPassword = props.getProperty("oxdPassword");
			
			log.info("Authenticating User: " + oxdUserName);

			user = authenticationService.authenticate(oxdUserName, oxdPassword);
			if(user == null)
	    		throw new OpenXdataDataAccessException("Access Denied");
			
		}
		return user;
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
				
				log.info("Saving Converted Study: " + study.getName());

				studyService.saveStudy(study);

			}else {
				
				log.info("First time download - Saving new converted study: " + study.getName());

				studyService.saveStudy(study);
			}
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
		
		log.info("Incrementing to latest Version: " + versionToIncrement.getName());

		String incrementVersionName = versionToIncrement.getName();
		String nextVersion = version.getFormDef().getNextVersionName();
		String newVersionName = incrementVersionName.replace(incrementVersionName.substring(incrementVersionName.length() -  2), nextVersion);
		
		versionToIncrement.setName(newVersionName);
		
		FormDef form = version.getFormDef();
		form.addVersion(versionToIncrement);
		
		form.turnOffOtherDefaults(versionToIncrement);
		form.setDirty(true);
		
	}
}
