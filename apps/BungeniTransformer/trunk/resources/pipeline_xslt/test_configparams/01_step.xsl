<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:bctype="http://www.bungeni.org/xml/contenttypes/1.0"    
    exclude-result-prefixes="xs bctype"
    version="2.0">
    
    <!-- INCLUDE FUNCTIONS -->
    <xsl:include href="resources/pipeline_xslt/bungeni/common/func_content_types.xsl" />
    
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Jan 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    <!-- INPUT PARAMETERS TO TRANSFORM-->
    
    <xsl:param name="text-param-defval" />
    <xsl:param name="text-param-defval-or" />
    <xsl:param name="text-param-input"/>
    <xsl:param name="xml-param-defval" />
    <xsl:param name="xml-param-defval-or" />
    <xsl:param name="xml-param-input" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="root">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="test">
        <test>
            <text-param-defval>
                <xsl:value-of select="$text-param-defval" />
            </text-param-defval>
            <text-param-defval-or>
                <xsl:value-of select="$text-param-defval-or" />
            </text-param-defval-or>
            
            <text-param-input>
                <xsl:value-of select="$text-param-input" />
            </text-param-input>
            
            <xml-param-defval>
                <xsl:value-of select="$xml-param-defval/value/map/@uri-name" />
            </xml-param-defval>
            <xml-param-input>
                <xsl:value-of select="$xml-param-input/hello/world/@name" />
            </xml-param-input>
            <xml-param-defval-or>
                <xsl:value-of select="$xml-param-defval-or/value/hello/@name" />
            </xml-param-defval-or>
            <content>
                <xsl:value-of select="." />
            </content>
        </test>
    </xsl:template>
        
</xsl:stylesheet>