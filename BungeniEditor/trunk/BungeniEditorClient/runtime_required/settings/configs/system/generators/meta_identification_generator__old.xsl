<?xml version="1.0" encoding="UTF-8"?>
<!-- We use the xmeta: prefix to distinguish between the xsl: namespace used by the xslt
    and the xslt we want to generate -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:office="urn:oasis:names:tc:opendocument:xmlns:office:1.0"
    xmlns:style="urn:oasis:names:tc:opendocument:xmlns:style:1.0"
    xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0"
    xmlns:table="urn:oasis:names:tc:opendocument:xmlns:table:1.0"
    xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
    xmlns:fo="urn:oasis:names:tc:opendocument:xmlns:xsl-fo-compatible:1.0"
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:dc="http://purl.org/dc/elements/1.1/"
    xmlns:meta="urn:oasis:names:tc:opendocument:xmlns:meta:1.0"
    xmlns:number="urn:oasis:names:tc:opendocument:xmlns:datastyle:1.0"
    xmlns:svg="urn:oasis:names:tc:opendocument:xmlns:svg-compatible:1.0"
    xmlns:chart="urn:oasis:names:tc:opendocument:xmlns:chart:1.0"
    xmlns:dr3d="urn:oasis:names:tc:opendocument:xmlns:dr3d:1.0"
    xmlns:math="http://www.w3.org/1998/Math/MathML"
    xmlns:form="urn:oasis:names:tc:opendocument:xmlns:form:1.0"
    xmlns:script="urn:oasis:names:tc:opendocument:xmlns:script:1.0"
    xmlns:ooo="http://openoffice.org/2004/office" 
    xmlns:ooow="http://openoffice.org/2004/writer"
    xmlns:oooc="http://openoffice.org/2004/calc" 
    xmlns:dom="http://www.w3.org/2001/xml-events"
    xmlns:xforms="http://www.w3.org/2002/xforms" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:rpt="http://openoffice.org/2005/report"
    xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
    xmlns:xhtml="http://www.w3.org/1999/xhtml" 
    xmlns:grddl="http://www.w3.org/2003/g/data-view#"
    xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
    xmlns:anx="http://anx.akomantoso.org/1.0"    
    xmlns:bungeni="http://editor.bungeni.org/1.0/anx/"
    xmlns:xmeta="http://meta.w3.org/1999/XSL/Transform"
    exclude-result-prefixes="xs" version="2.0">
    <xd:doc xmlns:xd="http://www.oxygenxml.com/ns/doc/xsl" scope="stylesheet">
        <xd:desc>
            <xd:p><xd:b>Created on:</xd:b> May 24, 2012</xd:p>
            <xd:p><xd:b>Author:</xd:b> Ashok Hariharan</xd:p>
            <xd:p>
                This stylesheet converts ODF to Meta Language
            </xd:p>
        </xd:desc>
    </xd:doc>
    
    <xsl:output method="xml" indent="yes" />
    <!-- the namespace-alias call switches the output namespace back to the xsl namespace,
        this way for development we use the meta namespace and the output is always rendered
        as the xsl: namespace -->
    <xsl:namespace-alias stylesheet-prefix="xmeta" result-prefix="xsl"/>

    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="office:document-content">
        <root name="root" >
            <xsl:apply-templates />
        </root>
    </xsl:template>
    
    <xsl:template match="office:meta">
        <mcontainer name="meta">
            <mcontainer name="identification">
                <mcontainer name="general">
                    <!--
                    <meta name="doctype" value="{}" />
                    <meta name="language" value="{}" />
                    <meta name="country" value="{}" />
                    <meta name="doctype" value="{}" />
                    <meta name="doctype" value="{}" />
                    <meta name="doctype" value="{}" />
                    -->
                </mcontainer>
                <mcontainer name="frbrwork"> 
                    <meta name="uri" value="{//meta:user-defined[@name='BungeniWorkURI']}" />
                    <meta name="author" value="{//meta:user-defined[@name='BungeniWorkAuthor']}" />
                    <meta name="date" 
                        value="{//meta:user-defined[@name='BungeniWorkDate']}" 
                    />
                </mcontainer>
                <mcontainer name="frbrexpression">
                    <meta name="uri" value="{//meta:user-defined[@name='BungeniExpURI']}" />
                    <meta name="author" value="{//meta:user-defined[@name='BungeniExpAuthor']}" />
                    <meta name="date" 
                        value="{//meta:user-defined[@name='BungeniExpDate']}" 
                    />
                </mcontainer>
                <mcontainer name="frbrmanifestation">
                    <meta name="uri" value="{//meta:user-defined[@name='BungeniManURI']}" />
                    <meta name="author" value="{//meta:user-defined[@name='BungeniManAuthor']}" />
                    <meta name="date" 
                        value="{//meta:user-defined[@name='BungeniManDate']}" 
                    />
                </mcontainer>
            </mcontainer>
            <mcontainer name="publication">
                <meta name="publication_name" 
                    value="{//meta:user-defined[@name='BungeniPublicationName']}" />
            </mcontainer>
        </mcontainer>
    </xsl:template>
    
    <xsl:template match="meta:user-defined">
        <meta>
            <xsl:attribute name="name"><xsl:value-of select="@meta:name" /></xsl:attribute>
            <xsl:value-of select="."></xsl:value-of>
        </meta>
    </xsl:template>


    <!-- fix tabs and spaces -->

    <xsl:template match="text:tab"></xsl:template>
    
    <xsl:template match="text:s"></xsl:template>
    
    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)" />
    </xsl:template>
    
</xsl:stylesheet>
