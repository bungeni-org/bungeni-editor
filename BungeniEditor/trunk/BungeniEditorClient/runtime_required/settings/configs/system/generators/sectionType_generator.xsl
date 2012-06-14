<?xml version="1.0" encoding="UTF-8"?>
<!-- We use the meta: prefix to distinguish between the xsl: namespace used by the xslt
    and the xslt we want to generate -->
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
    
    <xsl:output method="xml" indent="yes" />
    <!-- the namespace-alias call switches the output namespace back to the xsl namespace,
        this way for development we use the meta namespace and the output is always rendered
        as the xsl: namespace -->
    <xsl:namespace-alias stylesheet-prefix="meta" result-prefix="xsl"/>

    <xsl:template match="allConfigs">
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
        <xsl:variable name="typename" select="ancestor::*/@name"/>
        <xsl:text>&#xa;</xsl:text>
        <xsl:comment>
            <xsl:text>&#xa;</xsl:text>
            <xsl:text>template for </xsl:text><xsl:value-of select="$typename" />
            <xsl:text>&#xa;</xsl:text>
        </xsl:comment>
        <xsl:text>&#xa;</xsl:text>
        
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
                        <!-- 
                            Attributes are processed as follows :
                                values starting with #$
                                values starting with $
                                values with literals
                                -->
                        <xsl:when test="starts-with(.,'#')">
                            <!-- possibly add a check to see if the metadata exists in the parent -->
                            <meta:text>#</meta:text>
                            <meta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'#$') )}" />
                        </xsl:when>
                        <xsl:when test="starts-with(.,'$')">
                            <meta:value-of select="{concat('./bungeni:bungenimeta/bungeni:', substring-after(.,'$') )}" />
                        </xsl:when>
                        <xsl:otherwise>
                            <meta:text><xsl:value-of select="." /></meta:text>
                        </xsl:otherwise>
                    </xsl:choose>
                </meta:attribute>
            </xsl:for-each>
            <meta:apply-templates />
        </meta:element>
    </xsl:template>


</xsl:stylesheet>
