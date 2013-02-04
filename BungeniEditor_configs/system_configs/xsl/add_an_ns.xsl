<?xml version="1.0" encoding="UTF-8"?>
<!--
This template :
  ** attaches the Akoma Ntoso namespace as the default namespace to the root element
  ** attaches the bungeni-ODF namespace with the bodf prefix to the root element
  ** Information in the anx and bodf namespaces are not carried over in the final output
  xml , so the assumption is there are no elements with such prefixes in the input xml to
  this xslt.
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:bodf="http://editor.bungeni.org/1.0/odf/"
    version="2.0">
    <xsl:output indent="yes" method="xml" encoding="UTF-8"/>
    
    <xsl:template match="/">
        <xsl:apply-templates/>
    </xsl:template>

    <xsl:template match="*">
        <!-- attach the AN and BODF namespaces to the root element -->
        <xsl:choose>
            <!-- match default namespaces -->
            <xsl:when test="namespace-uri() eq '' or namespace-uri() eq 'http://www.metalex.org/1.0'">
                <xsl:element name="{node-name(.)}" namespace="http://www.akomantoso.org/2.0">
                    <xsl:namespace name="bodf" select="'http://editor.bungeni.org/1.0/odf/'"/>
                    <xsl:for-each select="@*">
                        <xsl:attribute name="{name(.)}">
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </xsl:for-each>
                    <xsl:apply-templates />
                </xsl:element>               
            </xsl:when>
            <xsl:otherwise>
            <!-- match proprietary namespaces 
                !+PROP_NS(ah, 04-02-2013)
            -->
                <xsl:element name="{node-name(.)}" namespace="{namespace-uri()}">
                    <xsl:namespace name="bodf" select="'http://editor.bungeni.org/1.0/odf/'"/>
                    <xsl:for-each select="@*">
                        <xsl:attribute name="{name(.)}">
                            <xsl:value-of select="."/>
                        </xsl:attribute>
                    </xsl:for-each>
                    <xsl:apply-templates />
                </xsl:element>               
            </xsl:otherwise>
        </xsl:choose>
 
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

</xsl:stylesheet>
