<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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

    <xsl:template match="*[@name='publication']">
        <publication>
			<xsl:if test="@date">
				<xsl:attribute name="date"><xsl:value-of select="@date" /></xsl:attribute>
			</xsl:if>
	        <xsl:attribute name="name"><xsl:value-of select="@contentName" /></xsl:attribute>
		    <xsl:attribute name="showAs"><xsl:value-of select="@showAs" /></xsl:attribute>
		    <xsl:apply-templates />
        </publication>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
