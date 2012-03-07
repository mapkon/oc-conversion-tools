
package org.openxdata.oc.service.impl

import groovy.util.logging.Log
import groovy.util.slurpersupport.NodeChild

import java.util.ArrayList
import java.util.Collection
import java.util.Date
import java.util.List
import java.util.Properties

import org.openxdata.oc.model.Event
import org.openxdata.oc.service.OpenclinicaService
import org.openxdata.oc.transport.impl.OpenClinicaSoapClientImpl
import org.openxdata.oc.util.PropertiesUtil
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef
import org.openxdata.server.admin.model.User
import org.openxdata.server.admin.model.exception.OpenXDataSessionExpiredException
import org.openxdata.server.admin.model.exception.UnexpectedException
import org.openxdata.server.export.ExportConstants
import org.openxdata.server.security.util.OpenXDataSecurityUtil
import org.openxdata.server.service.DataExportService
import org.openxdata.server.service.FormService
import org.openxdata.server.service.StudyManagerService
import org.openxdata.xform.StudyImporter

@Log
public class OpenclinicaServiceImpl implements OpenclinicaService {

	private def client
	
	private def formService
	private def studyService
	private DataExportService dataExportService	
	
	public OpenclinicaServiceImpl(Properties props) {

		if(!props) {
			props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')
		}

		client = new OpenClinicaSoapClientImpl(props)
	}
	
	@Override
	public Boolean hasStudyData(String studyKey) {
		StudyDef study = studyService.getStudyByKey(studyKey)
		return studyService.hasEditableData(study)
	}

	@Override
	public StudyDef importOpenClinicaStudy(String identifier) throws UnexpectedException {
				
		NodeChild xml = (NodeChild) client.getOpenxdataForm(identifier)
		
		log.info("OXD: Converting Xform to study definition.")
		StudyImporter importer = new StudyImporter(xml)
		StudyDef study = createStudy(importer)
		
		return study
	}

	private StudyDef createStudy(StudyImporter importer) {
		
		Date dateCreated = new Date()
		
		User creator = getUser()
		
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
		User user = null
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
		
		Collection<String> returnedSubjects = client.getSubjectKeys(studyOID)
		for(String x : returnedSubjects){
			subjects.add(x)
		}
		return subjects
	}

	@Override
	public String exportOpenClinicaStudyData() {

		List<FormData> dataList = dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)

		log.info("Running OpenClinica Export Routine to export " + dataList.size()	+ " form data items")

		String exportResponse
		
		if(dataList.size == 0)
			exportResponse = "No data to export. Collect Data and then export."
		else
			exportResponse = client.importData(dataList)

		if("Success".equals(exportResponse)) {
			
			dataList.each {

				log.info("Resetting Export Flag for form data with id: " + it.getId())

				dataExportService.setFormDataExported(it, ExportConstants.EXPORT_BIT_OPENCLINICA)
				it.setExportedFlag(ExportConstants.EXPORT_BIT_OPENCLINICA)
			}
		}

		return exportResponse
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
	
	public void setDataExportService(DataExportService dataExportService) {
		this.dataExportService = dataExportService
	}
}
