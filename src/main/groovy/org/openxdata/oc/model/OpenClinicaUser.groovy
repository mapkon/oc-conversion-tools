package org.openxdata.oc.model

class OpenClinicaUser {

	private def xml

	private def username
	private def hashedPassword
	private def canUseWebServices
	
	private def allowedStudies = []

	OpenClinicaUser(def xml){

		this.xml = xml

		this.username = xml.username.text()
		this.hashedPassword = xml.hashedPassword.text()
		this.canUseWebServices = Boolean.valueOf(xml.canUseWebServices.text())

	}

	def getAllowedStudies() {
		
		def allowedStudies = []
		
		def oids = xml.allowedStudyOid.each {
			allowedStudies.add(it.text())
		}
		
		return allowedStudies
	}
}
