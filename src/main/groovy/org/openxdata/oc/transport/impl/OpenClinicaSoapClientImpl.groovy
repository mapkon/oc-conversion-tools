package org.openxdata.oc.transport.impl

import groovy.util.logging.Log

import java.util.Collection

import org.openxdata.oc.Transform
import org.openxdata.oc.exception.ErrorCode
import org.openxdata.oc.exception.ParseException
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.proxy.CRFMetaDataVersionProxy
import org.openxdata.oc.transport.proxy.EventWebServiceProxy;
import org.openxdata.oc.transport.proxy.ImportWebServiceProxy
import org.openxdata.oc.transport.proxy.ListAllSubjectsByStudyWebServiceProxy
import org.openxdata.oc.util.PropertiesUtil


@Log
public class OpenClinicaSoapClientImpl implements OpenClinicaSoapClient {

	
	def username
	def password
	def connectionFactory
	
	private def importProxy
	private def eventsProxy
	private def listAllByStudyProxy
	private def crfMetaDataVersionProxy
			
	def OpenClinicaSoapClientImpl(def connectionFactory){
		
		log.info("Initializing Openclinica Soap Client.")
		
		this.connectionFactory = connectionFactory
			
		def util = new PropertiesUtil()
		def props = util.loadProperties('META-INF/openclinica.properties')
		
		username = props.getAt('username')
		password = props.getAt('password')
		
		init()
		
	}
	
	void init() {
		
		eventsProxy = new EventWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		importProxy = new ImportWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		listAllByStudyProxy = new ListAllSubjectsByStudyWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		crfMetaDataVersionProxy = new CRFMetaDataVersionProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
	}

	def findAllCRFS(def studyOID) {
		return crfMetaDataVersionProxy.findAllCRFS(studyOID)
	}
	
	public def importData(Collection<String> instanceData){
		
		return importProxy.importData(instanceData);
	}
		
	public Collection<String> getSubjectKeys(def identifier){
		
		return listAllByStudyProxy.listAllByStudy(identifier)
	}

	public def getOpenxdataForm(def studyOID) {
		
		try{

			log.info("Fetching Latest CRF Version for Openclinica study with OID: ${studyOID}")

			def xform = getXform(studyOID)

			return xform
		}catch(def ex){
			log.info("Failed with Exception: ${ex.getMessage()}")
			throw new ParseException(ErrorCode.XML_PARSE_EXCEPTION)
		}
	}

	private getXform(def studyOID) {

		def odmMetaData = findAllCRFS(studyOID)
		def convertedXform = Transform.getTransformer().ConvertODMToXform(odmMetaData)

		return convertedXform
	}
	
	def findEventsByStudyOID(def studyOID) {
		return eventsProxy.findEventsByStudyOID(studyOID)
	}
}
