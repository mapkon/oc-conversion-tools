package org.openxdata.oc.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.exception.ImportException;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.oc.service.impl.OpenClinicaServiceImpl;
import org.openxdata.oc.util.PropertiesUtil;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.FormDefVersion;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.admin.model.exception.OpenXdataDataAccessException;
import org.openxdata.server.service.AuthenticationService;
import org.openxdata.server.service.DataExportService;
import org.openxdata.server.service.FormService;
import org.openxdata.server.service.StudyManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OpenClinicaServlet extends HttpServlet {

	private static final long serialVersionUID = 6577932874016086164L;
	private final Logger log = LoggerFactory.getLogger(OpenClinicaServlet.class);

	private final String IMPORT = "Import";
	private final String EXPORT = "Export";

	private FormService formService;
	private StudyManagerService studyService;
	private Properties props = new Properties();
	private DataExportService dataExportService;
	private OpenClinicaService openclinicaService;
	private AuthenticationService authenticationService;
	private final String JSP_FILE_NAME = "openclinica.jsp";

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);

		ctx.getAutowireCapableBeanFactory().autowireBean(this);

		formService = (FormService) ctx.getBean("formService");
		studyService = (StudyManagerService) ctx.getBean("studyManagerService");
		dataExportService = (DataExportService) ctx.getBean("dataExportService");
		authenticationService = (AuthenticationService) ctx.getBean("authenticationService");

		props = new PropertiesUtil().loadOpenClinicaProperties(sctx);

		openclinicaService = new OpenClinicaServiceImpl(props);

		openclinicaService.setStudyService(studyService);
		openclinicaService.setFormService(formService);
		openclinicaService.setDataExportService(dataExportService);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, String> map = new HashMap<String, String>();
		try {

			User user = authenticate();

			StudyDef study = null;
			String oid = request.getParameter("oid");
			String action = request.getParameter("action");

			if (IMPORT.equals(action)) {

				// Load the oid from properties file
				if (oid == null || oid.length() == 0)
					oid = props.getProperty("studyOID");

				study = fetchAndSaveStudy(oid);

				map.put("", "OpenClinica Study with oid: " + study.getStudyKey() + " successfully converted and Imported");

				request.setAttribute("message", map);

				request.setAttribute("study", study);
				request.setAttribute("name", study.getName());
				request.setAttribute("key", study.getStudyKey());

			} else if (EXPORT.equals(action)) {

				HashMap<String, String> messages = exportStudyData();
				request.setAttribute("message", messages);
			}

			request.setAttribute("user", user);

			request.getRequestDispatcher(JSP_FILE_NAME).forward(request, response);

		} catch (Exception ex) {

			log.error(ex.getLocalizedMessage());
			request.setAttribute("message", ex.getLocalizedMessage());

			redirectToOpenClinicaPage(request, response);
		}
	}

	private void redirectToOpenClinicaPage(HttpServletRequest request, HttpServletResponse response) {
		try {

			response.sendRedirect(JSP_FILE_NAME);

		} catch (IOException ex) {
			log.error(ex.getLocalizedMessage());
		}
	}

	private User authenticate() {

		User user = null;
		if (authenticationService != null) {

			String oxdUserName = props.getProperty("oxdUserName");
			String oxdPassword = props.getProperty("oxdPassword");

			log.info("Authenticating User: " + oxdUserName);

			user = authenticationService.authenticate(oxdUserName, oxdPassword);
			if (user == null)
				throw new OpenXdataDataAccessException(
						"Access to OpenXData Services Denied. Check your login credentials.");

		}
		return user;
	}

	private HashMap<String, String> exportStudyData() throws ImportException {

		log.info("Initiating Export of Study Data to OpenClinica...");

		HashMap<String, String> exportMessages = openclinicaService.exportOpenClinicaStudyData();

		return exportMessages;
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

			} else {

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
		for (FormDef form : study.getForms()) {
			versions.add(form.getDefaultVersion());
		}
		return versions;
	}

	private void incrementAndMakeDefault(FormDefVersion versionToIncrement, FormDef form) {

		log.info("Incrementing to latest Version: " + versionToIncrement.getName());

		String incrementVersionName = versionToIncrement.getName();
		String nextVersion = form.getNextVersionName();

		String newVersionName = incrementVersionName.replace(
				incrementVersionName.substring(incrementVersionName.length() - 2), nextVersion);

		versionToIncrement.setName(newVersionName);

		versionToIncrement.setFormDef(form);
		form.addVersion(versionToIncrement);

		form.turnOffOtherDefaults(versionToIncrement);
		form.setDirty(true);

	}

}
