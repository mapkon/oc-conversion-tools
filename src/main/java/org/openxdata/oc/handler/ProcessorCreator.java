/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author kay
 */
@Component("processorCreator")
public class ProcessorCreator
{
    private ApplicationContext context;

    public ProcessorCreator(ApplicationContext context) {
	this.context = context;
    }
    
    

        public RequestHandler buildRequestHandler(String type)
        {
                RequestHandler rh = null;
                try {
                        rh = loadClassForQuery(type).newInstance();
                } catch (ClassNotFoundException ex) {
                        System.err.println("Error While Creating Handler: "+ex);
                        rh = new NotSupportedHandler(type);
                } catch (Exception ex) {
                        System.err.println("Error While Creating Handler: "+ex);
                        rh = new ErrorWhileProcessHandler(ex);
                }
		rh.setApplicationContext(context);
                return rh;
        }

        private Class<RequestHandler> loadClassForQuery(String handlerClassName) throws ClassNotFoundException
        {
                try {
                        return (Class<RequestHandler>) Class.forName(handlerClassName);
                } catch (ClassNotFoundException classNotFoundException) {
                }
                try {
                        return (Class<RequestHandler>) Class.forName("org.openxdata.oc.handler." + handlerClassName);
                } catch (ClassNotFoundException classNotFoundException) {
                }
                return (Class<RequestHandler>) Class.forName("org.openxdata.oc.handler." + handlerClassName + "Handler");
        }
}
