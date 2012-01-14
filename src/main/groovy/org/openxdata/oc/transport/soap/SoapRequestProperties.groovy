package org.openxdata.oc.transport.soap

abstract class SoapRequestProperties { 

	def username
	def hashedPassword
	def connectionFactory
	
	def getHeader(){
		"""<soapenv:Header xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
			<wsse:Security soapenv:mustUnderstand="1" xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
			   <wsse:UsernameToken wsu:Id="UsernameToken-27777511" xmlns:wsu="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd">
			   <wsse:Username>${username}</wsse:Username>
			  <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">${hashedPassword}</wsse:Password>
			 </wsse:UsernameToken>
			</wsse:Security></soapenv:Header>"""
	}
	
	def abstract getSoapEnvelope()
}
