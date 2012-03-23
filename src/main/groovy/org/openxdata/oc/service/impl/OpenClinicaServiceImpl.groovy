
package org.openxdata.oc.service.impl

import groovy.util.logging.Log
import groovy.util.slurpersupport.NodeChild

import java.util.Date
import java.util.List
import java.util.Properties

import org.openxdata.oc.model.Event
import org.openxdata.oc.model.StudySubject
import org.openxdata.oc.service.OpenClinicaService
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
public class OpenClinicaServiceImpl implements OpenClinicaService {

	private def client
	
	private def formService
	private def studyService
	private DataExportService dataExportService	
	
	public OpenClinicaServiceImpl(Properties props) {

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
	public StudyDef importOpenClinicaStudy(String studyOID) throws UnexpectedException {
				
		NodeChild xml = (NodeChild) client.getOpenxdataForm(studyOID)
		
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

	public List<StudySubject> getStudySubjectEvents(String studyOID) {
		
		return client.findStudySubjectEventsByStudyOID(studyOID)
	}
	
	@Override
	public HashMap<String, String> exportOpenClinicaStudyData() {

		List<FormData> dataList = dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)

		log.info("Running OpenClinica Export Routine to export «" + dataList.size()	+ "» form data item(s)")

		def exportResponseMessages = buildResponseMessage(dataList)

		for(Map.Entry<String, String> entry : exportResponseMessages.entrySet()) {
			
			def key = entry.key
			if(exportResponseMessages.get(key).is("Success")) {
				updateExportedDataItems(dataList, key)
			}
		}

		return exportResponseMessages
	}

	private def buildResponseMessage(def dataList) {
		
		def exportResponseMessages = [:]
		
		if(dataList.size == 0) {

			def message = "No data items found to export."
			
			log.info(message)
			exportResponseMessages.put("", message)
		}
		else {
			exportResponseMessages = client.importData(dataList)
		}
			
		return exportResponseMessages
	}

	private updateExportedDataItems(def dataList, def key) {

		dataList.each {

			def xml = new XmlSlurper().parseText(it.getData())
			
			if(key.equals(xml.@formKey.text())) {
				log.info("Resetting Export Flag for form data with id: " + it.getId())

				dataExportService.setFormDataExported(it, ExportConstants.EXPORT_BIT_OPENCLINICA)
				it.setExportedFlag(ExportConstants.EXPORT_BIT_OPENCLINICA)
			}
		}
	}
	
	public void setStudyService(StudyManagerService studyService) {
		this.studyService = studyService
	}
	
	public void setFormService(FormService formService) {
		this.formService = formService
	}
	
	public void setDataExportService(DataExportService dataExportService) {
		this.dataExportService = dataExportService
	}
	


}
