<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:bctype="http://www.bungeni.org/xml/contenttypes/1.0"    
    xmlns:xbf="http://bungeni.org/xslt/functions"
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
    
    <!-- these are input parameters to the transformation a-->
    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    <xsl:param name="type-mappings" />    
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:function name="xbf:parse-date">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputDate[2])" />
    </xsl:function>
    
    <xsl:template match="contenttype">
        
        <!-- this identifies the type of group committee, parliament etc .-->
        <xsl:variable name="bungeni-groupsitting-type" select="@name" />
        <!-- we map internal group type names to configured mapped name types -->
        <xsl:variable name="group-element-name" select="bctype:get_content_type_element_name($bungeni-groupsitting-type, $type-mappings)" />
        <xsl:variable name="content-type-uri-name" select="bctype:get_content_type_uri_name($bungeni-groupsitting-type, $type-mappings)" />        
        
        <xsl:variable name="group_id" select="field[@name='group_id']" />
        <xsl:variable name="sitting-session" select="concat(xbf:parse-date(field[@name='start_date']),';',xbf:parse-date(field[@name='end_date']))" />
        
        <ontology for="groupsitting">
            <groupsitting id="bungeniGroupsitting" isA="TLCConcept">
                <xsl:attribute name="for" select="$group-element-name" />
                
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="field[@name='language']" />
                </xsl:attribute>                 
                
                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) use ontology uri
                    for group since its non-document entity -->
                <xsl:attribute name="uri" 
                    select="concat(
                    '/ontology/',
                    $content-type-uri-name,'/',
                    $country-code, '/',
                    $group-element-name,'/',
                    $group_id,'/',
                    normalize-space($sitting-session)
                    )" />
                
                <xsl:attribute name="id" select="$group_id" />
                
                <docType isA="TLCTerm">
                    <value type="xs:string"><xsl:value-of select="$content-type-uri-name" /></value>
                </docType>                 
                
                <xsl:copy-of select="field[ @name='parent_group_id' or 
                    @name='min_num_members' or 
                    @name='num_researchers' or 
                    @name='num_members' or 
                    @name='quorum' or 
                    @name='start_date' or 
                    @name='end_date' or 
                    @name='venue_id' or 
                    @name='meeting_type' or 
                    @name='convocation_type' or 
                    @name='activity_type' or 
                    @name='status' or 
                    @name='election_date' ] | 
                    group_addresses | 
                    item_schedule | 
                    venue | 
                    reports"></xsl:copy-of>
                
                <xsl:copy-of select="permissions | contained_groups" />                
            </groupsitting>
            <legislature isA="TLCConcept" href="{$for-parliament}">
                <electionDate type="xs:date" select="{$parliament-election-date}"></electionDate>
                <xsl:copy-of select="field[  
                    @name='short_name' or 
                    @name='parliament_id' or 
                    @name='type' or 
                    @name='dissolution_date' or 
                    @name='results_date' or 
                    @name='status_date' ] | agenda_items | parent_group | group" 
                />             
            </legislature>             
            <bungeni id="bungeniMeta" showAs="Bungeni Specific info" isA="TLCObject">
                <xsl:attribute name="id" select="$parliament-id"/>
                <xsl:copy-of select="field[  
                    @name='language' ]" 
                />
                <xsl:copy-of select="tags" />
            </bungeni> 
            
            <!--    !+FIX_THIS (ao, jan 2012. Some address documents for individuals like clerk dont have 'type' field and 
                this broke the pipeline processor
                
                <xsl:element name="{$group-element-name}">
                <xsl:attribute name="isA">TLCOrganization</xsl:attribute>
                <xsl:attribute name="refersTo" select="concat('#', $group_id)" />
                </xsl:element>
            -->
            <custom>
                <xsl:copy-of select="$type-mappings" />
            </custom>            
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>