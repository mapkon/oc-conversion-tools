/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.openxdata.oc.model.Event;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.proto.WFSubmissionContext;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.FormDownloadService;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.service.UserService;
import org.openxdata.server.servlet.DefaultSubmissionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kay
 */
public class OCSubmissionContext extends DefaultSubmissionContext implements WFSubmissionContext {

	private OpenClinicaService ocService;
	private StudyManagerService studyManagerService;
	private static Logger log = LoggerFactory.getLogger(OCSubmissionContext.class);

	public OCSubmissionContext(DataInputStream input, DataOutputStream output, byte action, String locale,
			UserService userService, FormDownloadService formService, StudyManagerService studyManagerService,
			OpenClinicaService ocService) {
		super(input, output, action, locale, userService, formService, studyManagerService);
		this.studyManagerService = studyManagerService;
		this.ocService = ocService;
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
		List<StudySubject> sbjEvents = ocService.getStudySubjectEvents("S_DEFAULTS1");
		List<Object[]> workitems = new ArrayList<Object[]>();
		StudyDef ocStudy = getOCStudyID();
		if (ocStudy == null) {
			return workitems;
		}

		for (StudySubject studySubject : sbjEvents) {
			List<Object[]> studySubjWorkitems = studySubjectToWorkItems(studySubject, ocStudy);
			workitems.addAll(studySubjWorkitems);
		}

		return workitems;
	}

	private List<Object[]> studySubjectToWorkItems(StudySubject studySubject, StudyDef ocStudy) {
		List<Object[]> workitems = new ArrayList<Object[]>();
		List<Event> allEvents = studySubject.getEvents();
		Hashtable<String, List<Event>> eventGroups = groupEventByName(allEvents);
		Set<Entry<String, List<Event>>> entrySet = eventGroups.entrySet();

		for (Entry<String, List<Event>> entry : entrySet) {
			List<Event> events = entry.getValue();
			for (Event event : events) {
				Object[] workitem = eventToWIR(event, ocStudy, studySubject);
				if (workitem != null) {
					workitems.add(workitem);
				}

			}

		}
		return workitems;
	}

	private Object[] eventToWIR(Event event, StudyDef oCStudyID, StudySubject studySubject) {

		List<Object[]> formReferences = new ArrayList<Object[]>();
		List<String> formOIDs = (List) event.getFormOIDs();

		for (String formOID : formOIDs) {
			Object[] formRef = formDefToFormReferece(formOID, oCStudyID, event, studySubject);
			if (formRef != null) {
				formReferences.add(formRef);
			}
		}

		if (formReferences.isEmpty()) {
			return null;
		}

		Object[] workitem = new Object[5];
		workitem[0] = studySubject.getSubjectOID() + "-" + event.getEventName();
		workitem[1] = getKey(studySubject, event);
		workitem[2] = formReferences;
		return workitem;
	}

	private Object[] formDefToFormReferece(String formOID, StudyDef oCStudyID, Event event, StudySubject studySubject) {
		FormDef formDef = getFormByDescription(oCStudyID, formOID);

		if (formDef == null) {
			log.warn("FormOID[" + formOID + "] Event:[" + event.getEventDefinitionOID() + "] not found");
			return null;
		}

		List<String[]> prefills = new ArrayList<String[]>();
		prefills.add(new String[] { "SubjectKey_", "SubjectKey", studySubject.getSubjectOID() + "", "false" });

		Object[] formRef = new Object[3];
		formRef[0] = oCStudyID.getId();
		formRef[1] = formDef.getDefaultVersion().getId();
		formRef[2] = prefills;
		return formRef;
	}

	public List<Object[]> getWorkItems(String... caseIds) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private String getKey(StudySubject studySubject, Event event) {
		String key = studySubject.getSubjectOID().toString();
		key = key + "&" + event.getEventDefinitionOID();
		return key;
	}

	private StudyDef getOCStudyID() {
		List<StudyDef> studyByName = null;
		try {
			studyByName = studyManagerService.getStudyByName("Default Study");
		} catch (Exception e) {
			log.error("Failed to get openclinica study" + e.getMessage());
			log.trace("Failed to get openclinica study", e);
		}
		if (studyByName != null && !studyByName.isEmpty()) {
			StudyDef study = studyByName.get(0);
			return study;
		}
		return null;
	}

	private FormDef getFormByDescription(StudyDef def, String description) {
		List<FormDef> forms = def.getForms();

		//description = description.substring(0, description.lastIndexOf("_"));
		for (FormDef formDef1 : forms) {
			String frmDefDescr = formDef1.getDescription();
			//	frmDefDescr = frmDefDescr.substring(0, frmDefDescr.lastIndexOf("_"));
			if (frmDefDescr.equalsIgnoreCase(description)) {
				return formDef1;
			}
		}
		return null;
	}

	private Hashtable<String, List<Event>> groupEventByName(List<Event> events) {
		Hashtable<String, List<Event>> eventGroups = new Hashtable<String, List<Event>>();
		for (Event event : events) {
			String evntOID = (String) event.getEventDefinitionOID();

			List<Event> grpEvnts = eventGroups.get(evntOID);
			if (grpEvnts == null) {
				grpEvnts = new ArrayList<Event>();
				eventGroups.put(evntOID, events);
			}

			grpEvnts.add(event);
		}

		return eventGroups;
	}
}
