<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs"
    version="2.0">

    <xsl:param name="parliament-info" />
    <xsl:param name="type-mappings" />
    
    <xsl:variable name="country-code" >
        <xsl:value-of select="$parliament-info//countryCode" />
    </xsl:variable>
    
    <xsl:variable name="origin-parliament">
        <xsl:value-of select="//field[@name='origin_parliament']" />    
    </xsl:variable>
    
    <xsl:variable name="current-parliament" 
        select="$parliament-info//parliament[@id eq $origin-parliament]" />
    
    <xsl:variable name="parliament-id">
        <xsl:value-of select="$current-parliament/@id" />
    </xsl:variable>
    
    <xsl:variable name="for-parliament">
        <xsl:value-of select="$current-parliament/forParliament" />
    </xsl:variable>
    
    <xsl:variable name="parliament-election-date">
        <xsl:value-of select="$current-parliament/electionDate" />
    </xsl:variable>
    

</xsl:stylesheet>
