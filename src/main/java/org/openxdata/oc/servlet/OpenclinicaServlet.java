package org.openxdata.oc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.handler.AccessDeniedHandler;
import org.openxdata.oc.handler.ErrorWhileProcessHandler;
import org.openxdata.oc.handler.ProcessorCreator;
import org.openxdata.oc.handler.RequestHandler;
import org.openxdata.oc.service.OpenclinicaService;
import org.openxdata.oc.service.impl.OpenclinicaServiceImpl;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.admin.model.User;
import org.openxdata.server.dao.EditableDAO;
import org.openxdata.server.dao.FormDataDAO;
import org.openxdata.server.dao.StudyDAO;
import org.openxdata.server.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class OpenclinicaServlet extends HttpServlet {

	private static final long serialVersionUID = 6577932874016086164L;
		
	private static final String DOWNLOAD_AND_CONVERT = "downloadAndConvert";
	
	@Autowired private StudyDAO studyDAO;
	@Autowired private FormDataDAO formDataDAO;
	@Autowired private EditableDAO editableDAO;
	@Autowired private ProcessorCreator processorCreator;
	@Autowired private AuthenticationService authSrv;
	
	private OpenclinicaService openclinicaService;
	
	private static final Logger log = LoggerFactory.getLogger(OpenclinicaServlet.class);
	
	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
		
		ServletContext sctx = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);
		
		ctx.getAutowireCapableBeanFactory().autowireBean(this);
		
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
	
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	try {
	    RequestParams params = readType(req, resp);
	    RequestHandler reqHandler = processorCreator.buildRequestHandler(params.type);
	    ByteArrayOutputStream tempOS = new ByteArrayOutputStream();
	    reqHandler.handleRequest(params.user, req.getInputStream(), tempOS);
	    resp.getOutputStream().write(tempOS.toByteArray());
	    resp.getOutputStream().flush();
	} catch (Exception ex) {
	    log.error("Problem while processing request from mobile: ", ex);
	    new ErrorWhileProcessHandler(ex).handleRequest(null, null, resp.getOutputStream());
	}
    }

    private RequestParams readType(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	RequestParams params = new RequestParams();
	DataInputStream dis = new DataInputStream(req.getInputStream());
	User user = authenticatedBinaryStream(dis);
	if (user != null) {
	    params.user = user;
	    params.type = dis.readUTF();
	} else {
	    params.type = AccessDeniedHandler.class.getName();
	}
	return params;
    }

    private User authenticatedBinaryStream(DataInputStream dis) throws IOException {
	String userName = dis.readUTF();
	String password = dis.readUTF();
	User user = authSrv.authenticate(userName, password);
	return user;
    }

    private class RequestParams {

	private User user;
	private String type;
    }
}
