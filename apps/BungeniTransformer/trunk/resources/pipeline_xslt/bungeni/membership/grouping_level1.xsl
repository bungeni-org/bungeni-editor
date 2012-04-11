<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Jan 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p>This is a template for generating a membership document </xd:p>
        </xd:desc>
    </xd:doc>

    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-id"/>
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        <xsl:variable name="group_principal_id" select="field[@name='group_principal_id']" />
        <xsl:variable name="group_id" select="field[@name='group_id']" />
        <xsl:variable name="content-type" select="@name" />
        <xsl:variable name="group-type" select="field[@name='type']" />
        <ontology type="{$content-type}">
            <membership isA="TLCPerson" >
                
                <!-- !+URI_REWORK(ah, 11-04-2012) -->
                
                <xsl:variable name="full-user-identifier"
                    select="concat($country-code, '.',
                                    user/field[@name='last_name'], '.', 
                                    user/field[@name='first_name'], '.', 
                                    user/field[@name='date_of_birth'], '.', 
                                    field[@name='user_id'])" />
                
                
                <xsl:variable name="item_number" select="user/field[@name='user_id']"></xsl:variable>
                <xsl:variable name="group_type" select="group/field[@name='type']"></xsl:variable>
                <xsl:variable name="groups_id" select="group/field[@name='group_id']"></xsl:variable>

                
                <!-- !+NOTES (ao, 26 Mar 2012)
                     This is temporary - Group membership URI should be built with the group and not 
                     by parliament as enforced now. Proposed URI scheme should have secondary URIs to a resource 
                     embedded to a document. e.g. 
                     MP's URI... 
                        /ke/parliament/2011-02-01/parliament/2/member/20 
                     MP's other URIs to group memberships... 
                        /ke/parliament/2011-02-01/political-group/45/member/20
                        /ke/parliament/2011-02-01/office/16/member/20 
                -->
                <!-- !+URI_REWORK(ah, 11-04-2012 -->
                <xsl:attribute name="uri" 
                    select="concat('/ontology/Person/',
                                        $country-code, '/', 
                                        'ParliamentMember/', 
                                        $for-parliament, '/', 
                                        $parliament-election-date, '/',
                                        $full-user-identifier)" 
                />
                
                
                <!--
                <xsl:attribute name="uri" 
                    select="concat('/', $country-code, '/',
                    $for-parliament, '/', 
                    $parliament-id, '/',                     
                    'member','/',
                    $item_number)" /> -->
                
       
                <xsl:copy-of select="field[  
                    @name='status' or 
                    @name='party_id' or                     
                    @name='partymember' ]" 
                />     
                <referenceToUser uri="{concat('/',$country-code,'/',$for-parliament,'/user/',field[@name='user_id'])}" />
                <xsl:copy-of select="permissions | contained_groups" />                
                <xsl:copy-of select="user/child::*" /> 
                <xsl:copy-of select="changes | member_titles"/>
                <xsl:copy-of select="group" />
            </membership>
            <bungeni>
                <xsl:attribute name="id" select="$parliament-id"/>
                <xsl:copy-of select="tags"/>
                <xsl:copy-of select="field[  
                    @name='language' ]" 
                />                    
                <principalGroup>
                    <xsl:attribute name="href" select="concat('#', $group_principal_id)" />
                </principalGroup>
            </bungeni> 
            
            <!--    !+FIX_THIS (ao, jan 2012. Some address documents for individuals like clerk dont have 'type' field and 
                this broke the pipeline processor
                
            <xsl:element name="{$group-type}">
                <xsl:attribute name="isA">TLCOrganization</xsl:attribute>
                <xsl:attribute name="refersTo" select="concat('#', $group_id)" />
            </xsl:element>
            -->
            <legislature>
                <xsl:copy-of select="group/field[  
                    @name='short_name' or
                    @name='full_name' or 
                    @name='description' or 
                    @name='type' or 
                    @name='group_id' or  
                    @name='election_date' or 
                    @name='start_date' or 
                    @name='dissolution_date' or 
                    @name='results_date' or 
                    @name='proportional_presentation' or 
                    @name='status_date' ] " 
                />             
            </legislature> 
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>