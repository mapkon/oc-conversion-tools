/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.openxdata.oc.model.Event;
import org.openxdata.oc.service.OpenclinicaService;
import org.openxdata.proto.WFSubmissionContext;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.FormDownloadService;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.service.UserService;
import org.openxdata.server.servlet.DefaultSubmissionContext;

/**
 *
 * @author kay
 */
public class OCSubmissionContext extends DefaultSubmissionContext implements
		WFSubmissionContext {

	private OpenclinicaService ocService;
	private StudyManagerService studyManagerService;

	public OCSubmissionContext(DataInputStream input, DataOutputStream output,
			byte action, String locale, UserService userService,
			FormDownloadService formService,
			StudyManagerService studyManagerService,
			OpenclinicaService ocService) {
		super(input, output, action, locale, userService, formService,
				studyManagerService);
		this.studyManagerService = studyManagerService;
		this.ocService = ocService;
	}

	public Map<String, String> getOutParamsQuestionMapping(int formId,
			String caseId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Object[] getWorkitem(String caseId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void submitWorkitem(String caseId, String paramXML) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Object[]> availableWorkitems() {
		List<Event> events = ocService.getEvents("S_DEFAULTS1");
		List<Object[]> workitems = new ArrayList<Object[]>();
		StudyDef oCStudyID = getOCStudyID();
		if (oCStudyID == null) {
			return workitems;
		}
		for (Event event : events) {
			Object[] workitem = new Object[5];

			List<Object[]> formReferences = new ArrayList<Object[]>();
			String[] formOIDs = (String[]) event.getFormOIDs();

			for (String formOID : formOIDs) {
				FormDef formDef = getFormByDescription(oCStudyID, formOID);
				if (formDef == null) {
					continue;
				}
				String[] subjectKeys = (String[]) event.getSubjectKeys();
				for (String string : subjectKeys) {
					Object[] frmRfrnc = new Object[3];
					frmRfrnc[0] = oCStudyID.getId();
					frmRfrnc[1] = formDef.getDefaultVersion().getId();
					List<String[]> prefills = new ArrayList<String[]>();
					prefills.add(new String[] { "SubjectKey_", "SubjectKey",
							string, "false" });
					frmRfrnc[2] = prefills;
					formReferences.add(frmRfrnc);
				}
			}
			if (formReferences.isEmpty())
				continue;
			workitem[0] = event.getName().toString();
			workitem[1] = getKey(event);
			workitem[2] = formReferences;
			workitems.add(workitem);
		}
		return workitems;
	}

	public List<Object[]> getWorkItems(String... caseIds) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private String getKey(Event event) {
		String key = event.getFormOIDs().toString() + "-";
		Object[] subjectKeys = (Object[]) event.getSubjectKeys();
		for (int i = 0; i < subjectKeys.length; i++) {
			Object object = subjectKeys[i];
			key = key + ":" + object;
		}
		return key;
	}

	private StudyDef getOCStudyID() {
		List<StudyDef> studyByName = studyManagerService
				.getStudyByName("Default Study");
		if (studyByName != null) {
			StudyDef study = studyByName.get(0);
			return study;
		}
		return null;
	}

	private FormDef getFormByDescription(StudyDef def, String description) {
		List<FormDef> forms = def.getForms();
		for (FormDef formDef1 : forms) {
			if (formDef1.getDescription().equalsIgnoreCase(description)) {
				return formDef1;
			}
		}
		return null;
	}
}
