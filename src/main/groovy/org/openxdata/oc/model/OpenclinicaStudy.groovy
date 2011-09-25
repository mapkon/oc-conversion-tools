package org.openxdata.oc.model

/**
 * Simple representation of the OpenClinica Study.
 */
public class OpenclinicaStudy {
	
	def OID
	def name
	def identifier
	
	public String getOID(){
		return OID
	}
	
	public void setOID(def OID){
		this.OID = OID
	}
	
	public String getName(){
		return name
	}
	
	public void setName(def name){
		this.name = name
	}
	
	public String getIdentifier(){
		return identifier
	}
	
	public void setIdentifier(def identifier){
		this.identifier = identifier
	}
}
