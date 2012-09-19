package org.openxdata.oc.service

import org.openxdata.oc.model.OpenClinicaUser;
import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

public interface OpenClinicaService {
	
	Boolean hasStudyData(String studyKey)

	String exportFormData(FormData formData)

	List<StudySubject> getStudySubjectEvents()
	
	OpenClinicaUser getUserDetails(def username)

	void setFormService(FormService formService)
	
	HashMap<String, String> exportOpenClinicaStudyData()

	void setStudyService(StudyManagerService studyService)

	void setDataExportService(DataExportService dataExportService)
	
	StudyDef importOpenClinicaStudy(String oid) throws UnexpectedException
}
