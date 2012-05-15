<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Mar 22, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <!-- these are input parameters to the transformation a-->
    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code" />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date" />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        <xsl:variable name="heading_id" select="field[@name='heading_id']" />
        <xsl:variable name="content-type" select="@name" />
        <xsl:variable name="group_id" select="head/field[@name='group_id']" />        
        <xsl:variable name="group-type" select="head/field[@name='type']" />
        <xsl:variable name="parl-info" select="concat('/',$country-code,'/',$for-parliament,'/')"/>
        <!-- 
            !+NOTE (ao, jan-2012)
            Take country_id as opposed to $country-code as with other documents 
        -->
        <ontology for="{$content-type}">
            <heading isA="TLCConcept" xml:lang="{field[@name='language']}">
                <xsl:attribute name="uri" 
                    select="concat('/ontology/Heading/',$country-code,'/',
                    $for-parliament, '/', 
                    $content-type, '/', 
                    $heading_id)" />
                <xsl:copy-of select="field[
                    @name='heading_id' or 
                    @name='status' or 
                    @name='text' ] " />
                <xsl:copy-of select="permissions" /> 
            </heading>            
            <metadata>
                <xsl:attribute name="type">
                    <xsl:value-of select="$content-type" />
                </xsl:attribute>
            </metadata> 
            <bungeni>
                <xsl:attribute name="id" select="$parliament-id"/>
                <parliament href="{concat('/',$country-code,'/',$for-parliament)}" isA="TLCOrganization" date="{$parliament-election-date}" />
                <xsl:copy-of select="tags"/>
                <xsl:copy-of select="field[ 
                    @name='language' ]" 
                />                
            </bungeni>             
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>