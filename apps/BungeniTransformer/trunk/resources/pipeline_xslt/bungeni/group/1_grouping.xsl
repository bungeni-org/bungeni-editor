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
            <xd:p><xd:b>Created on:</xd:b> Oct 23, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    
    <!-- INPUT PARAMETERS TO TRANSFORM-->
    
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id"/>
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    <xsl:param name="type-mappings" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <!-- Content Type matcher -->
    <xsl:template match="contenttype">
        
        <!-- this identifies the type of group committee, parliament etc .-->
        <xsl:variable name="bungeni-group-type" select="field[@name='type']" />
        <!-- we map internal group type names to configured mapped name types -->
        <xsl:variable name="group-element-name" select="bctype:get_content_type_element_name($bungeni-group-type, $type-mappings)" />
        <xsl:variable name="content-type-uri-name" select="bctype:get_content_type_uri_name($bungeni-group-type, $type-mappings)" />
        
        <xsl:variable name="group_principal_id" select="field[@name='group_principal_id']" />
        <xsl:variable name="group_id" select="field[@name='group_id']" />
        
        <xsl:variable name="full-group-identifier" select="concat(
            $content-type-uri-name, '.',$for-parliament,'-',$parliament-election-date,'-',$parliament-id, '.','group','.',$group_id
            )" />
        
        <!-- ROOT OF THE DOCUMENT -->
        <ontology for="group">
            <group id="bungeniGroup" isA="TLCConcept">
                <!-- !+URI_REWORK (ah, mar-2012) 
                    ideally this should be a element describing the sub-type, but for
                    readability we set it as an attribute -->
                <!-- !+URI_REWORK (ah, 24-Apr-2012)
                    Applied element docType, attribute type not necessary anymore 
                -->
                <!--
                <xsl:attribute name="type" select="$content-type-uri-name" />    
                -->
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="field[@name='language']" />
                </xsl:attribute>                
                
                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) use ontology uri
                    for group since its non-document entity -->
                <!--
                    <xsl:attribute name="uri"
                    select="concat('/',$country-code,'/',
                    $for-parliament,'/',
                    $content-type,'/',
                    $group-type,'/',
                    $group_id
                    )" />
                -->
                <!-- !+URI_REWORK(ah, mar-2012) ...follow up to the FIX_THIS above, the URIs,
                    are being reworked to be fully AN compatible !!!WARNING!!! this will
                    break XML ui for the moment -->
                <xsl:attribute name="uri" 
                    select="concat('/ontology/',$content-type-uri-name ,'/',$full-group-identifier)" 
                />                
                
                <xsl:attribute name="id" select="$full-group-identifier" />
                
                <docType isA="TLCTerm">
                    <value type="xs:string"><xsl:value-of select="$content-type-uri-name" /></value>
                </docType>                 
                
                <xsl:copy-of select="field[ @name='parent_group_id' or 
                    @name='short_name' or
                    @name='full_name' or 
                    @name='description' or 
                    @name='min_num_members' or 
                    @name='num_researchers' or 
                    @name='num_members' or 
                    @name='quorum' or 
                    @name='start_date' or 
                    @name='status' or 
                    @name='election_date' ] | group_addresses | contained_groups"></xsl:copy-of>
                
                <!-- PERMISSIONS -->
                <xsl:copy-of select="permissions" />                
                
            </group>
            <legislature isA="TLCConcept" href="{$for-parliament}">
                <electionDate type="xs:date" select="{$parliament-election-date}"></electionDate> 
                <xsl:copy-of select="field[  
                    @name='parliament_id' or 
                    @name='type' or 
                    @name='election_date' or 
                    @name='dissolution_date' or 
                    @name='results_date' or 
                    @name='proportional_presentation' or 
                    @name='status_date' ] | agenda_items | parent_group" 
                />                 
            </legislature>
            <bungeni id="bungeniMeta" showAs="Bungeni Specific info" isA="TLCObject">
                <xsl:copy-of select="tags" />
                <xsl:copy-of select="field[@name='timestamp']" />
                <withPermissions href="#documentPermissions" />                
            </bungeni>
            
            <!--
                <xsl:element name="{$group-type}">
                <xsl:attribute name="isA">TLCConcept</xsl:attribute>
                <xsl:attribute name="refersTo" select="concat('#', $full-group-identifier)" />
                </xsl:element>
            -->
            
            <membership>
                <xsl:copy-of select="committee_type | members"></xsl:copy-of>
            </membership>  
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>