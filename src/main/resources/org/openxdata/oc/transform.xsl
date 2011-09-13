<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:oc="http://www.cdisc.org/ns/odm/v1.3"
	xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1"
	xmlns:xf="http://www.w3.org/2002/xforms">
	<xsl:output method="xml" />

	<xsl:template match="/">
		<study>
			<xsl:attribute name="description">
				<xsl:value-of select="normalize-space(//oc:StudyDescription)"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of select="//oc:StudyName"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="studyKey"> <xsl:value-of select="//oc:Study/@OID" /></xsl:attribute>

			<xsl:for-each select="oc:ODM/oc:Study/oc:MetaDataVersion/oc:StudyEventDef">
				<form>
					<xsl:attribute name="description">
						<xsl:value-of select="@Name"></xsl:value-of>
					</xsl:attribute>
					<xsl:attribute name="name">
						<xsl:value-of select="@Name"></xsl:value-of>
					</xsl:attribute>
					<version>
						<xsl:attribute name="name"><xsl:value-of
							select="@Name" />-v1</xsl:attribute>
						<xsl:call-template name="createForm" />
					</version>
				</form>
			</xsl:for-each>

		</study>
	</xsl:template>

	<xsl:template name="createForm">

		<xf:xforms xmlns:xf="http://www.w3.org/2002/xforms"
			xmlns:xsd="http://www.w3.org/2001/XMLSchema">

			<xf:model>
				<xsl:variable name="instanceElementName">
					<xsl:value-of select="../../@OID" />-<xsl:value-of select="@OID"/>
				</xsl:variable>
				<xf:instance>
					<xsl:attribute name="id"><xsl:value-of
						select="$instanceElementName" /></xsl:attribute>
					<ODM>
						<xsl:attribute name="Description">converted from ODM to Xform</xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of
							select="@Name"></xsl:value-of></xsl:attribute>
						<xsl:attribute name="formKey"><xsl:value-of
							select="@OID"></xsl:value-of></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of select="$instanceElementName"/></xsl:attribute>

						<ClinicalData>
							<xsl:attribute name="StudyOID"><xsl:value-of select="../../@OID"/></xsl:attribute>
							<xsl:attribute name="MetaDataVersionOID"><xsl:value-of select="../@OID"/></xsl:attribute>
							<SubjectData SubjectKey="">
		            <StudyEventData>
		            	<xsl:attribute name="StudyEventOID"><xsl:value-of select="@OID"/></xsl:attribute>
		            	
	                
										<xsl:for-each select="oc:FormRef">
											<xsl:variable name="formId" select="@FormOID" />
											
											<FormData>
												<xsl:attribute name="FormOID"><xsl:value-of select="$formId"/></xsl:attribute>
																					
												<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
													<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
													<ItemGroupData>
													<xsl:attribute name="ItemGroupOID"><xsl:value-of select="$itemGroupId"/></xsl:attribute>
													<xsl:for-each
														select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
														<ItemData value="">
															<xsl:attribute name="ItemOID"><xsl:value-of
																select="@ItemOID"></xsl:value-of></xsl:attribute>
														</ItemData>
													</xsl:for-each>
													</ItemGroupData>
												</xsl:for-each>
											
											</FormData>
											
										</xsl:for-each>
								
									
		            </StudyEventData>
	        		</SubjectData>
						</ClinicalData>
					</ODM>
				</xf:instance>

				<xsl:call-template name="createBinds" >
					<xsl:with-param name="studyEventId"><xsl:value-of select="@OID"/></xsl:with-param>
				</xsl:call-template>

			</xf:model>
			<xf:group id="1">
				<xf:label>Subject key</xf:label>
				<xf:input bind="subjectKeyBind">
					<xf:label>Subject Key</xf:label>
				</xf:input>			
			</xf:group>
			<xsl:for-each select="oc:FormRef">
				<xsl:call-template name="createGroup" />
			</xsl:for-each>
		</xf:xforms>

	</xsl:template>

	<xsl:template name="createBinds">
		<xsl:param name="studyEventId"/>
		<xsl:message><xsl:value-of select="$studyEventId"/></xsl:message>
	
		<xf:bind id="subjectKeyBind" nodeset="/ODM/ClinicalData/SubjectData/@SubjectKey" type="xsd:string"></xf:bind>

		<xsl:for-each select="oc:FormRef">
			<xsl:variable name="formId" select="@FormOID" />
			<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
				<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
				<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
					<xf:bind>
						<xsl:variable name="itemId" select="@ItemOID" />
						<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=$itemId]" />
						<xsl:attribute name="id"><xsl:value-of
							select="$itemId" /></xsl:attribute>
						<xsl:attribute name="nodeset">/ODM/ClinicalData/SubjectData/StudyEventData[@StudyEventOID=<xsl:value-of
						  select="$studyEventId"/>]/FormData[@FormOID=<xsl:value-of
							select="$formId"/>]/ItemGroupData[@ItemGroupOID=<xsl:value-of
							select="$itemGroupId"/>]/ItemData[@ItemOID='<xsl:value-of
							select="$itemId"/>']/@Value</xsl:attribute>

						<xsl:choose>
							<xsl:when test="$itemDef/@DataType = 'integer'">
								<xsl:attribute name="type">xsd:int</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'float'">
								<xsl:attribute name="type">xsd:decimal</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'date'">
								<xsl:attribute name="type">xsd:date</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'time'">
								<xsl:attribute name="type">xsd:time</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'datetime'">
								<xsl:attribute name="type">xsd:dateTime</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'boolean'">
								<xsl:attribute name="type">xsd:boolean</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'double'">
								<xsl:attribute name="type">xsd:decimal</xsl:attribute>
							</xsl:when>
							<xsl:when test="$itemDef/@DataType = 'base64Binary'">
								<xsl:attribute name="type">xsd:base64Binary</xsl:attribute>
							</xsl:when>
							<xsl:otherwise>
								<xsl:attribute name="type">xsd:string</xsl:attribute>
							</xsl:otherwise>
						</xsl:choose>

					</xf:bind>
				</xsl:for-each>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="createGroup">
		<group>

			<xsl:attribute name="id"><xsl:value-of select="position()+1" /></xsl:attribute>
			<xsl:variable name="formId" select="@FormOID" />
			<label>
				<xsl:value-of select="../../oc:FormDef[@OID = $formId]/@Name"/>
			</label>
			
			<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
				<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
				<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
					<xsl:variable name="itemId">
						<xsl:value-of select="@ItemOID" />
					</xsl:variable>
					<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=$itemId]" />
					<xsl:choose>
						<xsl:when test="$itemDef/oc:CodeListRef">
							<xf:select1>
								<xsl:attribute name="bind"><xsl:value-of
									select="$itemId" /></xsl:attribute>
								<xsl:variable name="codeListID">
									<xsl:value-of select="$itemDef/oc:CodeListRef/@CodeListOID" />
								</xsl:variable>
								<xf:label>
									<xsl:value-of
										select="normalize-space($itemDef/oc:Question/oc:TranslatedText)"></xsl:value-of>
								</xf:label>
								<xsl:for-each
									select="../../oc:CodeList[@OID = $codeListID]/oc:CodeListItem">
									<xf:item>
										<xsl:attribute name="id"><xsl:value-of
											select="@CodedValue" /></xsl:attribute>

										<xf:label>
											<xsl:value-of select="oc:Decode/oc:TranslatedText"></xsl:value-of>
										</xf:label>
										<xf:value>
											<xsl:value-of select="@CodedValue"></xsl:value-of>
										</xf:value>
									</xf:item>
								</xsl:for-each>
							</xf:select1>
						</xsl:when>
						<xsl:otherwise>

							<xf:input>
								<xsl:attribute name="bind"><xsl:value-of
									select="$itemId" /></xsl:attribute>


								<xf:label>
									<xsl:value-of
										select="normalize-space($itemDef/oc:Question/oc:TranslatedText)"></xsl:value-of>
								</xf:label>
							</xf:input>

						</xsl:otherwise>
					</xsl:choose>
				</xsl:for-each>
			</xsl:for-each>

		</group>
	</xsl:template>

</xsl:stylesheet>
