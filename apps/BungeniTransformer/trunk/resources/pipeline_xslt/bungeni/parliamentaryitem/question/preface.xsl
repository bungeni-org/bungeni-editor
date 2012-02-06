<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : frbrexpression.xsl
    Created on : 14 October 2011, 17:00
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
    
    
    <xsl:template name="preface" bp:name="preface">
        <xsl:variable name="parl_status"><xsl:value-of select="./bu:meta/bu:field[@name='status']" /></xsl:variable>
        <preface>
            <block name="preface">
                <docDate refersTo="#election-date" >
                    <xsl:attribute name="date"><xsl:value-of select="./bu:content/bu:field[@name='status_date']" /></xsl:attribute>
                </docDate>
                
                <!-- for active parliaments, dissoltion date may not be there -->
                <xsl:if test="$parl_status ne 'active'">
                    <docDate date="2011-10-13" refersTo="#dissolution-date"/>                    
                </xsl:if>
                
                <docDate refersTo="#status-date">
                    <xsl:attribute name="date"><xsl:value-of select="./bu:content/bu:field[@name='status_date']" /></xsl:attribute>
                </docDate>
                <docTitle refersTo="#short-name"><xsl:value-of select="//bu:field[@name='short_name']" /></docTitle>
                <docTitle refersTo="#full-name"><xsl:value-of select="//bu:field[@name='full_name']" /></docTitle>
            </block>
        </preface>
    </xsl:template>    
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 
    
</xsl:stylesheet>
