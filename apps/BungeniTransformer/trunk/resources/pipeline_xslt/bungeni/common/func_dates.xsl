<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:bdates="http://www.bungeni.org/xml/dates/1.0"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Apr 13, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok</xd:p>
            <xd:p>This is the XSLT function to process content type mappings</xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:function name="bdates:parse-date">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputDate[2])" />
    </xsl:function>

    <xsl:function name="bdates:parse-datepart-only">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="$arrInputDate[1]" />
    </xsl:function>
    
    <xsl:function name="bdates:yyyymmdd-date">
        <xsl:param name="input-date">
            <!-- this date must be a valid xsd:date or xsd:dateTime -->
        </xsl:param>
        <xsl:sequence select="format-dateTime($input-date, '[Y0001]-[M01]-[D01]')" />
    </xsl:function>
    
    <xsl:function name="bdates:yyyymmdd-hhmmss-date">
        <xsl:param name="input-date">
            <!-- this date must be a valid xsd:date or xsd:dateTime -->
        </xsl:param>
        <xsl:sequence select="format-dateTime($input-date, '[Y0001]-[M01]-[D01]T[H01]-[m01]-[s01]')" />
    </xsl:function>
    
    
    <xsl:function name="bdates:parse-date-nomicrotime">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:variable name="arrInputTime" select="substring-before(string($arrInputDate[2]), '.')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputTime)" />
    </xsl:function>
    
 
    
</xsl:stylesheet>
