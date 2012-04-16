<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="2.0" 
    exclude-result-prefixes="#all" 
    xmlns="http://www.akomantoso.org/2.0" xpath-default-namespace="http://www.akomantoso.org/2.0" 
    >
    
    <xsl:key name="outoflineMatcher" match="outOfLine" use="@href" />
    
    <!-- identity transform -->
    <xsl:template match="@*|*|processing-instruction()|comment()">
        <xsl:copy>
            <xsl:apply-templates select="*|@*|text()|processing-instruction()|comment()"/>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="components">
        <componentInfo>
                <xsl:apply-templates select="@* | node()" />
        </componentInfo>
    </xsl:template>
    
    <xsl:template match="component">
        <componentData>
            <xsl:apply-templates select="@* | node()" />
        </componentData>
    </xsl:template>

    <xsl:template match="event">
        <eventRef>
            <xsl:apply-templates select="@* | node()" />
        </eventRef>
    </xsl:template>

    <xsl:template match="publication">
        <xsl:copy>
            <xsl:attribute name="number" select="99999" />
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>

    <xsl:template match="content[parent::section]">
    
        <xsl:variable name="parentid" select="parent::section/@id" />
        <xsl:variable name="parentidhref" select="concat('#',$parentid)" />
        
        <xsl:choose>
         <xsl:when test="//outOfLine[@href eq $parentidhref]">
             <content>
                 <xsl:element name="p">
                     <xsl:element name="authorialNote">
                         <xsl:attribute name="id" select="generate-id()" />
                         <xsl:attribute name="placement"><xsl:text>side</xsl:text></xsl:attribute>
                         <xsl:copy-of select="//outOfLine[@href eq $parentidhref]/child::*"></xsl:copy-of>
                     </xsl:element>
                 </xsl:element>
                 <xsl:copy-of select="foreign" />
             </content>
         </xsl:when>
         <xsl:otherwise>
            <xsl:copy>
                <xsl:apply-templates select="@* | node()" />
            </xsl:copy>     
         </xsl:otherwise>
     </xsl:choose>   
    </xsl:template>
    
    <xsl:template match="outOfLines">
    </xsl:template>

</xsl:stylesheet>