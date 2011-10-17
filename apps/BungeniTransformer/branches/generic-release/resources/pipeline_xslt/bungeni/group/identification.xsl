<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : indentification.xsl
    Created on : 14 October 2011, 10:50
    Author     : anthony
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:bu="http://portal.bungeni.org/1.0/"
    version="2.0">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes" />

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

    <xsl:template match="bp:contenttype">
        <identification>
            <xsl:attribute name="source" namespace="http://portal.bungeni.org/1.0/">#bungeni</xsl:attribute>
            <FRBRWork>
                <xsl:for-each select="bu:owner/bp:field">
                    <this>
                        <xsl:attribute name="value">
                            <xsl:value-of select="@name" />
                        </xsl:attribute>
                        <xsl:attribute name="countno">
                            <xsl:value-of select="position()" />
                        </xsl:attribute>                        
                    </this>
                </xsl:for-each>
            </FRBRWork>
            <FRBRExpession>indentification.xsl</FRBRExpession>
            <FRBRManifestation>indentification.xsl</FRBRManifestation>
            <FRBRItem>indentification.xsl</FRBRItem>
        </identification>
    </xsl:template>

</xsl:stylesheet>
