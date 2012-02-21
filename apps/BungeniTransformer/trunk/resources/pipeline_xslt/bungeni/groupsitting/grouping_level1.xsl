<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:xbf="http://bungeni.org/xslt/functions"
    exclude-result-prefixes="xs"
    version="2.0">
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
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:function name="xbf:parse-date">
        <xsl:param name="input-date"/>
        <xsl:variable name="arrInputDate" select="tokenize($input-date,'\s+')" />
        <xsl:sequence select="concat($arrInputDate[1],'T',$arrInputDate[2])" />
    </xsl:function>
    
    <xsl:template match="contenttype">
        
        <xsl:variable name="group_principal_id" select="field[@name='group_principal_id']" />
        <xsl:variable name="group_id" select="field[@name='group_id']" />
        <xsl:variable name="content-type" select="@name" />      
        <xsl:variable name="sitting-session" select="concat(xbf:parse-date(field[@name='start_date']),';',xbf:parse-date(field[@name='end_date']))" />
        
        <xsl:variable name="group-type" select="group/field[@name='type']" />
        <ontology type="{$content-type}">
            <groupsitting>
                <xsl:attribute name="type" select="$group-type" />

                <!-- !+URI_GENERATOR,!+FIX_THIS(ah,nov-2011) use ontology uri
                for group since its non-document entity -->
                <xsl:attribute name="uri" 
                    select="concat(
                     '/ontology/',
                     $content-type,'/',
                     $country-code, '/',
                     $group-type,'/',
                     normalize-space($sitting-session),'/',
                     $group_id
                     )" />

                <xsl:attribute name="id" select="$group_id" />
                
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
                                            @name='election_date' ] | 
                                            group_addresses | 
                                            item_schedule | 
                                            venue | 
                                            reports"></xsl:copy-of>
            </groupsitting>
            <bungeni>
                <xsl:copy-of select="field[  
                    @name='language' or
                    @name='status' ]" 
                />                    
                <xsl:copy-of select="permissions" />
                <principalGroup>
                    <xsl:attribute name="href" select="concat('#', $group_principal_id)" />
                </principalGroup>
                <xsl:copy-of select="contained_groups | tags" />
            </bungeni> 
            
            <!--    !+FIX_THIS (ao, jan 2012. Some address documents for individuals like clerk dont have 'type' field and 
                this broke the pipeline processor
                
            <xsl:element name="{$group-type}">
                <xsl:attribute name="isA">TLCOrganization</xsl:attribute>
                <xsl:attribute name="refersTo" select="concat('#', $group_id)" />
            </xsl:element>
            -->
            <legislature>
                <xsl:copy-of select="field[  
                    @name='short_name' or 
                    @name='description' or 
                    @name='parliament_id' or 
                    @name='type' or 
                    @name='dissolution_date' or 
                    @name='results_date' or 
                    @name='status_date' ] | agenda_items | parent_group | group" 
                />             
            </legislature> 
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>