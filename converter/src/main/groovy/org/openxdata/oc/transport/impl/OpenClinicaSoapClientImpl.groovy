package org.openxdata.oc.transport.impl

import groovy.util.logging.Log

import org.openxdata.oc.Transformer
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.model.OpenClinicaUser
import org.openxdata.oc.model.StudySubject
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.soap.proxy.CRFMetaDataVersionProxy
import org.openxdata.oc.transport.soap.proxy.DataWebServiceProxy
import org.openxdata.oc.transport.soap.proxy.ImportWebServiceProxy
import org.openxdata.oc.transport.soap.proxy.SubjectEventWebServiceProxy
import org.openxdata.server.admin.model.FormData
import org.openxdata.server.admin.model.User


@Log
public class OpenClinicaSoapClientImpl implements OpenClinicaSoapClient {

	
	def props
	def username
	def password
	def connectionFactory
	
	private def dataProxy
	private def importProxy
	private def eventsProxy
	private def crfMetaDataVersionProxy
	private def studySubjectEventsProxy
			
	def OpenClinicaSoapClientImpl(Properties props) {

		log.info("Initializing Soap Client...")
		
		this.props = props
		
		def host = props.getAt('host')
		username = props.getAt('username')
		password = props.getAt('password')
		
		connectionFactory = new ConnectionFactory(host:host)

	}

	def findAllCRFS(def studyOID) {
		
		crfMetaDataVersionProxy = new CRFMetaDataVersionProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return crfMetaDataVersionProxy.findAllCRFS(studyOID)
	}
	
	private getXform(def studyOID) {

		log.info("Fetching Latest CRF Version for Openclinica study with OID: ${studyOID}")

		def transformer = new Transformer()
		
		def odmMetaData = findAllCRFS(studyOID)
		
		return transformer.convert(odmMetaData)

	}

	public def getOpenxdataForm(def studyOID) {
		
		try{

			return getXform(studyOID)

		}catch(def ex){

			log.info(ex.getMessage())
			throw new ImportException(ex.getMessage())
		}
	}

	OpenClinicaUser getUserDetails(def username) {
		
		dataProxy = new DataWebServiceProxy(username:this.username, hashedPassword:password, connectionFactory:connectionFactory)
		
		return dataProxy.getUserDetails(username)
	}
	
	List<StudySubject> findStudySubjectEventsByStudyOID(def studyOID){

		studySubjectEventsProxy = new SubjectEventWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		def eventNode = studySubjectEventsProxy.findStudySubjectEventsByStudyOIDRequest(studyOID)

		def subjects = []
		
		eventNode.studySubject.each {
			
			def subject = new StudySubject(it)
			subjects.add(subject)
		}

		return subjects
	}
	
	HashMap<String, String> importData(User user, List<FormData> instanceData){
		
		if(user) {
			importProxy = new ImportWebServiceProxy(username:user.getName(), hashedPassword:user.getSecretAnswer(), connectionFactory:connectionFactory)
		} else {
			importProxy = new ImportWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		}
		
		return importProxy.importData(instanceData)
	}
}
