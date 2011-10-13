<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : add_bungeniportal_ns.xsl
    Created on : 13 October 2011, 12:42
    Author     : anthony
    Description:
    Adds the bungeni portal namespace to incoming bungeni document
    in the pipeline
-->
<xsl:stylesheet version="2.0"
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     xmlns:bp="http://portal.bungeni.org/1.0/">

    <xsl:template match="* | /*">
        <xsl:element name="bp:{local-name()}">
            <xsl:copy-of select="@*" />
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>



</xsl:stylesheet>