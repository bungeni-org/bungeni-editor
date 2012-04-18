<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:bctypes="http://www.bungeni.org/xml/contenttypes/1.0"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Apr 13, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok</xd:p>
            <xd:p>This is the XSLT function to process content type mappings</xd:p>
        </xd:desc>
    </xd:doc>
    
    
    <!--
        Gets the Element name for a type mapping 
        -->
    <xsl:function name="bctypes:get_content_type_element_name">
        <xsl:param name="bungeni-content-type"/>
        <xsl:param name="type-mappings"/>
        
        <xsl:call-template name="get_content_type_element_name">
            <xsl:with-param name="bungeni-content-type" select="$bungeni-content-type"/>
            <xsl:with-param name="type-mappings" select="$type-mappings"/>
            
        </xsl:call-template>
    </xsl:function>
    
    <!--
        Gets the URI name for a type mapping
        -->
    <xsl:function name="bctypes:get_content_type_uri_name">
        <xsl:param name="bungeni-content-type"/>
        <xsl:param name="type-mappings"/>
        
        <xsl:call-template name="get_content_type_uri_name">
            <xsl:with-param name="bungeni-content-type" select="$bungeni-content-type"/>
            <xsl:with-param name="type-mappings" select="$type-mappings"/>
        </xsl:call-template>
    </xsl:function>
    
    <!--
        Generates the group identifier
        -->
    <xsl:function name="bctypes:generate-group-identifier">
        <xsl:param name="group-uri-name" />
        <xsl:param name="for-parliament" />
        <xsl:param name="parliament-election-date" />
        <xsl:param name="parliament-id" />
        <xsl:param name="group-id" />
        <xsl:sequence select="concat(
            $group-uri-name, '.',$for-parliament,'-',$parliament-election-date,'-',$parliament-id, '.','group','.',$group-id
            )" />
    </xsl:function>
    
    <!--
        Generates the group uri
        -->
    <xsl:function name="bctypes:generate-group-uri">
        <xsl:param name="group-uri-name" />
        <xsl:param name="full-group-identifier" />
        <xsl:sequence select="concat(
            '/ontology/',
            $group-uri-name ,'/',
            $full-group-identifier)" />
    </xsl:function>
    
    
    <!--
        Supporting templates for functions
        -->
    <xsl:template name="get_content_type_element_name">
        <xsl:param name="bungeni-content-type" />
        <xsl:param name="type-mappings" />
        <xsl:variable name="content-type-element-name">
            <xsl:variable name="tmp-content-type" select="$type-mappings//map[@from=$bungeni-content-type]/@element-name" />
            <xsl:choose>
                <xsl:when test="string-length($tmp-content-type) eq 0">
                    <xsl:value-of select="$bungeni-content-type" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$tmp-content-type" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:sequence select="$content-type-element-name" />
    </xsl:template>
    
    <xsl:template name="get_content_type_uri_name">
        <xsl:param name="bungeni-content-type" />
        <xsl:param name="type-mappings" />
        <xsl:variable name="content-type-uri-name">
            <xsl:variable name="tmp-content-type" select="$type-mappings//map[@from=$bungeni-content-type]/@uri-name" />
            <xsl:choose>
                <xsl:when test="string-length($tmp-content-type) eq 0">
                    <xsl:value-of select="$bungeni-content-type" />
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="$tmp-content-type" />
                </xsl:otherwise>
            </xsl:choose>
        </xsl:variable>
        <xsl:sequence select="$content-type-uri-name" />
    </xsl:template>
    
 
    
</xsl:stylesheet>
