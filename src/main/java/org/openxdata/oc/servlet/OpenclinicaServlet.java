package org.openxdata.oc.servlet;

import java.io.ByteArrayInputStream;
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
import org.openxdata.oc.util.TransformUtil;
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
		
	private static final String IMPORT = "Import";
	private static final String EXPORT = "Export";
	
	private FormService formService;
	private StudyManagerService studyService;
	private Properties props = new Properties();
	
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
    	
    	try {
    		
			log.debug("Attempting to load properties file from Web Context");
			InputStream propFileStream = getServletContext().getResourceAsStream("openclinica.properties");
			props.load(propFileStream);
			
		} catch (Exception e) {
			
			loadInternalProperties();
		}
    	
		return props;
	}

	private void loadInternalProperties() {
		
		try {
			
			log.debug("File not found in Web context. Attempting to load internal properties file");
			
			String propFileContent = new TransformUtil().loadFileContents("META-INF/openclinica.properties");
			props.load(new ByteArrayInputStream(propFileContent.getBytes()));
			
		} catch (IOException e) {
			log.debug(e.getLocalizedMessage());
		}
	}

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    	
    	try {
			request.getRequestDispatcher("openclinica.jsp").forward(request, response);
		} catch (ServletException e) {
			log.info(e.getLocalizedMessage());
		} catch (IOException e) {
			log.info(e.getLocalizedMessage());
		}
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    	
    	StudyDef study = null;
    	String oid = request.getParameter("oid");
    	String action = request.getParameter("action");

    	User user = authenticate();

    	if(IMPORT.equals(action)) {
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
				
				log.info("Saving existing [converted] Study: " + existingStudy.getName());

				studyService.saveStudy(existingStudy);

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
			if (x.getFormDef().getName().equals(version.getFormDef().getName())) {
				incrementAndMakeDefault(x, version.getFormDef());
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

	private void incrementAndMakeDefault(FormDefVersion versionToIncrement, FormDef form) {
		
		log.info("Incrementing to latest Version: " + versionToIncrement.getName());

		String incrementVersionName = versionToIncrement.getName();
		String nextVersion = form.getNextVersionName();
		
		String newVersionName = incrementVersionName.
				replace(incrementVersionName.substring(incrementVersionName.length() -  2), nextVersion);
		
		versionToIncrement.setName(newVersionName);
		
		versionToIncrement.setFormDef(form);
		form.addVersion(versionToIncrement);
		
		form.turnOffOtherDefaults(versionToIncrement);
		form.setDirty(true);
		
	}
}
