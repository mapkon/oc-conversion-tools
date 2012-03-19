package org.openxdata.oc.transport.impl

import groovy.util.logging.Log

import org.openxdata.oc.Transform
import org.openxdata.oc.exception.TransformationException
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.soap.proxy.CRFMetaDataVersionProxy
import org.openxdata.oc.transport.soap.proxy.EventWebServiceProxy
import org.openxdata.oc.transport.soap.proxy.ImportWebServiceProxy
import org.openxdata.server.admin.model.FormData


@Log
public class OpenClinicaSoapClientImpl implements OpenClinicaSoapClient {

	
	def props
	def username
	def password
	def connectionFactory
	
	private def importProxy
	private def eventsProxy
	private def crfMetaDataVersionProxy
			
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
	
	public String importData(List<FormData> instanceData){
		
		importProxy = new ImportWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return importProxy.importData(instanceData)
	}
		
	public def getOpenxdataForm(def studyOID) {
		
		try{

			return getXform(studyOID)

		}catch(def ex){
			log.info(ex.getMessage())
			throw new TransformationException(ex.getMessage())
		}
	}

	private getXform(def studyOID) {

		log.info("Fetching Latest CRF Version for Openclinica study with OID: ${studyOID}")
		
		def odmMetaData = findAllCRFS(studyOID)
		
		def convertedXform = Transform.getTransformer().ConvertODMToXform(odmMetaData)

		return convertedXform
	}
	
	def findEventsByStudyOID(def studyOID) {
		
		eventsProxy = new EventWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return eventsProxy.findEventsByStudyOID(studyOID)
	}
}
