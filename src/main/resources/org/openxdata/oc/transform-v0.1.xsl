<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:oc="http://www.cdisc.org/ns/odm/v1.3"
	xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1"
	xmlns:xf="http://www.w3.org/2002/xforms">
	<xsl:output method="xml" />

	<xsl:template match="/">
		<study>
			<xsl:attribute name="name"><xsl:value-of select="//oc:StudyName" /></xsl:attribute>
			<xsl:attribute name="studyKey"><xsl:value-of select="//oc:Study/@OID" /></xsl:attribute>
			<xsl:attribute name="description"><xsl:value-of
				select="normalize-space(//oc:StudyDescription)" /></xsl:attribute>
			<xsl:for-each select="oc:ODM/oc:Study/oc:MetaDataVersion/oc:StudyEventDef">
				<form>
					<xsl:attribute name="name">
						<xsl:value-of select="@OID" />
					</xsl:attribute>
					<xsl:attribute name="description">
						<xsl:value-of select="@Name" />
					</xsl:attribute>
					<version>
						<xsl:attribute name="name"><xsl:value-of select="@Name" />-v1</xsl:attribute>
						<xsl:attribute name="description">Converted from ODM using the oc-conversion-tools</xsl:attribute>
						<xform>
							<xsl:call-template name="createForm" />
						</xform>
					</version>
				</form>
			</xsl:for-each>
		</study>
	</xsl:template>

	<xsl:template name="createForm">
		<xforms xmlns="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
			<model>
				<xsl:variable name="instanceElementName">
					<xsl:value-of select="../../@OID" />
					-
					<xsl:value-of select="@OID" />
				</xsl:variable>
				<instance>
					<xsl:attribute name="id"><xsl:value-of
						select="normalize-space($instanceElementName)" /></xsl:attribute>
					<ODM>
						<xsl:attribute name="Description">This Xform was converted from an ODM file using the oc-conversion-tools</xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of
							select="@Name" /></xsl:attribute>
						<xsl:attribute name="formKey"><xsl:value-of
							select="normalize-space(@OID)" /></xsl:attribute>
						<xsl:attribute name="id"><xsl:value-of
							select="normalize-space($instanceElementName)" /></xsl:attribute>
						<xsl:attribute name="StudyOID"><xsl:value-of
							select="../../@OID" /></xsl:attribute>
						<xsl:attribute name="StudyEventOID"><xsl:value-of
							select="@OID" /></xsl:attribute>
						<xsl:for-each select="oc:FormRef">

							<xsl:variable name="formId" select="@FormOID" />
							<xsl:attribute name="FormOID"><xsl:value-of select="$formId" /></xsl:attribute>
							<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
								<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
								<xsl:attribute name="ItemGroupOID"><xsl:value-of select="$itemGroupId" /></xsl:attribute>
								<xsl:choose>
									<xsl:when test="//oc:ItemGroupDef[@OID=$itemGroupId]/@Repeating = 'Yes'">
										<xsl:element name="{$itemGroupId}">
											<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
												<xsl:element name="{@ItemOID}" />
											</xsl:for-each>
										</xsl:element>
									</xsl:when>
									<xsl:otherwise>
										<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
											<xsl:element name="{@ItemOID}" />
										</xsl:for-each>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:for-each>
						</xsl:for-each>
					</ODM>
				</instance>
				<xsl:call-template name="createBinds">
					<xsl:with-param name="studyEventId">
						<xsl:value-of select="@OID" />
					</xsl:with-param>
				</xsl:call-template>

			</model>
			<xsl:for-each select="oc:FormRef">
				<xsl:call-template name="createGroup" />
			</xsl:for-each>
		</xforms>
	</xsl:template>

	<xsl:template name="createBinds">
		<xsl:param name="studyEventId" />

		<xsl:for-each select="oc:FormRef">
			<xsl:variable name="formId" select="@FormOID" />
			<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
				<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
				<xsl:choose>
					<xsl:when test="//oc:ItemGroupDef[@OID=$itemGroupId]/@Repeating = 'Yes'">
						<xf:bind>
							<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=@ItemOID]" />

							<xsl:attribute name="id"><xsl:value-of select="$itemGroupId" /></xsl:attribute>
							<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$itemGroupId" /></xsl:attribute>

							<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
								<xf:bind>
									<xsl:attribute name="id"><xsl:value-of select="@ItemOID" /></xsl:attribute>
									<xsl:attribute name="nodeset">/ODM/<xsl:value-of
										select="$itemGroupId" />/<xsl:value-of select="@ItemOID" /></xsl:attribute>
									<xsl:call-template name="determineQuestionType">
										<xsl:with-param name="itemDef" select="../../oc:ItemDef[@OID=@ItemOID]" />
									</xsl:call-template>
								</xf:bind>
							</xsl:for-each>
						</xf:bind>
					</xsl:when>
					<xsl:otherwise>
						<xsl:call-template name="createItemBinds">
							<xsl:with-param name="itemGroupId" select="$itemGroupId" />
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="createItemBinds">
		<xsl:param name="itemGroupId" />
		<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
			<bind>
				<xsl:variable name="itemId" select="@ItemOID" />
				<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=$itemId]" />
				<xsl:attribute name="id"><xsl:value-of select="$itemId" /></xsl:attribute>
				<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$itemId" /></xsl:attribute>
				<xsl:call-template name="determineQuestionType">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:call-template>
			</bind>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="determineQuestionType">
		<xsl:param name="itemDef" />
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
	</xsl:template>

	<xsl:template name="createGroup">
		<group>
			<xsl:attribute name="id"><xsl:value-of select="position()" /></xsl:attribute>
			<xsl:variable name="formId" select="@FormOID" />
			<label>
				<xsl:value-of select="../../oc:FormDef[@OID = $formId]/@Name" />
			</label>
			<xsl:for-each select="../../oc:FormDef[@OID=$formId]/oc:ItemGroupRef">
				<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
				<xsl:call-template name="createQuestions">
					<xsl:with-param name="formId" select="$formId" />
				</xsl:call-template>
			</xsl:for-each>
		</group>
	</xsl:template>

	<xsl:template name="createQuestions">
		<xsl:param name="formId" />
		<xsl:variable name="itemGroupId" select="@ItemGroupOID" />
		<xsl:choose>
			<xsl:when test="//oc:ItemGroupDef[@OID=$itemGroupId]/@Repeating = 'Yes'">
				<xsl:attribute name="id">
					<xsl:value-of select="$itemGroupId" />
				</xsl:attribute>
				<group>
					<xsl:attribute name="id">
						<xsl:value-of select="$itemGroupId" />
					</xsl:attribute>
					<label>
						<xsl:value-of select="../../oc:FormDef[@OID = $formId]/@Name" />
					</label>
					<repeat>
						<xsl:attribute name="bind"><xsl:value-of select="$itemGroupId" /></xsl:attribute>
						<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
							<xsl:variable name="itemOID" select="@ItemOID" />
							<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=$itemOID]" />

							<xsl:call-template name="insertQuestionsAccordingToType">
								<xsl:with-param name="itemOID" select="$itemOID"></xsl:with-param>
								<xsl:with-param name="itemDef" select="$itemDef" />
							</xsl:call-template>
						</xsl:for-each>
					</repeat>
				</group>
			</xsl:when>
			<xsl:otherwise>
				<xsl:for-each select="../../oc:ItemGroupDef[@OID=$itemGroupId]/oc:ItemRef">
					<xsl:variable name="itemId" select="@ItemOID" />
					<xsl:variable name="itemDef" select="../../oc:ItemDef[@OID=$itemId]" />
					<xsl:call-template name="insertQuestionsAccordingToType">
						<xsl:with-param name="itemOID" select="$itemId" />
						<xsl:with-param name="itemDef" select="$itemDef" />
					</xsl:call-template>
				</xsl:for-each>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="insertQuestionsAccordingToType">

		<xsl:param name="itemOID" />
		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/oc:CodeListRef">
				<select1>
					<xsl:attribute name="bind"><xsl:value-of select="$itemOID" /></xsl:attribute>
					<xsl:variable name="codeListID">
						<xsl:value-of select="$itemDef/oc:CodeListRef/@CodeListOID" />
					</xsl:variable>
					<label>
						<xsl:value-of select="normalize-space($itemDef/oc:Question/oc:TranslatedText)"></xsl:value-of>
					</label>
					<xsl:for-each select="../../oc:CodeList[@OID = $codeListID]/oc:CodeListItem">
						<item>
							<xsl:attribute name="id"><xsl:value-of select="@CodedValue" /></xsl:attribute>
							<label>
								<xsl:value-of select="oc:Decode/oc:TranslatedText"></xsl:value-of>
							</label>
							<value>
								<xsl:value-of select="@CodedValue"></xsl:value-of>
							</value>
						</item>
					</xsl:for-each>
				</select1>
			</xsl:when>
			<xsl:otherwise>
				<input>
					<xsl:attribute name="bind"><xsl:value-of select="$itemOID" /></xsl:attribute>
					<label>
						<xsl:value-of select="normalize-space($itemDef/oc:Question/oc:TranslatedText)"></xsl:value-of>
					</label>
					<xsl:if test="$itemDef/oc:MeasurementUnitRef">
						<hint>
							<xsl:variable name="unitId">
								<xsl:value-of select="$itemDef/oc:MeasurementUnitRef/@MeasurementUnitOID" />
							</xsl:variable>
							<xsl:value-of select="//oc:MeasurementUnit[@OID=$unitId]/oc:Symbol/oc:TranslatedText" />
						</hint>
					</xsl:if>
				</input>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
