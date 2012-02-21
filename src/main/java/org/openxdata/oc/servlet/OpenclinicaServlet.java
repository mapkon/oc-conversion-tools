package org.openxdata.oc.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.service.OpenclinicaService;
import org.openxdata.oc.service.impl.OpenclinicaServiceImpl;
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
    	
    	log.info("Fetching study for oid: " + oid);
    	if(DOWNLOAD_AND_CONVERT.equals(action)) {
    		study = fetchAndSaveStudy(oid);
    	}
    	
    	request.getSession().setAttribute("study", study);
    }
    
	private StudyDef fetchAndSaveStudy(String oid) {
		
		StudyDef study = openclinicaService.importOpenClinicaStudy(oid);
		studyService.saveStudy(study);
		
		return study;
	}
}
