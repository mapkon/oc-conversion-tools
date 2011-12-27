package org.openxdata.oc.service;

import java.util.List

import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.dao.EditableDAO;
import org.openxdata.server.dao.FormDataDAO;
import org.openxdata.server.service.StudyManagerService;

public interface OpenclinicaService {

	Boolean hasStudyData(String studyKey);
	
	def getOpenClinicaStudies() throws UnexpectedException;
	
	List<String> getStudySubjects(String studyOID) throws UnexpectedException;

	StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException;

	String exportOpenClinicaStudyData(String studyKey)

	void setFormDataDAO(FormDataDAO formDataDAO)

	void setEditableDAO(EditableDAO editableDAO)

	void setStudyService(StudyManagerService studyService)
}
