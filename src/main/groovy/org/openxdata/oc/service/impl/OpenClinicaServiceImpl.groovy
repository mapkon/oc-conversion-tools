
package org.openxdata.oc.service.impl

import groovy.util.logging.Log
import groovy.util.slurpersupport.NodeChild

import org.openxdata.oc.model.OpenClinicaUser
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

	def user
	private def client

	private def props
	private def formService
	private def studyService
	private DataExportService dataExportService

	public OpenClinicaServiceImpl(Properties props) {

		if(!props) {
			props = new PropertiesUtil().loadProperties('META-INF/openclinica.properties')
		}

		this.props = props
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

	public List<StudySubject> getStudySubjectEvents() {

		return client.findStudySubjectEventsByStudyOID(props.getProperty("studyOID"))
	}

	private def buildResponseMessage(def dataList) {

		def exportResponseMessages = [:]

		if(dataList.size() > 0) {
			exportResponseMessages = client.importData(dataList)
		}
		else {

			log.info("No data items found to export.")
			exportResponseMessages.put("", "No data items found to export.")
		}

		return exportResponseMessages
	}

	@Override
	public HashMap<String, String> exportOpenClinicaStudyData() {

		List<FormData> dataList = dataExportService.getFormDataToExport(ExportConstants.EXPORT_BIT_OPENCLINICA)

		log.info("Running OpenClinica Export Routine to export ${dataList.size()} form data item(s)")

		def exportResponseMessages = buildResponseMessage(dataList)

		for(Map.Entry<String, String> entry : exportResponseMessages.entrySet()) {

			def key = entry.key
			if(exportResponseMessages.get(key).equals("Success")) {
				setFormDataExportedForKey(dataList, key)
			}

			log.info("Export of data item with id: ${key} finished with message: ${exportResponseMessages.get(key)}")
		}

		return exportResponseMessages
	}

	public String exportFormData(User user, FormData formData) {

		log.info("Exporting FormData with id: ${formData.getId()} uploaded by ${user.getName()}")

		this.user = user

		def formDataList = []

		formDataList.add(formData)

		def response = buildResponseMessage(formDataList)

		def formKey = extractKey(formData.getData())

		def resp = response.get(formKey)

		if(resp.equals("Success")) {
			resetExportFlag(formData)
		}

		log.info("Export of Form Data with id: ${formData.getId()} finished with message: ${resp}")

		return response.get(formKey)
	}
	
	private setFormDataExportedForKey(def dataList, def key) {

		dataList.each {
		
			def xml = new XmlSlurper().parseText(it.getData())

			if(key.equals(xml.@formKey.text())) {
				resetExportFlag(it)
			}
		}
	}

	private resetExportFlag(formData) {

		log.info("Resetting Export Flag for form data with id: ${formData.getId()}")

		dataExportService.setFormDataExported(formData, ExportConstants.EXPORT_BIT_OPENCLINICA)

		// To enable working with the form data during testing
		formData.setExportedFlag(ExportConstants.EXPORT_BIT_OPENCLINICA)
	}

	private def extractKey(def xml) {

		def slurpedXml = new XmlSlurper().parseText(xml)

		return slurpedXml.@formKey.toString()
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
	
	public OpenClinicaUser getUserDetails(def username) {
		
		return client.getUserDetails(username)
	}
}
