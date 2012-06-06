package org.openxdata.oc.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.proto.ProtocolHandler;
import org.openxdata.proto.ProtocolLoader;
import org.openxdata.proto.exception.ProtocolAccessDeniedException;
import org.openxdata.proto.exception.ProtocolException;
import org.openxdata.proto.exception.ProtocolInvalidSessionReferenceException;
import org.openxdata.proto.exception.ProtocolNotFoundException;
import org.openxdata.server.OpenXDataConstants;
import org.openxdata.server.service.AuthenticationService;
import org.openxdata.server.servlet.DefaultSubmissionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;

/**
 * An example of how to support multiple upload protocols based on similar (or the same) class.
 * 
 * @author batkinson
 * 
 */
public class MultiProtocolSubmissionServlet {

	private static final Logger log = LoggerFactory.getLogger(MultiProtocolSubmissionServlet.class);

	private byte ACTION_NONE = -1;
	public static final byte RESPONSE_STATUS_ERROR = 0;
	public static final byte RESPONSE_ACCESS_DENIED = 2;
	public static final byte RESPONSE_INVALID_SESSION_REFERENCE = 4;

	private AuthenticationService authenticationService;
	private WebApplicationContext ctx;
	private ServletContext sctx;
	private OpenClinicaService openclinicaService;
	private Properties props;

	public MultiProtocolSubmissionServlet(ServletConfig config, ServletContext sctx,
			OpenClinicaService openclinicaService, Properties props) throws ServletException {

		this.sctx = sctx;
		ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sctx);

		// Manual Injection
		authenticationService = (AuthenticationService) ctx.getBean("authenticationService");
		this.openclinicaService = openclinicaService;
		this.props = props;

	}

	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("incoming request");

		InputStream in = req.getInputStream();
		OutputStream out = resp.getOutputStream();

		ZOutputStream zOut = null;

		DataInputStream dataIn = null;
		DataOutputStream dataOut = null;

		resp.setContentType("application/octet-stream");

		zOut = new ZOutputStream(out, JZlib.Z_BEST_COMPRESSION);
		dataIn = new DataInputStream(in);
		dataOut = new DataOutputStream(zOut);

		ClassLoader origCl = Thread.currentThread().getContextClassLoader();

		try {
			Thread.currentThread().setContextClassLoader(MultiProtocolSubmissionServlet.class.getClassLoader());

			log.debug("reading request details");
			String username = dataIn.readUTF();
			String password = dataIn.readUTF();
			String serializer = dataIn.readUTF();
			String locale = dataIn.readUTF();
			String action = req.getParameter(OpenXDataConstants.REQUEST_PARAMETER_ACTION);

			log.debug("authenticating user");
			if (authenticationService.authenticate(username, password) == null) {
				dataOut.writeByte(RESPONSE_ACCESS_DENIED);
				resp.setStatus(HttpServletResponse.SC_OK);
				return;
			}

			log.debug("initializing protocol loader");
			String protoJarPath = resolvePluginName(serializer);
			URL protoLocation = getServletContext().getResource(protoJarPath);
			if (protoLocation == null) {
				throw new ProtocolNotFoundException("Could not load protocol jar '" + protoJarPath + "'");
			}
			if (log.isDebugEnabled())
				log.debug("loading protocol plugins from " + protoLocation);
			ProtocolLoader protoLoader = new ProtocolLoader();

			log.debug("loading protocol handler");
			ProtocolHandler handler = protoLoader.loadHandler(protoLocation);

			log.debug("creating submission context");
			DefaultSubmissionContext submitCtx = null;
			try {
				submitCtx = new OCSubmissionContext(in, zOut, req, resp, null, openclinicaService, props);
				submitCtx.setAction(action == null ? ACTION_NONE : Byte.parseByte(action));
				submitCtx.setLocale(locale);
				ctx.getAutowireCapableBeanFactory().autowireBean(submitCtx);
			} catch (Throwable numberFormatException) {
				numberFormatException.printStackTrace();
				throw new ProtocolException(numberFormatException);
			}

			log.debug("handling request");
			handler.handleRequest(submitCtx);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ProtocolAccessDeniedException e) {
			log.error("protocol access denied while handling request from client", e);
			dataOut.writeByte(RESPONSE_ACCESS_DENIED);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ProtocolInvalidSessionReferenceException e) {
			log.error("protocol invalid session reference errror while handling request from client", e);
			dataOut.writeByte(RESPONSE_INVALID_SESSION_REFERENCE);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (ProtocolException e) {
			log.error("protocol error while handling request from client", e);
			dataOut.writeByte(RESPONSE_STATUS_ERROR);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			log.error("error while handing request to protocol provider", e);
			dataOut.writeByte(RESPONSE_STATUS_ERROR);
			resp.setStatus(HttpServletResponse.SC_OK);
		} finally {
			if (dataOut != null)
				dataOut.flush();
			if (zOut != null)
				zOut.finish();
			resp.flushBuffer();
			Thread.currentThread().setContextClassLoader(origCl);
		}
	}

	/**
	 * Loads a file that contains mappings from requested protocol versions and the actual plugin version to use. This
	 * allows an administrator to change the version of the protocol used to handle client requests without updating the
	 * client or renaming the protocol plugins.
	 * 
	 * The file contains entries like:
	 * 
	 * mforms-1.2=mforms-1.2.1
	 * 
	 * Which says that client requests suggesting use of mforms-1.2 should be handled using mforms-1.2.1. This
	 * particular pattern is likely when making a bugfix to a protocol.
	 * 
	 * @return a Properties object containing requestedVersion=actualVersion entries.
	 * @throws IOException
	 */
	private Properties getProtoMap() throws IOException {

		log.debug("loading explicit protocol mappings");
		InputStream protoMapStream = getServletContext().getResourceAsStream(
				"/WEB-INF/protocol-jars/protomap.properties");

		Properties protoMap = new Properties();

		if (protoMapStream != null) {
			log.debug("found mappings file, loading");
			protoMap.load(protoMapStream);
		} else {
			log.debug("mappings file not found");
		}

		return protoMap;
	}

	/**
	 * Takes a protocol version name sent from a mobile client and attempts to resolve it to a plugin to handle it.
	 * 
	 * @param requestedVersion
	 *            the serializer string sent from mobile client
	 * @return a suggested path to a protocol plugin
	 * @throws IOException
	 */
	private String resolvePluginName(String requestedVersion) throws IOException {

		log.debug("checking to see if protocol is explicitly defined");
		Properties protoMap = getProtoMap();
		if (protoMap.containsKey(requestedVersion)) {
			String mappedVersion = protoMap.getProperty(requestedVersion);
			if (log.isDebugEnabled())
				log.debug("found mapping: " + requestedVersion + " -> " + mappedVersion);
			requestedVersion = mappedVersion;
		}

		String protoJarPath = MessageFormat.format("/WEB-INF/protocol-jars/{0}.jar", new Object[] { requestedVersion });

		if (log.isDebugEnabled())
			log.debug("resolved " + requestedVersion + " to " + protoJarPath);

		return protoJarPath;
	}

	private ServletContext getServletContext() {
		return sctx;
	}
}
