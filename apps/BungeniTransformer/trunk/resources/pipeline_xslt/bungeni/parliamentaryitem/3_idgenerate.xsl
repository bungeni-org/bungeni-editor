<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:bdates="http://www.bungeni.org/xml/dates/1.0"
    xmlns:busers="http://www.bungeni.org/xml/users/1.0"
    xmlns:bctypes="http://www.bungeni.org/xml/contenttypes/1.0"
    exclude-result-prefixes="xs"
    version="2.0">

    
    
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>
        
    <!-- These values are set in first input which is grouping_Level1 -->        

    <xsl:variable name="uri-type" select="data(/ontology/document/docType/value)" />
    <xsl:variable name="country" select="string(data(/ontology/legislature/country/value))" />
    <xsl:variable name="add-change" select="/ontology/document/changes/change[auditAction/value[.='add']]" />
    <xsl:variable name="active-date" select="data($add-change/activeDate)" />
    <xsl:variable name="parliament-id" select="data(/ontology/legislature/parliamentId)" />
    <xsl:variable name="registry-number" select="data(/ontology/document/registryNumber)" />
    <xsl:variable name="doc-uri"
        select="concat('/',
        $country, '/',  
        $uri-type, '/',
        'legislature.', $parliament-id,'/',
        $active-date,'/',
        $registry-number
        )" />

    
    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>
    
    
    
    <xsl:template match="@uri[parent::document]">
        <xsl:attribute name="uri"><xsl:value-of select="$doc-uri" /></xsl:attribute>
    </xsl:template>
    
    <xsl:template match="permissions[parent::change]">
        <xsl:variable name="parent-change-sequence-id"><xsl:value-of select="parent::change/auditId" /></xsl:variable> 
        <permissions id="change-permissions-{$parent-change-sequence-id}">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    
    
    <xsl:template match="permissions[parent::version]">
        <xsl:variable name="parent-version-sequence-id"><xsl:value-of select="parent::version/sequence" /></xsl:variable> 
        <permissions id="version-permissions-{$parent-version-sequence-id}">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    <xsl:template match="permissions[parent::attachment]">
        <xsl:variable name="parent-att-id"><xsl:value-of select="parent::attachment/attachmentId" /></xsl:variable> 
        <permissions id="attachment-permissions-{$parent-att-id}">
            <xsl:apply-templates />
        </permissions>
    </xsl:template>
    
    
    <xsl:template match="change[parent::changes/parent::document]">
        <xsl:variable name="change-xml-id" select="data(auditId)" />
        <change id="document-change-{$change-xml-id}">
            <refersToAudit>
                <xsl:attribute name="href"
                    select="concat('#document-audit-', $change-xml-id)" />
            </refersToAudit>
            <xsl:apply-templates />
        </change>
    </xsl:template>
    
    
    <xsl:template match="audit[parent::audits/parent::document]">
        <xsl:variable name="audit-xml-id" select="data(auditId)" />
        <audit id="document-audit-{$audit-xml-id}">
            <xsl:apply-templates />
        </audit>
    </xsl:template>
    
    <xsl:template match="version[parent::versions/parent::document]">
        <xsl:variable name="version-xml-id" select="data(auditId)">
        </xsl:variable>
        <xsl:copy>
            <xsl:attribute name="id" select="concat('document-version-',$version-xml-id)" />
            <xsl:attribute name="uri" select="concat($doc-uri, '@', data(activeDate))" />
            <refersToAudit>
                <xsl:attribute name="href"
                    select="concat('#document-audit-', $version-xml-id)" />
            </refersToAudit>
            <refersToChange>
                <xsl:attribute name="href"
                    select="concat('#document-change-', $version-xml-id)" />
            </refersToChange>
            <xsl:apply-templates />
        </xsl:copy>
    </xsl:template>
    
    
   <xsl:template match="custom" />
    
    
    
</xsl:stylesheet>