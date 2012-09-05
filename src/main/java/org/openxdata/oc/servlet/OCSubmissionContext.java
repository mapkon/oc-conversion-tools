package org.openxdata.oc.servlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openxdata.oc.exception.ExportException;
import org.openxdata.oc.model.Event;
import org.openxdata.oc.model.StudySubject;
import org.openxdata.oc.service.OpenClinicaService;
import org.openxdata.proto.WFSubmissionContext;
import org.openxdata.server.admin.model.FormData;
import org.openxdata.server.admin.model.FormDef;
import org.openxdata.server.admin.model.StudyDef;
import org.openxdata.server.service.FormDownloadService;
import org.openxdata.server.service.StudyManagerService;
import org.openxdata.server.service.UserService;
import org.openxdata.server.servlet.DefaultSubmissionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class OCSubmissionContext extends DefaultSubmissionContext implements WFSubmissionContext {

	private static Logger log = LoggerFactory.getLogger(OCSubmissionContext.class);

	private Properties props;
	private OpenClinicaService ocService;

	@Autowired
	private StudyManagerService studyManagerService;
	@Autowired
	private UserService userService;
	@Autowired
	private FormDownloadService formService;
	private List<Event> orphanedEvents = new ArrayList<Event>();

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

	public Map<String, Object> getWorkitem(String caseId) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void submitWorkitem(String caseId, String paramXML) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<Map<String, Object>> availableWorkitems() {

		clearOphanedEvents();

		List<StudySubject> sbjEvents = ocService.getStudySubjectEvents();
		List<Map<String, Object>> workitems = new ArrayList<Map<String, Object>>();
		StudyDef ocStudy = loadConvertedOpenClinicaStudy();
		if (ocStudy == null) {
			return workitems;
		}

		for (StudySubject studySubject : sbjEvents) {
			List<Map<String, Object>> studySubjWorkitems = studySubjectToWorkItems(studySubject, ocStudy);
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

	List<Map<String, Object>> studySubjectToWorkItems(StudySubject studySubject, StudyDef ocStudy) {

		List<Map<String, Object>> workitems = new ArrayList<Map<String, Object>>();
		List<Event> allEvents = studySubject.getEvents();
		Hashtable<String, List<Event>> eventsGroupedByName = groupEventByName(allEvents);
		Set<Entry<String, List<Event>>> eventGroupEntry = eventsGroupedByName.entrySet();

		for (Entry<String, List<Event>> entry : eventGroupEntry) {
			List<Event> eventList = entry.getValue();
			Map<String, Object> workitem = new HashMap<String, Object>();
			String workitemName = (studySubject.getSubjectOID() + "-" + entry.getKey()).replaceFirst("SS_", "");
			workitem.put("name", workitemName);
			workitem.put("id", generateWorkitemID(studySubject, entry.getKey()));

			List<Map<String,Object>> formReferences = new ArrayList<Map<String,Object>>();
			for (Event ocEvent : eventList) {
				List<Map<String,Object>> formRefs = extractFormReferencesFromEvent(ocEvent, ocStudy, studySubject);
				formReferences.addAll(formRefs);
			}
			if (!formReferences.isEmpty()) {
				workitem.put("formrefs", formReferences);
				workitems.add(workitem);
			}

		}
		return workitems;
	}

	private List<Map<String,Object>> extractFormReferencesFromEvent(Event ocEvent, StudyDef studyDef, StudySubject studySubject) {
		List<Map<String,Object>> formReferences = new ArrayList<Map<String,Object>>();
		List<String> formOIDs = ocEvent.getFormOIDs();
		for (String formOID : formOIDs) {
			Map<String,Object> formRef = formDefToFormReferece(formOID, studyDef, ocEvent, studySubject);
			if (formRef != null) {
				formReferences.add(formRef);
			}
		}
		return formReferences;
	}

	private Map<String, Object> formDefToFormReferece(String formOID, StudyDef oCStudyID, Event event, StudySubject studySubject) {
		FormDef formDef = getFormByDescription(oCStudyID, formOID);

		if (formDef == null) {
			orphanedEvents.add(event);
			return null;
		}

		List<String[]> prefills = new ArrayList<String[]>();
		prefills.add(new String[] { "SubjectKey_", "subjectkey", studySubject.getSubjectOID() + "", "false" });

		Map<String, Object>  formRef = new HashMap<String, Object>();
		formRef.put("studyid",oCStudyID.getId());
		formRef.put("formid",formDef.getDefaultVersion().getId());
		formRef.put("prefills", prefills);
		return formRef;
	}

	public List<Map<String, Object>> getWorkItems(String... caseIds) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	private String generateWorkitemID(StudySubject studySubject, String eventOID) {
		String key = studySubject.getSubjectOID().toString();
		key = key + "&" + eventOID;
		return key;
	}

	// Loads the study that was converted.
	private StudyDef loadConvertedOpenClinicaStudy() {
		StudyDef study = null;
		try {
			String property = props.getProperty("studyOID");
			log.debug("Reading study from properties file: " + property);
			if (property == null || property.isEmpty()) {
				log.error("The studyOID Property has not been set");
				return null;
			}
			study = studyManagerService.getStudyByKey(property);
			if (!isMappedToStudy(study)) {
				log.info("**Access not allowed to OC Converted study hence no workitems will be created");
				study = null;
			}
		} catch (Exception e) {
			log.error("Failed to get openclinica study" + e.getMessage());
			log.trace("Failed to get openclinica study", e);
		}

		return study;

	}

	private boolean isMappedToStudy(StudyDef study) {
		Map<Integer, String> studyNames = studyManagerService.getStudyNamesForCurrentUser();
		if (study == null)
			return false;
		return studyNames != null && studyNames.containsKey(study.getId());
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
		FormData formData = formService.saveFormData(formInstance, userService.getLoggedInUser(), new Date());
		String exportResponse = ocService.exportFormData(formData);
		if (exportResponse.equalsIgnoreCase("Success"))
			return formData.getId() + "";
		else
			throw new ExportException("Upload Failed: " + exportResponse);
	}

	public void setFormService(FormDownloadService formService) {
		this.formService = formService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
