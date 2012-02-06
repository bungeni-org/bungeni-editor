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

    <xsl:template match="*[@name='body']" bp:name="body">
        <debate>
            <xsl:for-each select="*[@name='Observation']">
                <subdivision name="SceneSubdivision" id="{generate-id(.)}">
                        <xsl:apply-templates select="." />
                </subdivision>
            </xsl:for-each>
             <xsl:apply-templates select="*[@name != 'Preface' and @name != 'Conclusion' and @name != 'Observation' and @name != 'meta']"/>
        </debate>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
