<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema" 
    xmlns:busers="http://www.bungeni.org/xml/users/1.0"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> Apr 13, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok</xd:p>
            <xd:p>This is the XSLT function to process content type mappings</xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:variable name="person-uri-prefix">
        <xsl:text>/ontology/Person/</xsl:text>
    </xsl:variable>
    
    <xsl:function name="busers:get_user_identifer">
        <xsl:param name="country-code"></xsl:param>
        <xsl:param name="last-name"></xsl:param>
        <xsl:param name="first-name"></xsl:param>
        <xsl:param name="user-id"></xsl:param>
        <xsl:param name="date-of-birth"></xsl:param>
        <xsl:sequence select="concat(
            $country-code, '.',
            $last-name, '.', 
            $first-name, '.', 
            $date-of-birth, '.', 
            $user-id)" />
    </xsl:function>
    
    
    <xsl:function name="busers:get_member_uri">
        <xsl:param name="country-code"></xsl:param>
        <xsl:param name="for-parliament"></xsl:param>
        <xsl:param name="parliament-election-date"></xsl:param>
        <xsl:param name="full-user-identifier"></xsl:param>
        <xsl:variable name="user-type" select="string('ParliamentMember')" />
        <xsl:sequence select="concat(
            $person-uri-prefix,
            $country-code, '/', 
            $user-type, '/',
            $for-parliament, '/', 
            $parliament-election-date, '/',
            $full-user-identifier)"             
        />
        
    </xsl:function>
        
     <xsl:function name="busers:get_user_uri">
            <xsl:param name="country-code"></xsl:param>
            <xsl:param name="full-user-identifier"></xsl:param>
            <xsl:sequence select="concat(
                $person-uri-prefix,
                $full-user-identifier
                )"             
            />
            
     </xsl:function>
    

    
   
 
    
</xsl:stylesheet>
