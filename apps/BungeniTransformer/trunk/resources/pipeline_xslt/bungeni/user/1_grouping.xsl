<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:bctype="http://www.bungeni.org/xml/contenttypes/1.0"
                exclude-result-prefixes="xs bctype"
                version="2.0">
    
    <!-- INCLUDE FUNCTIONS -->
    <xsl:import href="resources/pipeline_xslt/bungeni/common/func_content_types.xsl" />    
    
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 26, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    <!-- INPUT PARAMETERS TO TRANSFORM-->
    
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id" />
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    <xsl:param name="type-mappings" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        
        <!-- this field identifies the type of the input xml bill, question , motion etc. -->
        <xsl:variable name="bungeni-content-type" select="@name" />
        <!-- We map the bungeni internal content type name to a alternative name to prevent tie-in to internal representations -->
        <!-- the type mapping specifies both the name in the URI and the Element name -->
        <xsl:variable name="content-type-element-name" select="bctype:get_content_type_element_name($bungeni-content-type, $type-mappings)" />
        <xsl:variable name="content-type-uri-name" select="bctype:get_content_type_uri_name($bungeni-content-type, $type-mappings)" /> 
        
        
        <!-- ROOT ELEMENT OF DOCUMENT -->
        <ontology type="user">
            
            <xsl:variable name="full-user-identifier"
                select="concat($country-code, '.',field[@name='last_name'], '.', field[@name='first_name'], '.', field[@name='date_of_birth'], '.', field[@name='user_id'])" />
            
            <user id="bungeniUser" isA="TLCPerson" >
                <xsl:attribute name="xml:lang">
                    <xsl:value-of select="field[@name='language']" />
                </xsl:attribute>
                
                <xsl:variable name="item_number" select="field[@name='user_id']"></xsl:variable>
                
                <xsl:attribute name="uri" 
                    select="concat('/ontology/Person/',$full-user-identifier)" 
                />
                
                <xsl:copy-of select="field[
                    @name='first_name' or 
                    @name='last_name' or 
                    @name='user_id' or 
                    @name='description' or 
                    @name='language' or 
                    @name='gender' or 
                    @name='active_p' or 
                    @name='date_of_birth' or 
                    @name='titles' or 
                    @name='birth_country' or 
                    @name='national_id' or 
                    @name='login' or 
                    @name='password' or 
                    @name='salt' or 
                    @name='email' or 
                    @name='birth_nationality' or 
                    @name='current_nationality' or 
                    @name='tabled_document_number' ] |
                    user_addresses " />
            </user>
            
            <xsl:copy-of select="subscriptions"/>
            
            <bungeni id="bungeniMeta" showAs="Bungeni Specific info" isA="TLCObject">
                <withPermissions href="#documentPermissions" />
            </bungeni>
                      
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>