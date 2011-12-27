package org.openxdata.oc.service.impl

import groovy.util.logging.Log
import groovy.util.slurpersupport.NodeChild

import java.util.ArrayList
import java.util.Collection
import java.util.Date
import java.util.List

import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.service.OpenclinicaService
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl
import org.openxdata.oc.util.PropertiesUtil;
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.dao.EditableDAO
import org.openxdata.server.dao.FormDataDAO
import org.openxdata.server.dao.SettingDAO
import org.openxdata.server.security.util.OpenXDataSecurityUtil
import org.openxdata.server.service.StudyManagerService
import org.openxdata.xform.StudyImporter

@Log
public class OpenclinicaServiceImpl implements OpenclinicaService {

	private def client
	
	private def formDataDAO
	
	private def editableDAO
		
	private def studyService	
	
	private OpenClinicaSoapClient getClient() {
		
		if(client == null){
			
			log.info("OXD: Initializing client for first time use.")
			
			def props = new PropertiesUtil().loadProperties('openclinica.properties')
			
			def host = props.getAt('host')
			
			ConnectionFactory connectionFactory = new ConnectionFactory(host:host)
			client = new OpenClinicaSoapClientImpl(connectionFactory)
		}
		
		return client
	}
	
	@Override
	public Boolean hasStudyData(String studyKey) {
		StudyDef study = studyService.getStudy(studyKey)
		return editableDAO.hasEditableData(study)
	}
	
	@Override
	public def getOpenClinicaStudies() {
		
		log.info("OXD: Fetching available OpenClinica studies.")
		
		List<ConvertedOpenclinicaStudy> studies = getClient().listAll()
		
		log.info("OXD: " + studies.size() + "studies and returned to OpenXdata.")
		
		def returnStudies = []
		
		try{
			
			List<StudyDef> openxdataStudies = studyService.getStudies()
			
			for (def study : studies) {
				log.info("OXD: Checking duplicate studies.")
				if(!isStudyDownloaded(openxdataStudies, study)){
					returnStudies.add(study)
				}
			}
			
		}catch(Exception ex){
			throw new UnexpectedException(ex)
		}

		return returnStudies
	}

	private void appendSubjects(ConvertedOpenclinicaStudy study, def ocStudy) {
		
		Collection<String> subjects = getClient().getSubjectKeys(study.getIdentifier())
		
		log.info("OXD: Appending " + subjects.size() + "subjects to the study")
		
		ocStudy.setSubjects(subjects)
	}

	private boolean isStudyDownloaded(List<ConvertedOpenclinicaStudy> studies, ConvertedOpenclinicaStudy study) {
				
		studies.each {
			
			String oxdStudyName = it.getName()
			String oxdStudyIdentifier = it.getStudyKey()
			
			String ocStudyName = study.getName()
			String ocStudyIdentifier = study.getIdentifier()
			
			if(oxdStudyName.equals(ocStudyName) && 
					oxdStudyIdentifier == ocStudyIdentifier) {
				
				return true
			}
		}
		return false
	}

	@Override
	public StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException {
				
		NodeChild xml = (NodeChild) getClient().getOpenxdataForm(identifier)
		
		log.info("OXD: Converting Xform to study definition.")
		StudyImporter importer = new StudyImporter(xml)
		StudyDef study = createStudy(importer)
		
		log.info("OXD: Saving converted study definition.")
		studyService.saveStudy(study)
		
		return study
	}

	private StudyDef createStudy(StudyImporter importer) {
		
		Date dateCreated = new Date()
		User creator = OpenXDataSecurityUtil.getLoggedInUser()
		
		StudyDef study = (StudyDef) importer.extractStudy()
		study.setCreator(creator)
		study.setDateCreated(dateCreated)
		
		List<FormDef> forms = study.getForms()
		
		for(FormDef form : forms) {
			
			form.setStudy(study)
			form.setCreator(creator)
			form.setDateCreated(dateCreated)
			
			setFormVersionProperties(form, dateCreated, creator)
		}
		
		return study
	}
	
	private void setFormVersionProperties(FormDef form, Date dateCreated, User creator) {
		List<FormDefVersion> versions = form.getVersions()
		
		for(FormDefVersion version : versions) {
			
			version.setFormDef(form)
			version.setCreator(creator)
			version.setDateCreated(dateCreated)
		}
	}

	@Override
	public List<String> getStudySubjects(String studyOID) throws UnexpectedException {
		try{
			List<String> subjects = fetchSubjects(studyOID)
			return subjects
		}catch(Exception ex){
			throw new UnexpectedException(ex)
		}
	}

	private List<String> fetchSubjects(String studyOID) {
		
		List<String> subjects = new ArrayList<String>()

		log.info("OXD: Fetching subjects.")
		
		Collection<String> returnedSubjects = getClient().getSubjectKeys(studyOID)
		for(String x : returnedSubjects){
			subjects.add(x)
		}
		return subjects
	}

	@Override
	public String exportOpenClinicaStudyData(String studyKey) {
		StudyDef study = studyService.getStudy(studyKey)
		List<String> allData = new ArrayList<String>() 
		for (FormDef form : study.getForms()) {
			List<FormData> dataList = formDataDAO.getFormDataList(form)
			for (FormData formData: dataList) {
				allData.add(formData.getData())
			}
		}
		return (String) getClient().importData(allData)	
	}
	
	void setFormDataDAO(FormDataDAO formDataDAO) {
		this.formDataDAO = formDataDAO
	}

	void setEditableDAO(EditableDAO editableDAO) {
		this.editableDAO = editableDAO
	}

	void setStudyService(StudyManagerService studyService) {
		this.studyService = studyService
	}
}
