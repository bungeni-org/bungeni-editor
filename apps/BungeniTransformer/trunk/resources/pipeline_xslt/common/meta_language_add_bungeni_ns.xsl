<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:metalex="http://www.metalex.org/1.0" 
    exclude-result-prefixes="metalex"
   >
    
    <!-- ***DOC***
        Append the rdf namespace to the odf document-contenent root element 
    -->
    
    <!-- special rule for the document element -->
    <xsl:template match="/*">
        <xsl:copy>
            <xsl:namespace name="bungeni" select="'http://editor.bungeni.org/1.0/anx/'"/>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    
    <!-- the identity template -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:template>
    
    
    
</xsl:stylesheet>
