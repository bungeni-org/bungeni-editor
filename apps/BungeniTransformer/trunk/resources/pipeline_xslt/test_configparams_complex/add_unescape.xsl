<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : add_unescape.xsl
    Created on : 28 October 2011, 14:19
    Author     : anthony
    Description:
    Adds unescape on html-tagged content items
-->
<xsl:stylesheet version="2.0"
     xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
     xmlns:xhtml="http://www.w3.org/1999/xhtml"
     xmlns:bp="http://www.bungeni.org/pipeline/1.0" 
     xmlns:bu="http://portal.bungeni.org/1.0/">
    
    <xsl:output method="xml" use-character-maps="html-unescape" />
    <xsl:character-map name="html-unescape">
        <xsl:output-character character="&lt;" string="&lt;"/>
        <xsl:output-character character="&gt;" string="&gt;"/>
    </xsl:character-map>    

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

    <xsl:template match="bu:body">
        <xsl:element name="{name()}">
        <xsl:element name="div" namespace="http://www.w3.org/1999/xhtml">
            <xsl:value-of select="." />
        </xsl:element>
        </xsl:element>
    </xsl:template> 



</xsl:stylesheet>