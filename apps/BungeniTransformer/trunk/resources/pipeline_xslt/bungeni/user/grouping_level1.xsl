<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Oct 26, 2011</xd:p>
            <xd:p><xd:b>Author:</xd:b> Anthony</xd:p>
            <xd:p></xd:p>
        </xd:desc>
    </xd:doc>

    <!-- these are input parameters to the transformation a-->
    <xsl:param name="country-code"  />
    <xsl:param name="parliament-election-date"  />
    <xsl:param name="for-parliament" />
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="contenttype">
        <xsl:variable name="content-type" select="@name" />
        
        <ontology type="userdata">
            <metadata>
                <xsl:attribute name="type">
                    <xsl:value-of select="$content-type" />
                </xsl:attribute>
            </metadata>          
            <user isA="TLCPerson" >
                <xsl:variable name="item_number" select="field[@name='user_id']"></xsl:variable>
                <xsl:attribute name="uri" 
                    select="concat('/',$country-code,'/',
                    $for-parliament, '/', 
                    $content-type, '/', 
                    $item_number)" />
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
                                        subscriptions | 
                                        user_addresses " />
            </user>
            <bungeni>
                <country><xsl:value-of select="$country-code" /></country>
                <parliament href="{concat('/',$country-code,'/',$for-parliament)}" isA="TLCOrganization" date="{$parliament-election-date}" />
            </bungeni>              
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>