<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:ml="http://www.metalex.org/1.0"
                xmlns:bu="http://portal.bungeni.org/1.0/"
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                exclude-result-prefixes="bp"
                version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <!--

    Default templates :
    These templates are required

    -->

    <xsl:template match="/">
        <bp:stylesheets>
            <xsl:apply-templates/>
        </bp:stylesheets>
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
    <bp:template name="root" href="pipeline_xslt/bungeni/group/root.xsl"/>

    <bp:template name="preface" href="pipeline_xslt/bungeni/group/preface.xsl"/>

    <bp:template name="meta" href="pipeline_xslt/bungeni/group/meta.xsl"/>

    <bp:template name="content" href="pipeline_xslt/bungeni/group/content.xsl"/>

    <!--

    PIPELINE TEMPLATES END

    -->


</xsl:stylesheet>