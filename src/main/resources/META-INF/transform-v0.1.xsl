<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:odm="http://www.cdisc.org/ns/odm/v1.3"
	xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1"
	xmlns:xf="http://www.w3.org/2002/xforms">

	<xsl:output method="xml" version='1.0' encoding='UTF-8' indent="yes" omit-xml-declaration="yes" />
	<xsl:strip-space elements="*"/>
	
	<xsl:key name="kLabelsInForm" match="OpenClinica:SectionLabel" use="concat(../@FormOID, '+', .)" />

	<xsl:template match="/">
		<study>
			<xsl:attribute name="name"><xsl:value-of select="//odm:StudyName" /></xsl:attribute>
			<xsl:attribute name="studyKey"><xsl:value-of select="//odm:Study/@OID" /></xsl:attribute>
			<xsl:attribute name="description"><xsl:value-of select="normalize-space(//odm:StudyDescription)" /></xsl:attribute>


			<xsl:for-each select="//*[local-name()='FormDef']">

				<xsl:variable name="formOID" select="@OID" />

				<xsl:variable name="sections"
					select="//*[local-name()='ItemDef']/*/*/*[local-name()='SectionLabel'][generate-id() = generate-id(key('kLabelsInForm', concat($formOID, '+', .))[1])]">
					<xsl:value-of select="concat(., ' ')" />
				</xsl:variable>

				<form>
					<xsl:attribute name="name">
						<xsl:value-of select="@Name" />
					</xsl:attribute>
					<xsl:attribute name="description">
						<xsl:value-of select="@OID" />
					</xsl:attribute>
					<version>
						<xsl:attribute name="name"><xsl:value-of select="@Name" />-v1</xsl:attribute>
						<xsl:attribute name="description">Converted from ODM using the oc-conversion-tools</xsl:attribute>
						<xform>
							<xsl:apply-templates select="current()">
								<xsl:with-param name="sections" select="$sections"></xsl:with-param>
							</xsl:apply-templates>
						</xform>
					</version>
				</form>
			</xsl:for-each>
		</study>
	</xsl:template>

	<!-- Create data nodes for the question answers -->
	<xsl:template match="//*[local-name()='FormDef']">

		<xsl:param name="sections" />
		<xsl:variable name="form" select="current()" />

		<xforms xmlns="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
			<model>
				<xsl:variable name="instanceElementName">
					<xsl:value-of select="@OID" />
				</xsl:variable>
				<instance>
					<xsl:attribute name="id"><xsl:value-of select="normalize-space($instanceElementName)" /></xsl:attribute>
					<ODM>
						<xsl:attribute name="Description">This Xform was converted from an ODM file using the oc-conversion-tools</xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of select="@Name" /></xsl:attribute>
						<xsl:attribute name="formKey"><xsl:value-of select="@OID" /></xsl:attribute>
						<xsl:attribute name="StudyOID"><xsl:value-of select="../../@OID" /></xsl:attribute>
						<xsl:attribute name="MetaDataVersionOID"><xsl:value-of select="../@OID" /></xsl:attribute>

						<xsl:element name="SubjectKey" />

						<xsl:for-each select="odm:ItemGroupRef">

							<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
							<xsl:variable name="itemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$itemGroupOID and */*/@FormOID=$form/@OID]" />

							<xsl:choose>
								<xsl:when test="$itemGroupDef/@Repeating = 'Yes'">
									<xsl:element name="{@ItemGroupOID}">
										<xsl:for-each select="$itemGroupDef/odm:ItemRef">
											<xsl:element name="{@ItemOID}" />
										</xsl:for-each>
									</xsl:element>
								</xsl:when>
								<xsl:otherwise>
									<xsl:for-each select="$itemGroupDef/odm:ItemRef">
										<xsl:element name="{@ItemOID}">
											<xsl:attribute name="ItemGroupOID"><xsl:value-of select="$itemGroupOID" /></xsl:attribute>
										</xsl:element>
									</xsl:for-each>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:for-each>
					</ODM>
				</instance>
				
				<bind id="subjectKey" nodeset="/ODM/SubjectKey" type="xsd:string" required="true()" locked="true()" visible="false()" />
				
				<xsl:apply-templates select="child::node()">
					<xsl:with-param name="form" select="$form" />
				</xsl:apply-templates>

			</model>

			<xsl:for-each select="$sections">
				<xsl:call-template name="createGroup">
					<xsl:with-param name="form" select="$form" />
				</xsl:call-template>
			</xsl:for-each>
		</xforms>
	</xsl:template>

	<!-- Create Binds for the questions -->
	<xsl:template match="//*[local-name()='ItemGroupRef']">

		<xsl:param name="form" />

		<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
		<xsl:variable name="itemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$itemGroupOID and */*/@FormOID=$form/@OID]" />

		<xsl:choose>
			<xsl:when test="$itemGroupDef/@Repeating = 'Yes'">

				<bind>
					<xsl:attribute name="id"><xsl:value-of select="$itemGroupOID" /></xsl:attribute>
					<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$itemGroupOID" /></xsl:attribute>
				</bind>

				<xsl:for-each select="$itemGroupDef/odm:ItemRef">
					<bind>
						<xsl:attribute name="id"><xsl:value-of select="@ItemOID" /></xsl:attribute>
						<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$itemGroupOID" />/<xsl:value-of select="@ItemOID" /></xsl:attribute>
						<xsl:call-template name="determineBindQuestionType">
							<xsl:with-param name="itemDef" select="//*[local-name()='ItemDef' and @OID=@ItemOID]" />
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
	</xsl:template>

	<xsl:template name="createItemBinds">

		<xsl:param name="itemGroupDef" />

		<xsl:for-each select="$itemGroupDef/odm:ItemRef">
			<bind>
				<xsl:variable name="itemDef" select="//*[local-name()='ItemDef' and @OID=@ItemOID]" />
				<xsl:attribute name="id"><xsl:value-of select="@ItemOID" /></xsl:attribute>
				<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="@ItemOID" /></xsl:attribute>
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

		<xsl:param name="form" />
		<xsl:variable name="section" select="current()" />

		<group>

			<xsl:attribute name="id"><xsl:value-of select="position()" /></xsl:attribute>
			<label>
				<xsl:value-of select="//*[local-name()='ItemDef']/*/*/*[local-name()='SectionTitle' and ../OpenClinica:SectionLabel=$section]" />
			</label>

			<!-- Add the subject key input field only to the first group. -->
			<xsl:if test="position() = '1'">
				<input bind="subjectKey">
					<label>Subject Key</label>
					<hint>The subject key for whom you are collecting data for.</hint>
				</input>
			</xsl:if>

			<xsl:for-each select="$form/odm:ItemGroupRef">

				<xsl:variable name="itemGroupOID" select="@ItemGroupOID" />
				<xsl:variable name="itemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$itemGroupOID and */*/@FormOID=$form/@OID]" />

				<xsl:apply-templates select="$itemGroupDef">
					<xsl:with-param name="form" select="$form" />
					<xsl:with-param name="section" select="$section" />
					<xsl:with-param name="itemGroupDef" select="$itemGroupDef" />
				</xsl:apply-templates>
				
			</xsl:for-each>
		</group>

	</xsl:template>

	<!-- Create repeat questions -->
	<xsl:template match="//*[local-name()='ItemGroupDef' and @Repeating='Yes']">
		
		<xsl:param name="form" />
		<xsl:param name="section" />
		<xsl:param name="itemGroupDef" />
		
		<xsl:if test="$itemGroupDef/*/*/@SectionLabel=$section">
			<group>
				<xsl:attribute name="id"><xsl:value-of select="$itemGroupDef/@OID" /></xsl:attribute>
				<label>
					<xsl:value-of select="$itemGroupDef/*/*/*[local-name()='ItemGroupHeader']" />
				</label>

				<xf:repeat>

					<xsl:attribute name="bind"><xsl:value-of select="$itemGroupDef/@OID" /></xsl:attribute>

					<xsl:for-each select="$itemGroupDef/odm:ItemRef">

						<xsl:variable name="itemOID" select="@ItemOID"></xsl:variable>
						<xsl:variable name="itemDef" select="//*[local-name()='ItemDef' and @OID=$itemOID]" />

						<xsl:if test="$itemDef/*/*[@FormOID=$form/@OID]/*[local-name()='SectionLabel']=$section">
							<xsl:apply-templates select=".">
								<xsl:with-param name="itemDef" select="$itemDef" />
							</xsl:apply-templates>
						</xsl:if>
					</xsl:for-each>
				</xf:repeat>
			</group>
		</xsl:if>
	</xsl:template>

	<!-- Create non-repeat questions -->
	<xsl:template match="//*[local-name()='ItemGroupDef' and @Repeating='No']">

		<xsl:param name="form" />
		<xsl:param name="section" />
		<xsl:param name="itemGroupDef" />

		<xsl:for-each select="$itemGroupDef/odm:ItemRef">
			<xsl:variable name="itemOID" select="@ItemOID"></xsl:variable>
			<xsl:variable name="itemDef" select="//*[local-name()='ItemDef' and @OID=$itemOID]" />

			<xsl:if test="$itemDef/*/*[@FormOID=$form/@OID]/*[local-name()='SectionLabel']=$section">
				<xsl:apply-templates select=".">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Create either single select or input type of questions. -->
	<xsl:template match="//*[local-name()='ItemRef']">

		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:CodeListRef">
				<xsl:apply-templates select="$itemDef/*[local-name()='CodeListRef']">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$itemDef" mode="createInputQuestions">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="//*[local-name()='ItemDef']" mode="createInputQuestions">

		<xsl:param name="itemDef" />

		<input>
			<xsl:attribute name="bind"><xsl:value-of select="$itemDef/@OID" /></xsl:attribute>
			<label>
				<xsl:variable name="lText">
					<xsl:apply-templates select="$itemDef">
						<xsl:with-param name="itemDef" select="$itemDef" />
					</xsl:apply-templates>
				</xsl:variable>
				<xsl:value-of select="normalize-space($lText)" />
			</label>
			<hint>
				<xsl:apply-templates select="$itemDef" mode="createHintText">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:apply-templates>
			</hint>
		</input>
	</xsl:template>

	<!-- Create single select questions -->
	<xsl:template match="//*[local-name()='ItemDef']/*[local-name()='CodeListRef']">
	
		<xsl:param name="itemDef" />
		
		<select1>
			<xsl:attribute name="bind"><xsl:value-of select="$itemDef/@OID" /></xsl:attribute>
			<xsl:variable name="codeListID">
				<xsl:value-of select="$itemDef/odm:CodeListRef/@CodeListOID" />
			</xsl:variable>
			<label>
				<xsl:variable name="lText">
					<xsl:apply-templates select="$itemDef">
						<xsl:with-param name="itemDef" select="$itemDef" />
					</xsl:apply-templates>
				</xsl:variable>

				<xsl:value-of select="normalize-space($lText)" />
			</label>
			<xsl:for-each select="../../odm:CodeList[@OID = $codeListID]/odm:CodeListItem">
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
				<xsl:apply-templates select="$itemDef" mode="createHintText">
					<xsl:with-param name="itemDef" select="$itemDef" />
				</xsl:apply-templates>
			</hint>
		</select1>
	</xsl:template>
	
	<!-- Append question number to the label text -->
	<xsl:template match="//*[local-name()='ItemDef']">

		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:Question/@OpenClinica:QuestionNumber">
				<xsl:value-of select="normalize-space($itemDef/odm:Question/@OpenClinica:QuestionNumber)" />
				<xsl:value-of select="normalize-space($itemDef/odm:Question/odm:TranslatedText)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="normalize-space($itemDef/odm:Question/odm:TranslatedText)" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="//*[local-name()='ItemDef']" mode="createHintText">

		<xsl:param name="itemDef" />

		<xsl:choose>
			<xsl:when test="$itemDef/odm:MeasurementUnitRef">
				<xsl:variable name="measurementUnitOID">
					<xsl:value-of select="$itemDef/odm:MeasurementUnitRef/@MeasurementUnitOID" />
				</xsl:variable>
				<xsl:value-of select="//odm:MeasurementUnit[@OID=$measurementUnitOID]/odm:Symbol/odm:TranslatedText" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$itemDef/@Comment" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
