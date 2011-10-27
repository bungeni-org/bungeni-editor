<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 23, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    
    
    <xsl:variable name="country-code" select="string('ke')" />
    <xsl:variable name="parliament-election-date" select="string('2011-03-02')" />
    <xsl:variable name="for-parliament" select="concat('/ke/parliament/', $parliament-election-date)" />
    
    <xsl:template match="contenttype">
        <xsl:variable name="group_principal_id" select="./field[@name='group_principal_id']" />
        <xsl:variable name="group_id" select="./field[@name='group_id']" />
        <xsl:variable name="content-type" select="@name" />
        <ontology type="{$content-type}">
            <group>
                
                <xsl:variable name="group-type" select="field[@name='type']" />
                
                <xsl:attribute name="type" select="$group-type" />
                
                <xsl:attribute name="uri" 
                    select="concat($for-parliament, '/', 
                    $content-type, '/',
                    $group_id)" />
                
                <xsl:copy-of select="field[ @name='group_id' or 
                                            @name='parent_group_id' or 
                                            @name='min_num_members' or 
                                            @name='num_researchers' or 
                                            @name='num_members' or 
                                            @name='quorum' or 
                                            @name='start_date' or 
                                            @name='election_date' ] | group_addresses"></xsl:copy-of>
            </group>
            <bungeni>
                <status><xsl:value-of select="./field[@name='status']" /></status>
                <language><xsl:value-of select="./field[@name='language']" /></language>
                <xsl:copy-of select="permissions" />
                <principalGroup>
                    <xsl:attribute name="href" select="concat('#', $group_principal_id)" />
                </principalGroup>
                <xsl:copy-of select="contained_groups" />
            </bungeni> 
            
            <xsl:element name="{./field[@name='type']}">
                <xsl:attribute name="isA">TLCOrganization</xsl:attribute>
                <xsl:attribute name="id">parl</xsl:attribute>
                <xsl:attribute name="type" select="$group_id" />
                <xsl:copy-of select="field[  
                    @name='short_name' or
                    @name='full_name' or
                    @name='description' ]" 
                />
            </xsl:element>
            
            <legislature>
                <xsl:copy-of select="field[  
                    @name='short_name' or
                    @name='parliament_id' or
                    @name='group_principal_id' or
                    @name='language' or 
                    @name='type' or 
                    @name='election_date' or 
                    @name='dissolution_date' or 
                    @name='results_date' or 
                    @name='proportional_presentation' or 
                    @name='status_date' ] | agenda_items" 
                />            
            </legislature>
            <membership>
                <xsl:copy-of select="committee_type | members"></xsl:copy-of>
            </membership>  
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>