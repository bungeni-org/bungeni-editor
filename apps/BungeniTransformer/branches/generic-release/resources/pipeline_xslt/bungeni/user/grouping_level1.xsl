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
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
    <xsl:template match="contenttype">
        <ontology type="user">
            <user type="user">
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
        </ontology>
    </xsl:template>
    
</xsl:stylesheet>