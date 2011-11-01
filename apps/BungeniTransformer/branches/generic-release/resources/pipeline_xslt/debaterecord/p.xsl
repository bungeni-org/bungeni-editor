<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                exclude-result-prefixes="bp"
                version="2.0"> 
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
            <xsl:apply-templates />
        </xsl:element>
    </xsl:template>

    <xsl:template match="*[@name='p']" bp:name="p">
        <!-- when the descendant is a ref within a paragraph, its usually a <from>,
        if its a from we dont decoreate the template with a paragraph 'p' element -->
        <xsl:choose>
            <xsl:when test="descendant::*[@name='ref' and @class='BungeniSpeechBy']">
                <xsl:apply-templates />    
            </xsl:when>
            <xsl:otherwise>
                <p>
                    <xsl:apply-templates />
                </p>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>