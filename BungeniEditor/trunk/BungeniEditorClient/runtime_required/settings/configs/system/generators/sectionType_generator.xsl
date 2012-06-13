<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:meta="http://meta.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> May 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok Hariharan</xd:p>
            <xd:p>This is a meta template generator for Transforming ODF sections 
                into AKoma Ntoso Sections</xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:template match="sectionTypes">
        <meta:stylesheet
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           xmlns:meta="http://meta.w3.org/1999/XSL/Transform"
           exclude-result-prefixes="xs" 
           version="2.0"
        >
            <xsl:apply-templates />
        </meta:stylesheet>
    </xsl:template>

    <xsl:template match="inlineTypes">
        <xsl:apply-templates />
    </xsl:template>


    <xsl:template match="sectionType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="inlineType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="output" >
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="metadatas">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="meta">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="output">
        <xsl:variable name="typename" select="ancestor::sectionType/@name"/>
        <meta:template>
            <xsl:attribute name="match">
                <xsl:text>*[@name='</xsl:text>
                <xsl:value-of select="$typename" />
                <xsl:text>']</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates />
        </meta:template>

    </xsl:template>

    <xsl:template match="content">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*[parent::content]">
        <xsl:variable name="elemname"  select="local-name()"/>
        <meta:element name="{$elemname}">
            <xsl:for-each select="@*">
                <!-- process attributes -->
                <meta:attribute name="{local-name()}" >
                    <xsl:choose>
                        <xsl:when test="starts-with(.,'#')">
                            <!-- possibly add a check to see if the metadata exists in the parent -->
                            <meta:text>#</meta:text>
                            <meta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'#$') )}" />
                        </xsl:when>
                        <xsl:otherwise>
                            <meta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'$') )}" />
                        </xsl:otherwise>
                    </xsl:choose>
                </meta:attribute>
            </xsl:for-each>
            <meta:apply-templates />
        </meta:element>
    </xsl:template>


</xsl:stylesheet>
