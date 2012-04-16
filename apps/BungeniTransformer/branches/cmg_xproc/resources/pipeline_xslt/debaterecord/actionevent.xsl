<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:include href="../common/identity_template.xsl"/>

    <xsl:template match="*[@name='ActionEvent']" bp:name="ActionEvent">
        <subdivision>
            <xsl:if test="@id">
                <xsl:attribute name="id">
                    <xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:attribute name="bodf:sourceId" select="@id" />
            </xsl:if>
            <xsl:if test="@name">
                <xsl:attribute name="name">
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniOntologyName"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </subdivision>
    </xsl:template>

    <xsl:include href="../common/normalize_text_template.xsl"/>

</xsl:stylesheet>
