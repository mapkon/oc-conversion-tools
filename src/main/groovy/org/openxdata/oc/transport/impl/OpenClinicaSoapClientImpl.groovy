package org.openxdata.oc.transport.impl

import groovy.util.logging.Log

import java.util.Collection

import org.openxdata.oc.Transform
import org.openxdata.oc.exception.UnAvailableException
import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.factory.ConnectionFactory
import org.openxdata.oc.transport.proxy.ImportWebServiceProxy
import org.openxdata.oc.transport.proxy.ListAllByStudyWebServiceProxy
import org.openxdata.oc.transport.proxy.ListAllWebServiceProxy
import org.openxdata.oc.transport.proxy.StudyMetaDataWebServiceProxy


@Log
public class OpenClinicaSoapClientImpl implements OpenClinicaSoapClient {

	def username
	def password
	
	private def connectionFactory

	OpenClinicaSoapClientImpl(def userName, def password){
		log.info("Initialized Openclinica Soap Client.")
		
		this.username = userName
		this.password = password
	}

	public List<ConvertedOpenclinicaStudy> listAll(){
		
		def listAllProxy = new ListAllWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return listAllProxy.listAll()
	}
	
	public String getMetadata(String identifier) {
		def getMetaDataProxy = new StudyMetaDataWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return getMetaDataProxy.getMetaData(identifier)
	}
	
	public Collection<String> getSubjectKeys(String studyIdentifier){
		def listAllByStudyProxy = new ListAllByStudyWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return listAllByStudyProxy.listAllByStudy(studyIdentifier)
	}
	
	public def getOpenxdataForm(String studyOID) {
		
		log.info("Fetching Form for Openclinica study with ID: " + studyOID)
		
		def odmMetaData = getMetadata(studyOID)
		def convertedStudy = transformMetaData(odmMetaData)
		
		log.info("<< ODM To OpenXData Transformation Complete. Returning... >>")
		
		return convertedStudy.convertedXformXml
	}

	private transformMetaData(String odmMetaData) {
		
		def convertedStudy = Transform.getTransformer().ConvertODMToXform(odmMetaData)

		convertedStudy.appendSubjectKeyNode([:])
		convertedStudy.parseMeasurementUnits()
		convertedStudy.serializeXformNode()
		
		return convertedStudy
	}
		
	public def importData(Collection<String> instanceData){
		def importProxy = new ImportWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		return importProxy.importData(instanceData);
	}
	
	public void setConnectionFactory(ConnectionFactory factory){
		this.connectionFactory = factory
	}
}
