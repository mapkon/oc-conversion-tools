<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:odm="http://www.cdisc.org/ns/odm/v1.3"
	xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1"
	xmlns:xf="http://www.w3.org/2002/xforms">

	<xsl:output method="xml" />

	<xsl:template match="/">
		<study>
			<xsl:attribute name="name"><xsl:value-of select="//odm:StudyName" /></xsl:attribute>
			<xsl:attribute name="studyKey"><xsl:value-of select="//odm:Study/@OID" /></xsl:attribute>
			<xsl:attribute name="description"><xsl:value-of
				select="normalize-space(//odm:StudyDescription)" /></xsl:attribute>
			<xsl:for-each select="odm:ODM/odm:Study/odm:MetaDataVersion/odm:FormDef">
				<form>
					<xsl:attribute name="name">
						<xsl:value-of select="@Name" />
					</xsl:attribute>
					<xsl:attribute name="description">
						<xsl:value-of select="@OID" />
					</xsl:attribute>
					<version>
						<xsl:attribute name="name"><xsl:value-of
							select="@Name" />-v1</xsl:attribute>
						<xsl:attribute name="description">Converted from ODM using the oc-conversion-tools</xsl:attribute>
						<xform>
							<xsl:call-template name="createXForm" />
						</xform>
					</version>
				</form>
			</xsl:for-each>
		</study>
	</xsl:template>

	<xsl:template name="createXForm">
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
						<xsl:attribute name="StudyOID"><xsl:value-of
							select="../../@OID" /></xsl:attribute>
						<xsl:attribute name="StudyEventOID"><xsl:value-of
							select="@OID" /></xsl:attribute>
						<xsl:attribute name="MetaDataVersionOID"><xsl:value-of
							select="../@OID" /></xsl:attribute>

						<xsl:element name="SubjectKey" />

						<xsl:for-each select="odm:ItemGroupRef">

							<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
							<xsl:variable name="itemGroupDef"
								select="../../odm:ItemGroupDef[@OID=$itemGroupOID]" />

							<xsl:choose>
								<xsl:when test="$itemGroupDef/@Repeating = 'Yes'">
									<xsl:element name="{$itemGroupOID}">
										<xsl:for-each select="$itemGroupDef/odm:ItemRef">
											<xsl:element name="{@ItemOID}" />
										</xsl:for-each>
									</xsl:element>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="$itemGroupDef/odm:ItemRef">
										<xsl:element name="{@ItemOID}">
											<xsl:attribute name="ItemGroupOID"><xsl:value-of
												select="$itemGroupOID" /></xsl:attribute>
										</xsl:element>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</ODM>
				</instance>
				<xsl:call-template name="createBinds" />

			</model>
			<group id="1">
				<label>Subject key</label>
				<input bind="subjectKeyBind">
					<label>Subject Key</label>
					<hint>The subject key for whom you are collecting data for.</hint>
				</input>
			</group>

			<xsl:for-each select="odm:ItemGroupRef">
				<xsl:call-template name="createGroup" />
			</xsl:for-each>
		</xforms>
	</xsl:template>

	<xsl:template name="createBinds">

		<bind id="subjectKeyBind" nodeset="/ODM/SubjectKey" type="xsd:string"
			required="true()" locked="true()" visible="false()"></bind>

		<xsl:for-each select="odm:ItemGroupRef">
			<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
			<xsl:variable name="itemGroupDef"
				select="../../odm:ItemGroupDef[@OID=$itemGroupOID]" />
			<xsl:choose>
				<xsl:when test="$itemGroupDef/@Repeating = 'Yes'">

					<bind>

						<xsl:attribute name="id"><xsl:value-of
							select="$itemGroupOID" /></xsl:attribute>
						<xsl:attribute name="nodeset">/ODM/<xsl:value-of
							select="$itemGroupOID" /></xsl:attribute>
					</bind>

					<xsl:for-each select="$itemGroupDef/odm:ItemRef">
						<bind>
							<xsl:attribute name="id"><xsl:value-of
								select="@ItemOID" /></xsl:attribute>
							<xsl:attribute name="nodeset">/ODM/<xsl:value-of
								select="$itemGroupOID" />/<xsl:value-of select="@ItemOID" /></xsl:attribute>
							<xsl:call-template name="determineBindQuestionType">
								<xsl:with-param name="itemDef"
									select="../../odm:ItemDef[@OID=@ItemOID]" />
							</xsl:call-template>

							<xsl:if test="./Mandatory = 'Yes'">
								<xsl:attribute name="required">true()</xsl:attribute>
							</xsl:if>
						</bind>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:call-template name="createItemBinds">
						<xsl:with-param name="itemGroupDef" select="$itemGroupDef" />
					</xsl:call-template>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="createItemBinds">
		<xsl:param name="itemGroupDef" />
		<xsl:for-each select="$itemGroupDef/odm:ItemRef">
			<bind>
				<xsl:variable name="itemId" select="@ItemOID" />
				<xsl:variable name="itemDef" select="../../odm:ItemDef[@OID=$itemId]" />
				<xsl:attribute name="id"><xsl:value-of select="$itemId" /></xsl:attribute>
				<xsl:attribute name="nodeset">/ODM/<xsl:value-of
					select="$itemId" /></xsl:attribute>
				<xsl:call-template name="determineBindQuestionType">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:call-template>

				<xsl:if test="./@Mandatory = 'Yes'">
					<xsl:attribute name="required">true()</xsl:attribute>
				</xsl:if>
			</bind>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="determineBindQuestionType">
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

			<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
			<xsl:variable name="itemGroupDef"
				select="../../odm:ItemGroupDef[@OID=$itemGroupOID]" />

			<xsl:attribute name="id"><xsl:value-of select="position()+1" /></xsl:attribute>
			<label>
				<xsl:value-of select="$itemGroupDef/@Name" />
			</label>

			<xsl:variable name="repeating" select="$itemGroupDef/@Repeating" />

			<xsl:choose>
				<xsl:when test="$repeating = 'Yes'">
					<group>
						<xsl:attribute name="id">
							<xsl:value-of select="$itemGroupDef/@OID" />
						</xsl:attribute>
						<label>
							<xsl:value-of select="$itemGroupDef/@OID" />
						</label>
						<repeat>
							<xsl:attribute name="bind"><xsl:value-of
								select="$itemGroupDef/@OID" /></xsl:attribute>
							<xsl:for-each select="$itemGroupDef/odm:ItemRef">
								<xsl:call-template name="createQuestions" />
							</xsl:for-each>
						</repeat>
					</group>
				</xsl:when>
				<xsl:otherwise>
					<xsl:for-each select="$itemGroupDef/odm:ItemRef">
						<xsl:call-template name="createQuestions" />
					</xsl:for-each>
				</xsl:otherwise>
			</xsl:choose>

		</group>
	</xsl:template>

	<xsl:template name="createQuestions">

		<xsl:variable name="itemId" select="@ItemOID" />
		<xsl:variable name="itemDef" select="../../odm:ItemDef[@OID=$itemId]" />
		<xsl:call-template name="insertQuestionsTextAndBind">
			<xsl:with-param name="itemOID" select="$itemId" />
			<xsl:with-param name="itemDef" select="$itemDef" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="insertQuestionsTextAndBind">

		<xsl:param name="itemOID" />
		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:CodeListRef">
				<select1>
					<xsl:attribute name="bind"><xsl:value-of
						select="$itemOID" /></xsl:attribute>
					<xsl:variable name="codeListID">
						<xsl:value-of select="$itemDef/odm:CodeListRef/@CodeListOID" />
					</xsl:variable>
					<label>
						<xsl:call-template name="createQuestionText">
							<xsl:with-param name="itemDef" select="$itemDef" />
						</xsl:call-template>
					</label>
					<xsl:for-each
						select="../../odm:CodeList[@OID = $codeListID]/odm:CodeListItem">
						<item>
							<xsl:attribute name="id"><xsl:value-of
								select="@CodedValue" /></xsl:attribute>
							<label>
								<xsl:value-of select="odm:Decode/odm:TranslatedText"></xsl:value-of>
							</label>
							<value>
								<xsl:value-of select="@CodedValue"></xsl:value-of>
							</value>
						</item>
					</xsl:for-each>
					<hint>
						<xsl:call-template name="createHintText">
							<xsl:with-param name="itemDef" select="$itemDef" />
						</xsl:call-template>
					</hint>
				</select1>
			</xsl:when>
			<xsl:otherwise>
				<input>
					<xsl:attribute name="bind"><xsl:value-of
						select="$itemOID" /></xsl:attribute>
					<label>
						<xsl:call-template name="createQuestionText">
							<xsl:with-param name="itemDef" select="$itemDef" />
						</xsl:call-template>
					</label>
					<hint>
						<xsl:call-template name="createHintText">
							<xsl:with-param name="itemDef" select="$itemDef" />
						</xsl:call-template>
					</hint>
				</input>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="createQuestionText">

		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:Question/@OpenClinica:QuestionNumber">
				<xsl:variable name="questionNumber"
					select="$itemDef/odm:Question/@OpenClinica:QuestionNumber" />

				<xsl:value-of select="$questionNumber" />
				<xsl:value-of
					select="normalize-space($itemDef/odm:Question/odm:TranslatedText)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of
					select="normalize-space($itemDef/odm:Question/odm:TranslatedText)" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="createHintText">

		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:MeasurementUnitRef">
				<xsl:variable name="measurementUnitOID">
					<xsl:value-of select="$itemDef/odm:MeasurementUnitRef/@MeasurementUnitOID" />
				</xsl:variable>
				<xsl:value-of
					select="//odm:MeasurementUnit[@OID=$measurementUnitOID]/odm:Symbol/odm:TranslatedText" />
			</xsl:when>
			<xsl:otherwise >
				<xsl:value-of select="$itemDef/@Comment" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
