<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                exclude-result-prefixes="xs"
                version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 17, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <legislativeitem name="question">
            <meta>
                <xsl:copy-of select="field[  @name='question_type_id' or
                                             @name='ministry_id' or
                                             @name='response_type_id' or
                                             @name='language' or 
                                             @name='receive_notification' or 
                                             @name='type' or 
                                             @name='owner_id' or 
                                             @name='status' or 
                                             @name='question_number' or
                                             @name='status_date' or
                                             @name='question_id' or
                                             @name='parliamentary_item_id' or
                                             @name='registry_number' or
                                             @name='uri' ]" />
                <xsl:copy-of select="ministry/field[@name='short_name']" />
                <xsl:copy-of select="question_type" />

                <xsl:copy-of select="permissions | ministry | contained_groups | events" />
            </meta>
            <content>
                <xsl:copy-of select="field[ @name='body_text' or
                                            @name='short_name' or 
                                            @name='status_date' ] |
                                            group_addresses" />  
                
                <xsl:copy-of select="response_type | owner | changes | versions | attachedfiles" />
            </content>
        </legislativeitem>
    </xsl:template>

</xsl:stylesheet>