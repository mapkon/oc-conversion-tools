package org.openxdata.oc.service

import java.util.List

import org.openxdata.oc.model.StudySubject
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService

public interface OpenClinicaService {

	Boolean hasStudyData(String studyKey)

	StudyDef importOpenClinicaStudy(String oid) throws UnexpectedException

	HashMap<String, String> exportOpenClinicaStudyData()

	List<StudySubject> getStudySubjectEvents()

	void setFormService(FormService formService)

	void setStudyService(StudyManagerService studyService)

	void setDataExportService(DataExportService dataExportService)
}
