package org.openxdata.oc.service;

import java.util.List

import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

public interface OpenclinicaService {

	Boolean hasStudyData(String studyKey);
		
	List<String> getStudySubjects(String studyOID) throws UnexpectedException;

	StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException;

	String exportOpenClinicaStudyData(String studyKey)

	void setFormService(FormService formService)
	
	void setStudyService(StudyManagerService studyService)

}
