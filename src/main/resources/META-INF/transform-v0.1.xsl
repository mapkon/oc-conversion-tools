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

				<xsl:variable name="vFormOID" select="@OID" />

				<xsl:variable name="vSections" select="//*[local-name()='ItemDef']/*/*/*[local-name()='SectionLabel'][generate-id() = generate-id(key('kLabelsInForm', concat($vFormOID, '+', .))[1])]">
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
								<xsl:with-param name="pSections" select="$vSections"></xsl:with-param>
							</xsl:apply-templates>
						</xform>
					</version>
				</form>
			</xsl:for-each>
		</study>
	</xsl:template>

	<!-- Create data nodes for the question answers -->
	<xsl:template match="//*[local-name()='FormDef']">

		<xsl:param name="pSections" />
		<xsl:variable name="vForm" select="current()" />

		<xforms xmlns="http://www.w3.org/2002/xforms" xmlns:xsd="http://www.w3.org/2001/XMLSchema">
			
			<xsl:apply-templates select="$vForm" mode="createModel">
				<xsl:with-param name="pForm" select="$vForm" />
			</xsl:apply-templates>

			<xsl:call-template name="createGroups">
				<xsl:with-param name="pForm" select="$vForm" />
				<xsl:with-param name="pSections" select="$pSections" />
			</xsl:call-template>
			
		</xforms>
	</xsl:template>

	<!-- Create the Model element -->
	<xsl:template match="//*[local-name()='FormDef']" mode="createModel">

		<xsl:param name="pForm" />

		<model>
			<xsl:variable name="vInstanceElementName">
				<xsl:value-of select="@OID" />
			</xsl:variable>
			<instance>
				<xsl:attribute name="id"><xsl:value-of select="normalize-space($vInstanceElementName)" /></xsl:attribute>
				<ODM>
					<xsl:attribute name="Description">This Xform was converted from an ODM file using the oc-conversion-tools</xsl:attribute>
					<xsl:attribute name="name"><xsl:value-of select="@Name" /></xsl:attribute>
					<xsl:attribute name="formKey"><xsl:value-of select="@OID" /></xsl:attribute>
					<xsl:attribute name="StudyOID"><xsl:value-of select="../../@OID" /></xsl:attribute>
					<xsl:attribute name="MetaDataVersionOID"><xsl:value-of select="../@OID" /></xsl:attribute>

					<xsl:element name="SubjectKey" />

					<xsl:for-each select="odm:ItemGroupRef">

						<xsl:variable name="vItemGroupOID" select="@ItemGroupOID" />
						<xsl:variable name="vItemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$vItemGroupOID and */*/@FormOID=$pForm/@OID]" />

						<xsl:choose>
							<xsl:when test="$vItemGroupDef/@Repeating = 'Yes'">
								<xsl:element name="{@ItemGroupOID}">
									<xsl:for-each select="$vItemGroupDef/odm:ItemRef">
										<xsl:element name="{@ItemOID}" />
									</xsl:for-each>
								</xsl:element>
							</xsl:when>
							<xsl:otherwise>
								<xsl:for-each select="$vItemGroupDef/odm:ItemRef">
									<xsl:element name="{@ItemOID}">
										<xsl:attribute name="ItemGroupOID">
										<xsl:value-of select="$vItemGroupOID" />
									</xsl:attribute>
									</xsl:element>
								</xsl:for-each>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</ODM>
			</instance>

			<bind id="subjectKey" nodeset="/ODM/SubjectKey" type="xsd:string" required="true()" locked="true()" visible="false()" />

			<xsl:apply-templates select="child::node()">
				<xsl:with-param name="pForm" select="$pForm" />
			</xsl:apply-templates>
		</model>

	</xsl:template>

	<!-- Create Binds for the questions -->
	<xsl:template match="//*[local-name()='ItemGroupRef']">

		<xsl:param name="pForm" />

		<xsl:variable name="vItemGroupOID" select="@ItemGroupOID" />
		<xsl:variable name="vItemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$vItemGroupOID and */*/@FormOID=$pForm/@OID]" />

		<xsl:choose>
			<xsl:when test="$vItemGroupDef/@Repeating = 'Yes'">

				<bind>
					<xsl:attribute name="id"><xsl:value-of select="$vItemGroupOID" /></xsl:attribute>
					<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$vItemGroupOID" /></xsl:attribute>
				</bind>

				<xsl:for-each select="$vItemGroupDef/odm:ItemRef">
					<bind>
						<xsl:attribute name="id"><xsl:value-of select="@ItemOID" /></xsl:attribute>
						<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="$vItemGroupOID" />/<xsl:value-of select="@ItemOID" /></xsl:attribute>
						<xsl:call-template name="determineBindQuestionType">
							<xsl:with-param name="pItemDef" select="//*[local-name()='ItemDef' and @OID=@ItemOID]" />
						</xsl:call-template>

						<xsl:if test="./Mandatory = 'Yes'">
							<xsl:attribute name="required">true()</xsl:attribute>
						</xsl:if>
					</bind>
				</xsl:for-each>
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="createItemBinds">
					<xsl:with-param name="pItemGroupDef" select="$vItemGroupDef" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="createItemBinds">

		<xsl:param name="pItemGroupDef" />

		<xsl:for-each select="$pItemGroupDef/odm:ItemRef">
			<bind>
				<xsl:variable name="vItemDef" select="//*[local-name()='ItemDef' and @OID=@ItemOID]" />
				<xsl:attribute name="id"><xsl:value-of select="@ItemOID" /></xsl:attribute>
				<xsl:attribute name="nodeset">/ODM/<xsl:value-of select="@ItemOID" /></xsl:attribute>
				<xsl:call-template name="determineBindQuestionType">
					<xsl:with-param name="pItemDef" select="$vItemDef" />
				</xsl:call-template>

				<xsl:if test="./@Mandatory = 'Yes'">
					<xsl:attribute name="required">true()</xsl:attribute>
				</xsl:if>
			</bind>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="determineBindQuestionType">
		<xsl:param name="pItemDef" />
		<xsl:choose>
			<xsl:when test="$pItemDef/@DataType = 'integer'">
				<xsl:attribute name="type">xsd:int</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'float'">
				<xsl:attribute name="type">xsd:decimal</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'date'">
				<xsl:attribute name="type">xsd:date</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'time'">
				<xsl:attribute name="type">xsd:time</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'datetime'">
				<xsl:attribute name="type">xsd:dateTime</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'boolean'">
				<xsl:attribute name="type">xsd:boolean</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'double'">
				<xsl:attribute name="type">xsd:decimal</xsl:attribute>
			</xsl:when>
			<xsl:when test="$pItemDef/@DataType = 'base64Binary'">
				<xsl:attribute name="type">xsd:base64Binary</xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:attribute name="type">xsd:string</xsl:attribute>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- Create Groups from OpenClinica Section Labels -->
	<xsl:template name="createGroups">

		<xsl:param name="pForm" />
		<xsl:param name="pSections" />
		
		<xsl:for-each select="$pSections">
		
			<xsl:variable name="vSection" select="current()" />
			
			<group>
	
				<xsl:attribute name="id"><xsl:value-of select="position()" /></xsl:attribute>
				<label>
					<xsl:value-of select="//*[local-name()='ItemDef']/*/*/*[local-name()='SectionTitle' and ../OpenClinica:SectionLabel=$vSection]" />
				</label>
	
				<!-- Add the subject key input field only to the first group. -->
				<xsl:if test="position() = '1'">
					<input bind="subjectKey">
						<label>Subject Key</label>
						<hint>The subject key for whom you are collecting data for.</hint>
					</input>
				</xsl:if>
	
				<xsl:for-each select="$pForm/odm:ItemGroupRef">
	
					<xsl:variable name="vItemGroupOID" select="@ItemGroupOID" />
					<xsl:variable name="vItemGroupDef" select="//*[local-name()='ItemGroupDef' and @OID=$vItemGroupOID and */*/@FormOID=$pForm/@OID]" />
	
					<xsl:apply-templates select="$vItemGroupDef">
						<xsl:with-param name="pForm" select="$pForm" />
						<xsl:with-param name="pSection" select="$vSection" />
						<xsl:with-param name="pItemGroupDef" select="$vItemGroupDef" />
					</xsl:apply-templates>
	
				</xsl:for-each>
				
			</group>
			
		</xsl:for-each>

	</xsl:template>

	<!-- Create repeat questions -->
	<xsl:template match="//*[local-name()='ItemGroupDef' and @Repeating='Yes']">
		
		<xsl:param name="pForm" />
		<xsl:param name="pSection" />
		<xsl:param name="pItemGroupDef" />
		
		<xsl:if test="$pItemGroupDef/*/*/@SectionLabel=$pSection">
			<group>
				<xsl:attribute name="id"><xsl:value-of select="$pItemGroupDef/@OID" /></xsl:attribute>
				<label>
					<xsl:value-of select="$pItemGroupDef/*/*/*[local-name()='ItemGroupHeader']" />
				</label>

				<xf:repeat>

					<xsl:attribute name="bind"><xsl:value-of select="$pItemGroupDef/@OID" /></xsl:attribute>

					<xsl:for-each select="$pItemGroupDef/odm:ItemRef">

						<xsl:variable name="vItemOID" select="@ItemOID"></xsl:variable>
						<xsl:variable name="vItemDef" select="//*[local-name()='ItemDef' and @OID=$vItemOID]" />

						<xsl:if test="$vItemDef/*/*[@FormOID=$pForm/@OID]/*[local-name()='SectionLabel']=$pSection">
							<xsl:apply-templates select=".">
								<xsl:with-param name="pItemDef" select="$vItemDef" />
							</xsl:apply-templates>
						</xsl:if>
					</xsl:for-each>
				</xf:repeat>
			</group>
		</xsl:if>
	</xsl:template>

	<!-- Create non-repeat questions -->
	<xsl:template match="//*[local-name()='ItemGroupDef' and @Repeating='No']">

		<xsl:param name="pForm" />
		<xsl:param name="pSection" />
		<xsl:param name="pItemGroupDef" />

		<xsl:for-each select="$pItemGroupDef/odm:ItemRef">
			<xsl:variable name="vItemOID" select="@ItemOID"></xsl:variable>
			<xsl:variable name="vItemDef" select="//*[local-name()='ItemDef' and @OID=$vItemOID]" />

			<xsl:if test="$vItemDef/*/*[@FormOID=$pForm/@OID]/*[local-name()='SectionLabel']=$pSection">
				<xsl:apply-templates select=".">
					<xsl:with-param name="pItemDef" select="$vItemDef" />
				</xsl:apply-templates>
			</xsl:if>
		</xsl:for-each>
	</xsl:template>

	<!-- Create either single select or input type of questions. -->
	<xsl:template match="//*[local-name()='ItemRef']">

		<xsl:param name="pItemDef" />

		<xsl:choose>
			<xsl:when test="$pItemDef/odm:CodeListRef">
				<xsl:apply-templates select="$pItemDef/*[local-name()='CodeListRef']">
					<xsl:with-param name="pItemDef" select="$pItemDef" />
				</xsl:apply-templates>
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="$pItemDef" mode="createInputQuestions">
					<xsl:with-param name="pItemDef" select="$pItemDef" />
				</xsl:apply-templates>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="//*[local-name()='ItemDef']" mode="createInputQuestions">

		<xsl:param name="pItemDef" />

		<input>
			<xsl:attribute name="bind"><xsl:value-of select="$pItemDef/@OID" /></xsl:attribute>
			<label>
				<xsl:variable name="vLText">
					<xsl:apply-templates select="$pItemDef">
						<xsl:with-param name="pItemDef" select="$pItemDef" />
					</xsl:apply-templates>
				</xsl:variable>
				<xsl:value-of select="normalize-space($vLText)" />
			</label>
			<hint>
				<xsl:apply-templates select="$pItemDef" mode="createHintText">
					<xsl:with-param name="pItemDef" select="$pItemDef" />
				</xsl:apply-templates>
			</hint>
		</input>
	</xsl:template>

	<!-- Create single select questions -->
	<xsl:template match="//*[local-name()='ItemDef']/*[local-name()='CodeListRef']">
	
		<xsl:param name="pItemDef" />
		
		<select1>
			<xsl:attribute name="bind"><xsl:value-of select="$pItemDef/@OID" /></xsl:attribute>
			<xsl:variable name="codeListID">
				<xsl:value-of select="$pItemDef/odm:CodeListRef/@CodeListOID" />
			</xsl:variable>
			<label>
				<xsl:variable name="lText">
					<xsl:apply-templates select="$pItemDef">
						<xsl:with-param name="pItemDef" select="$pItemDef" />
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
				<xsl:apply-templates select="$pItemDef" mode="createHintText">
					<xsl:with-param name="pItemDef" select="$pItemDef" />
				</xsl:apply-templates>
			</hint>
		</select1>
	</xsl:template>
	
	<!-- Append question number to the label text if it exists, else use translated text-->
	<xsl:template match="//*[local-name()='ItemDef']">

		<xsl:param name="pItemDef" />

		<xsl:choose>
			<xsl:when test="$pItemDef/odm:Question/@OpenClinica:QuestionNumber">
				<xsl:value-of select="normalize-space($pItemDef/odm:Question/@OpenClinica:QuestionNumber)" />
				<xsl:value-of select="normalize-space($pItemDef/odm:Question/odm:TranslatedText)" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="normalize-space($pItemDef/odm:Question/odm:TranslatedText)" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="//*[local-name()='ItemDef']" mode="createHintText">

		<xsl:param name="pItemDef" />

		<xsl:choose>
			<xsl:when test="$pItemDef/odm:MeasurementUnitRef">
				<xsl:variable name="vMeasurementUnitOID">
					<xsl:value-of select="$pItemDef/odm:MeasurementUnitRef/@MeasurementUnitOID" />
				</xsl:variable>
				<xsl:value-of select="//odm:MeasurementUnit[@OID=$vMeasurementUnitOID]/odm:Symbol/odm:TranslatedText" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$pItemDef/@Comment" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
