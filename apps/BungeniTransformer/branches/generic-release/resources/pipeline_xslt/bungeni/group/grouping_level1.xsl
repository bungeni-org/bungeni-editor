<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs"
                version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 16, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <xsl:variable name="group_principal_id" select="./field[@name='group_principal_id']" />
        <ontology type="group">
            <group>
                <xsl:attribute 
                    name="type">
                    <xsl:value-of 
                        select="./field[@name='type']" />
                </xsl:attribute>
                <xsl:attribute 
                    name="id">
                    <xsl:text>g1</xsl:text>
                </xsl:attribute>
            </group>
            <bungeni>
                <status>
                    <xsl:value-of 
                        select="./field[@name='status']" />
                </status>
                <language>
                    <xsl:value-of 
                        select="./field[@name='language']" />
                </language>
                <permissions 
                    href="#p1" />
                <principalGroup>
                    <xsl:attribute 
                        name="href" 
                        select="concat('#', $group_principal_id)" />
                </principalGroup>
                <xsl:copy-of select="field[  
                    @name='status' or
                    @name='language' ] |
                    contained_groups" />
            </bungeni>           
            <legislature>
                <xsl:copy-of select="field[  
                    @name='short_name' or
                    @name='parliament_id' or
                    @name='group_principal_id' or
                    @name='language' or 
                    @name='type' or 
                    @name='election_date' or
                    @name='status_date' ]" 
                    />            
            </legislature>
            <membership>
                
            </membership>
            <xsl:copy-of select="permissions" />
            <meta>
                <xsl:copy-of select="field[  @name='status' or
                                             @name='parliament_id' or
                                             @name='group_principal_id' or
                                             @name='language' or 
                                             @name='type' or 
                                             @name='group_id' or
                                             @name='status_date' or 
                                             @name='start_date' or 
                                             @name='party_id' or
                                             @name='parent_group_id' or
                                             @name='committee_id' or
                                             @name='committee_type_id' or
                                             @name='min_num_members' or
                                             @name='proportional_presenatation' or
                                             @name='num_researchers' ]" />

            </meta>
            <content>
                <xsl:copy-of select="field[ @name='quorum' or
                                            @name='num_members' or 
                                            @name='short_name' or
                                            @name='full_name' or 
                                            @name='description' or 
                                            @name='election_date' or 
                                            @name='dissolution_date' or 
                                            @name='results_date' or 
                                            @name= 'status_date' ] |
                                            group_addresses" />  
            </content>
        </ontology>
    </xsl:template>

</xsl:stylesheet>