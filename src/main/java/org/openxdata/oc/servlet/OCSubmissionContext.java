package org.openxdata.oc.servlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.model.Event;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.proto.WFSubmissionContext;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.servlet.DefaultSubmissionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OCSubmissionContext extends DefaultSubmissionContext implements WFSubmissionContext {

	@Autowired
	private StudyManagerService studyManagerService;
	private OpenClinicaService ocService;
	private static Logger log = LoggerFactory.getLogger(OCSubmissionContext.class);
	private Properties props;
	private List<Event> orphanedEvents = new ArrayList<Event>();
	private ExecutorService ex = Executors.newSingleThreadExecutor();

	public OCSubmissionContext(InputStream input, OutputStream output, HttpServletRequest httpReq,
			HttpServletResponse httpRsp, Map<String, List<String>> metaData, OpenClinicaService ocService,
			Properties props) {
		super(input, output, httpReq, httpRsp, metaData);
		this.ocService = ocService;
		this.props = props;
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
		clearOphanedEvents();
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

	public void clearOphanedEvents() {
		orphanedEvents.clear();
	}

	public List<Event> getOrphanedEvents() {
		return orphanedEvents;
	}

	List<Object[]> studySubjectToWorkItems(StudySubject studySubject, StudyDef ocStudy) {
		List<Object[]> workitems = new ArrayList<Object[]>();
		List<Event> allEvents = studySubject.getEvents();
		Hashtable<String, List<Event>> eventGroups = groupEventByName(allEvents);
		Set<Entry<String, List<Event>>> entrySet = eventGroups.entrySet();

		for (Entry<String, List<Event>> entry : entrySet) {
			List<Event> events = entry.getValue();
			Object[] workitem = new Object[5];
			workitem[0] = (studySubject.getSubjectOID() + "-" + entry.getKey()).replaceFirst("SS_", "");
			workitem[1] = getKey(studySubject, entry.getKey());

			List<Object[]> formReferences = new ArrayList<Object[]>();
			for (Event event : events) {
				List<Object[]> formRefs = extractFormReferences(event, ocStudy, studySubject);
				formReferences.addAll(formRefs);
			}
			if (!formReferences.isEmpty()) {
				workitem[2] = formReferences;
				workitems.add(workitem);
			}

		}
		return workitems;
	}

	private List<Object[]> extractFormReferences(Event even, StudyDef studyDef, StudySubject studySubject) {
		List<Object[]> formReferences = new ArrayList<Object[]>();
		List<String> formOIDs = even.getFormOIDs();
		for (String formOID : formOIDs) {
			Object[] formRef = formDefToFormReferece(formOID, studyDef, even, studySubject);
			if (formRef != null) {
				formReferences.add(formRef);
			}
		}
		return formReferences;
	}

	private Object[] formDefToFormReferece(String formOID, StudyDef oCStudyID, Event event, StudySubject studySubject) {
		FormDef formDef = getFormByDescription(oCStudyID, formOID);

		if (formDef == null) {
			orphanedEvents.add(event);
			return null;
		}

		List<String[]> prefills = new ArrayList<String[]>();
		prefills.add(new String[] { "SubjectKey_", "subjectkey", studySubject.getSubjectOID() + "", "false" });

		Object[] formRef = new Object[3];
		formRef[0] = oCStudyID.getId();
		formRef[1] = formDef.getDefaultVersion().getId();
		formRef[2] = prefills;
		return formRef;
	}

	public List<Object[]> getWorkItems(String... caseIds) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private String getKey(StudySubject studySubject, String eventOID) {
		String key = studySubject.getSubjectOID().toString();
		key = key + "&" + eventOID;
		return key;
	}

	private StudyDef getOCStudyID() {
		List<StudyDef> studyByName = null;
		try {
			String property = props.getProperty("ocStudy");
			log.debug("Reading study from properties file: " + property);
			if (property == null || property.isEmpty()) {
				log.error("The ocStudy Property has not been set");
				return null;
			}
			studyByName = studyManagerService.getStudyByName(property);
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

		for (FormDef formDef1 : forms) {
			String frmDefDescr = formDef1.getDescription();
			if (frmDefDescr != null && frmDefDescr.equalsIgnoreCase(description)) {
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
				eventGroups.put(evntOID, grpEvnts);
			}

			grpEvnts.add(event);
		}

		return eventGroups;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

	public void setStudyManagerService(StudyManagerService studyManagerService) {
		this.studyManagerService = studyManagerService;
	}

	@Override
	public String setUploadResult(String formInstance) {
		String result = super.setUploadResult(formInstance);
		exportDataToOC();
		return result;
	}

	private void exportDataToOC() {
		Runnable exporter = new Runnable() {

			public void run() {
				ocService.exportOpenClinicaStudyData();
			}
		};
		ex.execute(exporter);
	}
}
