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

    <xsl:template match="*[@name='TLCReference']" bp:name="TLCReference">
        <TLCReference>
            <xsl:if test="@id">
                    <xsl:attribute name="id"><xsl:value-of select="@id" /></xsl:attribute>
            </xsl:if>
            <xsl:if test="@href">
                    <xsl:attribute name="href"><xsl:value-of select="@href" /></xsl:attribute>
            </xsl:if>
            <xsl:if test="@showAs">
                    <xsl:attribute name="showAs"><xsl:value-of select="@showAs" /></xsl:attribute>
            </xsl:if>

            <xsl:apply-templates />
        </TLCReference>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
