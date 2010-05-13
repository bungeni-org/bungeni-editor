<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
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
                xmlns:xsd="http://www.w3.org/2001/XMLSchema"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xmlns:of="urn:oasis:names:tc:opendocument:xmlns:of:1.2"
                xmlns:rdfa="http://docs.oasis-open.org/opendocument/meta/rdfa#"
                xmlns:field="urn:openoffice:names:experimental:ooo-ms-interop:xmlns:field:1.0"
                xmlns:rpt="http://openoffice.org/2005/report"
                xmlns:anx="http://anx.akomantoso.org/1.0"
				version="2.0">
    <xsl:output indent="yes" method="xml"/>
    
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    
	<xsl:template match="*">
        <xsl:element name="{node-name(.)}">
            <xsl:variable name="stylename" select="@text:style-name" />
			<xsl:for-each select="@*">
		    	<xsl:attribute name="{name(.)}">
		        	<xsl:value-of select="."/>
		        </xsl:attribute>
			</xsl:for-each>
           <!-- <xsl:attribute xmlns:saxon="http://saxon.sf.net/" name="linenumber" select="saxon:line-number(.)" />
            <xsl:attribute xmlns:saxon="http://saxon.sf.net/" name="columnnumber" select="saxon:column-number(.)" /> -->
            <xsl:for-each select="//style:*[@style:name=$stylename]//*/@*[contains(name(),'Bungeni')]">
                <xsl:attribute name="{node-name(.)}">
                    <xsl:value-of select="."/>
                </xsl:attribute>
            </xsl:for-each>
			<xsl:apply-templates />
		</xsl:element>    
	</xsl:template>
	
    <xsl:template match="office:scripts">
    </xsl:template>

    <xsl:template match="office:styles">
    </xsl:template>

    <xsl:template match="office:automatic-styles">
    </xsl:template>

    <xsl:template match="office:font-face-decls">
    </xsl:template>

    <xsl:template match="office:forms">
    </xsl:template>

    <xsl:template match="office:body">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="office:text">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="office:document-meta">
        <xsl:apply-templates />
    </xsl:template>

    <xsl:template match="text:sequence-decls">
    </xsl:template>

    <xsl:template match="text()">
        <xsl:value-of select="normalize-space(.)"/>
    </xsl:template>

</xsl:stylesheet>
