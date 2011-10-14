<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:bu="http://portal.bungeni.org/1.0/"
        version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <!--

    Default templates :
    These templates are required

    -->

    <xsl:template match="/">
        <stylesheets>
            <xsl:apply-templates/>
        </stylesheets>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

    <!--

    PIPELINE TEMPLATES FOLLOW

    -->

    <xsl:template match="bp:contenttype">
        <xslt name="bp:contenttype" href="pipeline_xslt/bungeniportal/contenttype.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>

    <!--
    <xsl:template match="bp:contenttype">
        <xslt name="bp:contenttype" href="pipeline_xslt/bungeniportal/attachments.xsl"/>
        <xsl:apply-templates/>
    </xsl:template>
    -->

    <!--

    PIPELINE TEMPLATES END

    -->


</xsl:stylesheet>