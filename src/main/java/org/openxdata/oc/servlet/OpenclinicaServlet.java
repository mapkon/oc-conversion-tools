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
import org.openxdata.server.dao.EditableDAO;
import org.openxdata.server.dao.FormDataDAO;
import org.openxdata.server.dao.StudyDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OpenclinicaServlet extends HttpServlet {

	private static final long serialVersionUID = 6577932874016086164L;
		
	private static final String DOWNLOAD_AND_CONVERT = "downloadAndConvert";
	
	private StudyDAO studyDAO;
	private FormDataDAO formDataDAO;
	private EditableDAO editableDAO;
	
	private OpenclinicaService openclinicaService;
	
	private static final Logger log = LoggerFactory.getLogger(OpenclinicaServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);

		studyDAO = (StudyDAO) ctx.getBean("studyDAO");
		formDataDAO = (FormDataDAO) ctx.getBean("formDataDAO");
		editableDAO = (EditableDAO) ctx.getBean("editableDAO");

		openclinicaService = new OpenclinicaServiceImpl();

		openclinicaService.setStudyDAO(studyDAO);
		openclinicaService.setFormDataDAO(formDataDAO);
		openclinicaService.setEditableDAO(editableDAO);

	}
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
    	
    	StudyDef study = null;
    	String oid = request.getParameter("oid");
    	String action = request.getParameter("action");
    	
    	log.info("Fetching study for oid: " + oid);
    	if(DOWNLOAD_AND_CONVERT.equals(action)) {
    		study = downloadAndConvertOpenclinicaStudy(oid);
    	}
    	
    	request.getSession().setAttribute("study", study);
    }
    
	private StudyDef downloadAndConvertOpenclinicaStudy(String oid) {
		return openclinicaService.importOpenClinicaStudy(oid);
	}
}
