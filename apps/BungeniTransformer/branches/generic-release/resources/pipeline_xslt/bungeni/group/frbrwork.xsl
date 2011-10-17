<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : frbrwork.xsl
    Created on : 14 October 2011, 16:16
    Author     : anthony
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns:bu="http://portal.bungeni.org/1.0/"
    exclude-result-prefixes="bp"
    version="2.0"> 
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="bu:contenttype" bp:name="FRBRWork">
        <FRBRWork>
            <FRBRthis value="/ken/government/{bu:field[@name='start_date']}/{position()}>/main"/>
            <FRBRuri value="/ken/government/{bu:field[@name='start_date']}/{position()}>/main"/>
            <FRBRdate date="{bu:field[@name='start_date']}" name="#Par"/>
            <FRBRauthor href="#Author"/>
        </FRBRWork>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
