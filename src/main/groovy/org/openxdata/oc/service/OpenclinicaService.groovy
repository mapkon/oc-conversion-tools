package org.openxdata.oc.service;

import java.util.List

import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.exception.UnexpectedException

public interface OpenclinicaService {

	Boolean hasStudyData(String studyKey);
	
	def getOpenClinicaStudies() throws UnexpectedException;
	
	List<String> getStudySubjects(String studyOID) throws UnexpectedException;

	StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException;

	String exportOpenClinicaStudyData(String studyKey);
}
