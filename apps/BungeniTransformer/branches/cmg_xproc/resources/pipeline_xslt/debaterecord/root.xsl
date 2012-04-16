<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                exclude-result-prefixes="bp"
                version="2.0">
    <!--
    This is the root template that directs all the others
    -->
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

    <xsl:template match="*[@name='root']" bp:name="root">
        <akomaNtoso>
            <debateRecord>
                <xsl:apply-templates select="//*[@name='meta']"/>
                <xsl:apply-templates select="//*[@name='Preface']"/>
                <!-- !+PIPELINE(ah, feb-2012), added explicit matcher for body
                otherwise meta gets matched twice -->
                <xsl:apply-templates select="//*[@name='body']" />
                <xsl:apply-templates select="//*[@name='Conclusion']"/>
            </debateRecord>
        </akomaNtoso>    
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
