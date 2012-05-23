<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0" >
    <xsl:output method="xml" />
    <!--
        This XSLT strips out languages that dont have alpha2 code 
        -->
    <!-- identity transform -->
    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="language" >
        <xsl:if test="string-length(@alpha2) &gt; 0">
            <xsl:copy>
                <xsl:apply-templates select="@* | node()" />
            </xsl:copy>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="name_en">
        <name lang="en">
            <xsl:apply-templates select="@* | node()" />
        </name>
    </xsl:template>

    <xsl:template match="name_fr">
        <name lang="fr">
            <xsl:apply-templates select="@* | node()" />
        </name>
    </xsl:template>
    

</xsl:stylesheet>