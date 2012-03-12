package org.openxdata.oc.service

import java.util.List

import org.openxdata.oc.model.Event
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

public interface OpenclinicaService {

	Boolean hasStudyData(String studyKey)
		
	StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException

	String exportOpenClinicaStudyData()

	List<Event> getEvents(String studyOID)
	
	void setFormService(FormService formService)
	
	void setStudyService(StudyManagerService studyService)

	void setDataExportService(DataExportService dataExportService)
}
