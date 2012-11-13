package org.openxdata.xform

import groovy.util.logging.Log

import org.openxdata.server.admin.model.FormDef
import org.openxdata.server.admin.model.FormDefVersion
import org.openxdata.server.admin.model.StudyDef


@Log
class StudyImporter {

	def xml
	def study = new StudyDef()
	
	def StudyImporter(def xml) {
		this.xml = xml
	}

	def extractStudy() {

		log.info("Extracting study properties...")
		
		setStudyName()
		setStudyDescription()
		setStudyKey()

		setStudyFormsAndPotentiallyFormVersions()

		return study
	}

	private setStudyName(){
		
		log.info("Setting Study Name")
		
		def studyName = xml.@name.text()
		study.setName(studyName)
	}

	private setStudyDescription(){
		
		log.info("Setting Study Description")
		
		def studyDescription = xml.@description.text()
		study.setDescription(studyDescription)
	}

	private setStudyKey(){
		
		log.info("Setting Study Key")
		
		def studyKey = xml.@studyKey.text()
		study.setStudyKey(studyKey)
	}

	private setStudyFormsAndPotentiallyFormVersions(){
		def forms = []

		xml.form.each {
			def form = createForm(it)
			forms.add(form)
		}

		study.setForms(forms)
	}

	private createForm(def formNode) {
		
		log.info("Creating Study Form(s)")
		
		def form = new FormDef()

		def formName = formNode.@name.text()
		form.setName(formName)

		def formDescription = formNode.@description.text()
		form.setDescription(formDescription)
		
		def formVersions = createFormVersions(formNode)
		
		form.setVersions(formVersions)

		return form
	}
	
	private createFormVersions(def formNode){
		
		log.info("Creating Study Form Versions")
		
		def versions = []
		formNode.version.each {
			def version = extractFormVersion(it)
			versions.add(version)
		}
		
		return versions
	}
	
	private extractFormVersion(def versionNode){
		
		def version = new FormDefVersion()
		
		def versionName = versionNode.@name.text()
		version.setName(versionName)
		
		def versionDescription = versionNode.@description.text()
		version.setDescription(versionDescription)
		
		def xform = versionNode.xform[0].text()
		
		xform = xform.replaceFirst("<?xml version=\"1.0\" encoding=\"UTF-8\"?>null", "")
		
		version.setXform(xform)
		
		return version
	}
}
