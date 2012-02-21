package org.openxdata.oc.service.impl

import groovy.util.logging.Log
import groovy.util.slurpersupport.NodeChild

import java.util.ArrayList
import java.util.Collection
import java.util.Date
import java.util.List

import org.openxdata.oc.model.Event
import org.openxdata.oc.service.OpenclinicaService
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl
import org.openxdata.oc.util.PropertiesUtil
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User
import org.openxdata.server.admin.model.exception.OpenXDataSessionExpiredException
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.admin.model.paging.PagingLoadConfig
import org.openxdata.server.security.util.OpenXDataSecurityUtil
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService
import org.openxdata.xform.StudyImporter

@Log
public class OpenclinicaServiceImpl implements OpenclinicaService {

	private def client
	
	private def formService
			
	private def studyService	
	
	private OpenClinicaSoapClient getClient() {
		
		if(client == null){
			
			log.info("OXD: Initializing client for first time use.")
			
			def props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')
			
			def host = props.getAt('host')
			
			def connectionFactory = new ConnectionFactory(host:host)
			client = new OpenClinicaSoapClientImpl(connectionFactory)
		}
		
		return client
	}
	
	@Override
	public Boolean hasStudyData(String studyKey) {
		StudyDef study = studyService.getStudyByKey(studyKey)
		return studyService.hasEditableData(study)
	}

	@Override
	public StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException {
				
		NodeChild xml = (NodeChild) getClient().getOpenxdataForm(identifier)
		
		log.info("OXD: Converting Xform to study definition.")
		StudyImporter importer = new StudyImporter(xml)
		StudyDef study = createStudy(importer)
		
		return study
	}

	private StudyDef createStudy(StudyImporter importer) {
		
		Date dateCreated = new Date()
		
		User creator = getUser();
		
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
	
	private User getUser() {
		User user = null;
		try {
			user = OpenXDataSecurityUtil.getLoggedInUser()
		}catch(OpenXDataSessionExpiredException ex){
			user = new User('admin')
		}
		
		return user
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
		
		def allData = []
		def config = new PagingLoadConfig(0, 5);
		StudyDef study = studyService.getStudyByKey(studyKey)
		
		for (FormDef form : study.getForms()) {
			def dataList = formService.getFormDataList(form.getDefaultVersion(), config)
			dataList.getData().each {
				allData.add(it)
			}
		}
		return (String) getClient().importData(allData)	
	}
	
	List<Event> getEvents(String studyOID){
		
		def events = []
		def xml = client.findEventsByStudyOID(studyOID)
		
		def eventNode = new XmlSlurper().parseText(xml)
		eventNode.event.each {
			def event = new Event(it)
			events.add(event)
		}
		
		return events
	}
	
	void setStudyService(StudyManagerService studyService) {
		this.studyService = studyService
	}
	
	void setFormService(FormService formService) {
		this.formService = formService
	}
}
