<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
                xmlns:bungeni="http://editor.bungeni.org/1.0/anx"
                xmlns:bp="http://www.bungeni.org/pipeline/1.0"
                xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
                exclude-result-prefixes="bp"
                version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>

    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:include href="../common/identity_template.xsl"/>

    <xsl:template match="*[@name='Speech1']" bp:name="Speech1">
        <speech>
            <xsl:if test="@id">
                <xsl:attribute name="id">
                    <xsl:value-of select="@id"/>
                </xsl:attribute>
                <xsl:attribute name="bodf:sourceId" select="@id" />
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechBy">
                <xsl:attribute name="by">
                    <xsl:text>#p</xsl:text>
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniPersonID"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechTo">
                <xsl:attribute name="to">
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechTo"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:if test="./bungeni:bungenimeta/bungeni:BungeniSpeechAs">
                <xsl:attribute name="as">
                    <xsl:value-of select="./bungeni:bungenimeta/bungeni:BungeniSpeechAs"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:apply-templates/>
        </speech>
    </xsl:template>


    <xsl:include href="../common/normalize_text_template.xsl"/>

</xsl:stylesheet>
