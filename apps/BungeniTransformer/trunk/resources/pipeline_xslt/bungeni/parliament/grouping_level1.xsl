<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Mar 29, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code" />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date" />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        <xsl:variable name="parliament_id" select="field[@name='parliament_id']" />
        <xsl:variable name="content-type" select="@name" />
        <xsl:variable name="group_id" select="head/field[@name='group_id']" />        
        <xsl:variable name="group-type" select="head/field[@name='type']" />
        <xsl:variable name="parl-info" select="concat('/',$country-code,'/',$for-parliament,'/')"/>
        <ontology type="{$content-type}">
            <bungeni>
                <xsl:attribute name="id" select="$parliament-id"/>
                <xsl:copy-of select="tags"/>
                <xsl:copy-of select="field[ 
                    @name='language' ]" 
                />                
            </bungeni>             
            <parliament isA="TLCObject" date="{$parliament-election-date}">
                <xsl:attribute name="uri" 
                    select="concat('/',$country-code,'/',
                    $for-parliament, '/', 
                    $content-type, '/', 
                    $parliament_id)" />
                <xsl:copy-of select="field[
                    @name='parliament_id' or 
                    @name='status' or 
                    @name='short_name' or 
                    @name='full_name' or 
                    @name='description' or 
                    @name='group_principal_id' or 
                    @name='election_date' or 
                    @name='start_date' or 
                    @name='status_date' or 
                    @name='text' ] " />
                <xsl:copy-of select="permissions | group | contained_groups | 
                                     members | group_addresses" /> 
            </parliament>
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>