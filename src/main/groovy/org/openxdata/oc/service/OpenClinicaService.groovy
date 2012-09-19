package org.openxdata.oc.service

import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

public interface OpenClinicaService {

	Boolean hasStudyData(String studyKey)

	StudyDef importOpenClinicaStudy(String oid) throws UnexpectedException

	String exportFormData(FormData formData)

	HashMap<String, String> exportOpenClinicaStudyData()

	List<StudySubject> getStudySubjectEvents()

	void setFormService(FormService formService)

	void setStudyService(StudyManagerService studyService)

	void setDataExportService(DataExportService dataExportService)
}
