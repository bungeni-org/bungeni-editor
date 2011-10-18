<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs"
                version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 16, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> undesa</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <group>
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

                <xsl:copy-of select="permissions | contained_groups" />
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
        </group>
    </xsl:template>

</xsl:stylesheet>