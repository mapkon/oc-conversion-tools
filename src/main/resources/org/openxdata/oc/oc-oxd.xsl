<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:oc="http://www.cdisc.org/ns/odm/v1.3" >
	<xsl:output method="xml" />

	<xsl:template match="/">
		<studyDef>
			<data><xsl:value-of select="oc:ODM/oc:AdminData/oc:User/oc:FullName"></xsl:value-of></data>
			<xsl:apply-templates/>
		</studyDef>
	</xsl:template>

	<xsl:template match="oc:Study">
		<formDef>hello <xsl:value-of select="../oc:AdminData/oc:User/oc:FullName"></xsl:value-of></formDef>
	</xsl:template>
	
	<xsl:template match="oc:AdminData">
		
	</xsl:template>

</xsl:stylesheet>
