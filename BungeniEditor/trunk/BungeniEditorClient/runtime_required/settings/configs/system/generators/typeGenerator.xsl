<?xml version="1.0" encoding="UTF-8"?>
<!-- We use the meta: prefix to distinguish between the xsl: namespace used by the xslt
    and the xslt we want to generate -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> May 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok Hariharan</xd:p>
            <xd:p>This is a meta template generator for Transforming ODF sections 
                into AKoma Ntoso Sections</xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:output method="xml" indent="yes" />
    <!-- the namespace-alias call switches the output namespace back to the xsl namespace,
        this way for development we use the meta namespace and the output is always rendered
        as the xsl: namespace -->
    <xsl:namespace-alias stylesheet-prefix="xmeta" result-prefix="xsl"/>

    <xsl:template match="allConfigs">
        <xmeta:stylesheet
           xmlns:xs="http://www.w3.org/2001/XMLSchema"
           xmlns:meta="http://meta.w3.org/1999/XSL/Transform"
           xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
           xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
           version="2.0"
        >
            <xsl:apply-templates />
        </xmeta:stylesheet>
    </xsl:template>

    <xsl:template match="inlineTypes">
        <!-- INLINE TYPES -->
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="sectionTypes">
        <!-- SECTION TYPES -->
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="text()">
        <!-- strip extra whitespace from text nodes
            (including leading and trailing whitespace) -->
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>
    
    
    <xsl:template match="sectionType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="inlineType">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="metadatas">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="meta">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="output">
        <xsl:variable name="typename" select="ancestor::*/@name"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:comment>
            <xsl:text>&#xa;</xsl:text>
            <xsl:text>template for </xsl:text><xsl:value-of select="$typename" />
            <xsl:text>&#xa;</xsl:text>
        </xsl:comment>
        <xsl:text>&#xa;</xsl:text>
        
        <xmeta:template>
            <xsl:attribute name="match"> 
                <xsl:text>*[@name='</xsl:text>
                <xsl:value-of select="$typename" />
                <xsl:text>']</xsl:text>
            </xsl:attribute>
            <xsl:apply-templates />
        </xmeta:template>

    </xsl:template>

    <xsl:template match="content">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="*[parent::content]">
        <xsl:variable name="elemname"  select="local-name()"/>

        
        <xmeta:element name="{$elemname}">
            <xsl:for-each select="@*">
                <!-- process attributes -->
                <xmeta:attribute name="{local-name()}" >
                    <xsl:choose>
                        <!-- 
                            Attributes are processed as follows :
                                values starting with #$
                                values starting with $
                                values with literals
                                -->
                        <xsl:when test="starts-with(.,'#')">
                            <!-- possibly add a check to see if the metadata exists in the parent -->
                            <xmeta:text>#</xmeta:text>
                            <xmeta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'#$') )}" />
                        </xsl:when>
                        <xsl:when test="starts-with(.,'$')">
                            <xmeta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'$') )}" />
                        </xsl:when>
                        <xsl:otherwise>
                            <xmeta:text><xsl:value-of select="." /></xmeta:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </xmeta:attribute>
            </xsl:for-each>
            <xmeta:apply-templates />
        </xmeta:element>
    </xsl:template>


</xsl:stylesheet>
