/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.Map;
import org.openxdata.proto.WFSubmissionContext;
import org.openxdata.server.service.FormDownloadService;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.service.UserService;
import org.openxdata.server.servlet.DefaultSubmissionContext;

/**
 *
 * @author kay
 */
public class OCSubmissionContext extends DefaultSubmissionContext implements WFSubmissionContext {

    public OCSubmissionContext(DataInputStream input, DataOutputStream output, byte action, String locale, UserService userService, FormDownloadService formService, StudyManagerService studyManagerService) {
        super(input, output, action, locale, userService, formService, studyManagerService);
    }

    public Map<String, String> getOutParamsQuestionMapping(int formId, String caseId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object[] getWorkitem(String caseId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void submitWorkitem(String caseId, String paramXML) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Object[]> availableWorkitems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Object[]> getWorkItems(String... caseIds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
