<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : content.xsl
    Created on : 17 October 2011, 13:27
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
    
    <xsl:template match="bu:content" bp:name="content">
        <mainContent>
            <block name="description">
                <xsl:value-of select="./bu:field[@name='body_text']" />                
            </block>
        </mainContent>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 
    
</xsl:stylesheet>
