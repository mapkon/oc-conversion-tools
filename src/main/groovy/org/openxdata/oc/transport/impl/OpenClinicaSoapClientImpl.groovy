package org.openxdata.oc.transport.impl

import groovy.util.logging.Log
import groovy.xml.XmlUtil

import java.util.Collection

import org.openxdata.oc.Transform
import org.openxdata.oc.exception.ErrorCode
import org.openxdata.oc.exception.ImportException
import org.openxdata.oc.model.ConvertedOpenclinicaStudy
import org.openxdata.oc.transport.OpenClinicaSoapClient
import org.openxdata.oc.transport.proxy.ImportWebServiceProxy
import org.openxdata.oc.transport.proxy.ListAllByStudyWebServiceProxy
import org.openxdata.oc.transport.proxy.ListAllWebServiceProxy
import org.openxdata.oc.transport.proxy.StudyMetaDataWebServiceProxy


@Log
public class OpenClinicaSoapClientImpl implements OpenClinicaSoapClient {

	
	private def importProxy
	private def listAllProxy
	private def getMetaDataProxy
	private def listAllByStudyProxy
		
	def OpenClinicaSoapClientImpl(def username, def password, def connectionFactory){
		
		log.info("Initialized Openclinica Soap Client.")
				
		importProxy = new ImportWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		listAllProxy = new ListAllWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		getMetaDataProxy = new StudyMetaDataWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
		listAllByStudyProxy = new ListAllByStudyWebServiceProxy(username:username, hashedPassword:password, connectionFactory:connectionFactory)
	}

	public String getMetadata(String identifier) {
		
		return getMetaDataProxy.getMetaData(identifier)
	}
	
	public List<ConvertedOpenclinicaStudy> listAll(){		
		
		return listAllProxy.listAll()
	}
	
	public def importData(Collection<String> instanceData){
		
		return importProxy.importData(instanceData);
	}
		
	public Collection<String> getSubjectKeys(String studyIdentifier){
		
		return listAllByStudyProxy.listAllByStudy(studyIdentifier)
	}

	public def getOpenxdataForm(String studyOID) {
		
		try{
			
			log.info("Fetching Form for Openclinica study with ID: " + studyOID)
			
			def odmMetaData = getMetadata(studyOID)
			def xformXml = convertODM(odmMetaData)
			
			return XmlUtil.asString(xformXml)
			
		}catch(def ex){
		log.info("Failed with Exception: ${ex.getMessage()}")
			throw new ImportException(ErrorCode.XML_PARSE_EXCEPTION)
		}
	}

	private convertODM(def odmMetaData) {
		
		def convertedStudy = transformMetaData(odmMetaData)
		log.info("<< ODM To OpenXData Transformation Complete. Returning... >>")

		return convertedStudy.convertedXformXml
	}

	private transformMetaData(def odmMetaData) {
		
		def convertedStudy = Transform.getTransformer().ConvertODMToXform(odmMetaData)

		convertedStudy.appendSubjectKeyNode([:])
		convertedStudy.parseMeasurementUnits()
		convertedStudy.serializeXformNode()
		
		return convertedStudy
	}
}
