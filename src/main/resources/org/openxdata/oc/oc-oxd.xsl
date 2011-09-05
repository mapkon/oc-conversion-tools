<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:oc="http://www.cdisc.org/ns/odm/v1.3"
	xmlns:OpenClinica="http://www.openclinica.org/ns/odm_ext_v130/v3.1">
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
			<xsl:apply-templates select="oc:ODM/oc:Study" />
		</study>
	</xsl:template>

	<xsl:template match="oc:Study">
		<form>
			<xsl:attribute name="description">
				<xsl:value-of
				select="normalize-space(oc:MetaDataVersion/oc:FormDef/OpenClinica:FormDetails/OpenClinica:VersionDescription)"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of
				select="normalize-space(oc:MetaDataVersion/oc:FormDef/@Name)"></xsl:value-of>
			</xsl:attribute>
			<xsl:apply-templates select="oc:MetaDataVersion" />
		</form>
	</xsl:template>

	<xsl:template match="oc:MetaDataVersion">
		<version>
			<xsl:attribute name="description">
				<xsl:value-of select="normalize-space(@Name)"></xsl:value-of>
			</xsl:attribute>
			<xsl:attribute name="name">
				<xsl:value-of select="normalize-space(@OID)"></xsl:value-of>
			</xsl:attribute>
			<xsl:for-each select="oc:FormDef">
				<xform>
					<xsl:variable name="xform">

						<xsl:call-template name="createForm" />

					</xsl:variable>
					<xsl:copy-of select="$xform" />
				</xform>
			</xsl:for-each>

		</version>
	</xsl:template>

	<xsl:template name="createForm">

		<xf:xforms xmlns:xf="http://www.w3.org/2002/xforms"
			xmlns:xsd="http://www.w3.org/2001/XMLSchema">

			<xf:model>
				<xf:instance id="ODM">
					<ODM xmlns="http://www.cdisc.org/ns/odm/v1.3" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
						xsi:schemaLocation="http://www.cdisc.org/ns/odm/v1.3 ODM1-3.xsd"
						ODMVersion="1.3" FileType="Snapshot" Description="">
						<xsl:attribute name="FileOID"><xsl:value-of
							select="current-date()"></xsl:value-of></xsl:attribute>
						<xsl:attribute name="CreationDateTime"><xsl:value-of
							select="current-date()"></xsl:value-of></xsl:attribute>
						<xsl:attribute name="name"><xsl:value-of
							select="oc:FormDef/@Name"></xsl:value-of></xsl:attribute>
						<xsl:attribute name="formKey"><xsl:value-of
							select="oc:FormDef/@OID"></xsl:value-of></xsl:attribute>
						<ClinicalData StudyOID="" MetaDataVersionOID="v1.0.0"
							UserID="">
							<SubjectData SubjectKey="">
								<StudyEventData StudyEventOID="">
									<FormData>
										<xsl:attribute name="FormOID"><xsl:value-of
											select="oc:FormDef/@OID"></xsl:value-of></xsl:attribute>
										<xsl:for-each select="oc:ItemGroupDef">
											<xsl:call-template name="createItemGroupData"></xsl:call-template>
										</xsl:for-each>
									</FormData>
								</StudyEventData>
							</SubjectData>
						</ClinicalData>
					</ODM>
				</xf:instance>

				<xsl:call-template name="createBinds" />

			</xf:model>
			<xsl:for-each select="oc:ItemGroupRef">
				<xsl:call-template name="createGroup">
				</xsl:call-template>
			</xsl:for-each>
		</xf:xforms>

	</xsl:template>

	<xsl:template name="createBinds">

	</xsl:template>

	<xsl:template name="createGroup">
		<group>
			<xsl:attribute name="id"><xsl:value-of select="position()"></xsl:value-of></xsl:attribute>

			<xsl:variable name="groupId">
				<xsl:value-of select="@ItemGroupOID" />
			</xsl:variable>
			<xsl:for-each select="//oc:ItemGroupDef[@OID = $groupId]/oc:ItemRef">
				<xsl:variable name="itemId">
					<xsl:value-of select="@ItemOID" />
				</xsl:variable>
				<xsl:for-each select="//oc:ItemDef[@OID = $itemId]">
					<item>
						<xsl:attribute name="name"><xsl:value-of
							select="@Name" /></xsl:attribute>
					</item>
				</xsl:for-each>
			</xsl:for-each>
		</group>
	</xsl:template>

	<xsl:template name="createItemGroupData">
		<ItemGroupData TransactionType="Insert">
			<xsl:attribute name="ItemGroupOID"><xsl:value-of select="@OID"></xsl:value-of></xsl:attribute>
		</ItemGroupData>
	</xsl:template>
</xsl:stylesheet>
