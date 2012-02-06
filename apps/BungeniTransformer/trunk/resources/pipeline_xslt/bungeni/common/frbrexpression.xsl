<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : frbrexpression.xsl
    Created on : 18 October 2011, 11:30
    Author     : anthony
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
    xmlns:bp="http://www.bungeni.org/pipeline/1.0"
    xmlns:bu="http://portal.bungeni.org/1.0/"
    exclude-result-prefixes="bp"
    version="2.0"> 
    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>

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
    
   <xsl:template name="frbrexpression" bp:name="frbrexpression">
        <xsl:param name="contenttypename" />
        <xsl:param name="contenturidate" />
        <xsl:param name="contenturilang" />
        <FRBRExpression>
            <xsl:call-template name="frbrauthor" />
            <xsl:call-template name="frbrdate" >
                <xsl:with-param name="contenturidate" select="$contenturidate" />
            </xsl:call-template>
            <xsl:call-template name="frbruri" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
                <xsl:with-param name="contenturidate"  select="$contenturidate"/>
            </xsl:call-template>
            <xsl:call-template name="frbrthis" >
                <xsl:with-param name="contenttypename"  select="$contenttypename"/>
            </xsl:call-template>
            <xsl:call-template name="frbrlang" >
                <xsl:with-param name="contenturilang"  select="$contenturilang"/>
            </xsl:call-template>
        </FRBRExpression>
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template> 

</xsl:stylesheet>
